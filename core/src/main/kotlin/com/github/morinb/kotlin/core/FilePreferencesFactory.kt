/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.github.morinb.kotlin.core

import com.github.morinb.kotlin.shared.logger
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import java.util.prefs.AbstractPreferences
import java.util.prefs.BackingStoreException
import java.util.prefs.Preferences
import java.util.prefs.PreferencesFactory


class FilePreferencesFactory : PreferencesFactory {
    private var rootPreferences: Preferences? = null


    override fun systemRoot(): Preferences? = userRoot()

    override fun userRoot(): Preferences? {
        if (rootPreferences == null) {
            logger.debug("Instantiating root preferences")

            rootPreferences = FilePreferences(null, "")
        }
        return rootPreferences
    }

    companion object {
        private val logger = logger<FilePreferencesFactory>()
        const val SYSTEM_PROPERTY_FILE = "com.github.morinb.kotlin.prefs.FilePreferencesFactory.file"
        private var preferenceFile: File? = null

        fun getPreferencesFile(): File? {
            if (preferenceFile == null) {
                var prefsFile = System.getProperty(SYSTEM_PROPERTY_FILE)
                if (prefsFile == null || prefsFile.isBlank()) {
                    prefsFile = System.getProperty("user.home") + File.separator + ".fileprefs"
                }
                preferenceFile = File(prefsFile).absoluteFile
                logger.debug("Preferences file is $preferenceFile")
            }
            return preferenceFile
        }
    }
}

class FilePreferences(parent: AbstractPreferences?, name: String) : AbstractPreferences(parent, name) {
    private val logger = logger<FilePreferences>()
    private val root: MutableMap<String, String>
    private val children: MutableMap<String, FilePreferences>
    private var isRemovedBoolean = false

    init {
        logger.debug("Instantiating node $name")
        root = TreeMap<String, String>()
        children = TreeMap<String, FilePreferences>()
        try {
            sync()
        } catch (e: BackingStoreException) {
            logger.error("Unable to sync on creation of node $name", e)
        }
    }

    override fun putSpi(key: String, value: String?) {
        root[key] = value!!
        try {
            flush()
        } catch (e: BackingStoreException) {
            logger.error("Unable to flush after putting $key", e)
        }
    }

    override fun getSpi(key: String?): String? {
        return root[key]
    }

    override fun removeSpi(key: String) {
        root.remove(key)
        try {
            flush()
        } catch (e: BackingStoreException) {
            logger.error("Unable to flush after removing $key", e)
        }
    }

    @Throws(BackingStoreException::class)
    override fun removeNodeSpi() {
        isRemovedBoolean = true
        flush()
    }

    @Throws(BackingStoreException::class)
    override fun keysSpi(): Array<String?> {
        return root.keys.toTypedArray()
    }

    @Throws(BackingStoreException::class)
    override fun childrenNamesSpi(): Array<String?> {
        return children.keys.toTypedArray()
    }

    override fun childSpi(name: String?): FilePreferences {
        var child = children[name]
        if (child == null || child.isRemoved) {
            child = FilePreferences(this, name!!)
            children[name] = child
        }
        return child
    }


    @Throws(BackingStoreException::class)
    override fun syncSpi() {
        if (isRemoved) return
        val file: File? = FilePreferencesFactory.getPreferencesFile()
        if (file != null) {
            if (!file.exists()) return
            synchronized(file) {
                val p = Properties()
                try {
                    p.load(FileInputStream(file))
                    val sb = StringBuilder()
                    getPath(sb)
                    val path = sb.toString()
                    val pnen = p.propertyNames()
                    while (pnen.hasMoreElements()) {
                        val propKey = pnen.nextElement() as String
                        if (propKey.startsWith(path)) {
                            val subKey = propKey.substring(path.length)
                            // Only load immediate descendants
                            if (subKey.indexOf('.') == -1) {
                                root[subKey] = p.getProperty(propKey)
                            }
                        }
                    }
                } catch (e: IOException) {
                    throw BackingStoreException(e)
                }
            }
        }
    }

    private fun getPath(sb: StringBuilder) {
        val parent = (parent() ?: return) as FilePreferences
        parent.getPath(sb)
        sb.append(name()).append('.')
    }

    @Throws(BackingStoreException::class)
    override fun flushSpi() {
        val file: File? = FilePreferencesFactory.getPreferencesFile()
        if (file != null) {


            synchronized(file) {
                val p = Properties()
                try {
                    val sb = StringBuilder()
                    getPath(sb)
                    val path = sb.toString()
                    if (file.exists()) {
                        p.load(FileInputStream(file))
                        val toRemove: MutableList<String> = ArrayList()

                        // Make a list of all direct children of this node to be removed
                        val pnen = p.propertyNames()
                        while (pnen.hasMoreElements()) {
                            val propKey = pnen.nextElement() as String
                            if (propKey.startsWith(path)) {
                                val subKey = propKey.substring(path.length)
                                // Only do immediate descendants
                                if (subKey.indexOf('.') == -1) {
                                    toRemove.add(propKey)
                                }
                            }
                        }

                        // Remove them now that the enumeration is done with
                        for (propKey in toRemove) {
                            p.remove(propKey)
                        }
                    }

                    // If this node hasn't been removed, add back in any values
                    if (!isRemovedBoolean) {
                        for (s in root.keys) {
                            p.setProperty(path + s, root[s])
                        }
                    }
                    p.store(FileOutputStream(file), "FilePreferences")
                } catch (e: IOException) {
                    throw BackingStoreException(e)
                }
            }
        }
    }
}
