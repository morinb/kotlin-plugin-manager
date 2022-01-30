/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.github.morinb.kotlin.core

import com.github.morinb.kotlin.shared.KPlugin
import java.io.File
import java.net.URL
import java.net.URLClassLoader
import java.security.AccessController
import java.security.PrivilegedExceptionAction
import java.util.jar.JarFile
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.isSubclassOf

/**
 * Loads plugins located in directory specified by the modulePath option, and whose MANIFEST.MF file contains a Plugin-Class entry with the full path of the Plugin main class.
 */
@Suppress("unused")
object KPluginsLoader {

    private lateinit var classLoader: URLClassLoader

    /**
     * Loads and reloads plugin from the modules directory, and register them in the [PluginManager]
     */
    fun reloadPlugins(): Collection<KPlugin> {
        val pluginClassNames = mutableListOf<String>()
        val pluginFileUrl = mutableListOf<URL>()
        File(Options.modulePath).listFiles(KPluginsFilter)?.forEach { pluginFile ->
            val jarFile = JarFile(pluginFile)
            val pluginClass = jarFile.manifest.mainAttributes.getValue("Plugin-Class")
            pluginClassNames.add(pluginClass)
            pluginFileUrl.add(pluginFile.toURI().toURL())
        }

        classLoader = AccessController.doPrivileged(PrivilegedExceptionAction {
            URLClassLoader(pluginFileUrl.toTypedArray(), KPluginsLoader::class.java.classLoader)
        })

        pluginClassNames.forEach { clazz ->
            val pluginKClass = Class.forName(clazz, true, classLoader).kotlin
            if (pluginKClass.isSubclassOf(KPlugin::class)) {
                val plugin = pluginKClass.createInstance() as KPlugin
                PluginManager.register(plugin)
            }
        }

        return PluginManager.plugins()
    }

}