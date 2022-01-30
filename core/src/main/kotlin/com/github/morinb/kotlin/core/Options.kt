/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.github.morinb.kotlin.core

import java.util.prefs.Preferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty


/**
 * Here are stored and synchronized all options of the application
 */


@Suppress("unused")
object Options {
    private val systemPreferences by lazy { Preferences.systemNodeForPackage(Options::class.java) }
    private val userPreferences by lazy { Preferences.userNodeForPackage(Options::class.java) }

    var modulePath by preference(userPreferences, "modulePath", "modules")


    internal var forTestsUser by preference(userPreferences, "for-test", "test-test")
    internal var forTestsSystem by preference(systemPreferences, "for-test-sys", "test-test-sys")

    internal var forTestInt by preference(userPreferences, "for-test-int", 1)
    internal var forTestLong by preference(userPreferences, "for-test-long", 1L)
    internal var forTestFloat by preference(userPreferences, "for-test-float", 1F)
    internal var forTestDouble by preference(userPreferences, "for-test-double", 1.0)
    internal var forTestBoolean by preference(userPreferences, "for-test-boolean", true)
    internal var forTestByteArray by preference(
        userPreferences,
        "for-test-byte-array",
        "test".toByteArray(Charsets.UTF_8)
    )
    internal var forTestUnknown by preference(userPreferences, "for-test-unknown", KPluginsFilter)
}


inline fun <reified T : Any> preference(preferences: Preferences, key: String, defaultValue: T) =
    PreferenceDelegate(preferences, key, defaultValue, T::class)

class PreferenceDelegate<T : Any>(
    private val preferences: Preferences,
    private val key: String,
    private val defaultValue: T,
    private val type: KClass<T>
) :
    ReadWriteProperty<Any, T> {

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return with(preferences) {
            when (type) {
                Int::class -> getInt(key, defaultValue as Int)
                Long::class -> getLong(key, defaultValue as Long)
                Float::class -> getFloat(key, defaultValue as Float)
                Double::class -> getDouble(key, defaultValue as Double)
                Boolean::class -> getBoolean(key, defaultValue as Boolean)
                String::class -> get(key, defaultValue as String)
                ByteArray::class -> getByteArray(key, defaultValue as ByteArray)
                else -> error("Unsupported preference type $type.")
            } as T
        }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        with(preferences) {
            when (type) {
                Int::class -> putInt(key, value as Int)
                Long::class -> putLong(key, value as Long)
                Float::class -> putFloat(key, value as Float)
                Double::class -> putDouble(key, value as Double)
                Boolean::class -> putBoolean(key, value as Boolean)
                String::class -> put(key, value as String)
                ByteArray::class -> putByteArray(key, value as ByteArray)
                else -> error("Unsupported preference type $type.")
            }
        }
    }

}
