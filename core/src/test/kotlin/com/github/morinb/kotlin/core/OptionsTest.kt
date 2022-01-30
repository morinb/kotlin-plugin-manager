/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.github.morinb.kotlin.core

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.prefs.Preferences

internal class OptionsTest {

    @Test
    fun testOptions() {
        Preferences.userNodeForPackage(Options::class.java).remove("for-test")
        Preferences.systemNodeForPackage(Options::class.java).remove("for-test-sys")


        assertEquals("test-test", Options.forTestsUser)
        assertEquals("test-test-sys", Options.forTestsSystem)

        Options.forTestsUser = "testing"
        Options.forTestsSystem = "testing-system"

        assertEquals("testing", Options.forTestsUser)
        assertEquals("testing-system", Options.forTestsSystem)

        Preferences.userNodeForPackage(Options::class.java).remove("for-test")
        Preferences.systemNodeForPackage(Options::class.java).remove("for-test-sys")
    }

    @Test
    fun testInt() {
        Preferences.userNodeForPackage(Options::class.java).remove("for-test-int")
        assertEquals(1, Options.forTestInt)
        Options.forTestInt = 1000
        assertEquals(1000, Options.forTestInt)
        Preferences.userNodeForPackage(Options::class.java).remove("for-test-int")
    }

    @Test
    fun testLong() {
        Preferences.userNodeForPackage(Options::class.java).remove("for-test-long")
        assertEquals(1L, Options.forTestLong)
        Options.forTestLong = 1000L
        assertEquals(1000L, Options.forTestLong)
        Preferences.userNodeForPackage(Options::class.java).remove("for-test-long")
    }

    @Test
    fun testFloat() {
        Preferences.userNodeForPackage(Options::class.java).remove("for-test-float")
        assertEquals(1F, Options.forTestFloat)
        Options.forTestFloat = 1000F
        assertEquals(1000F, Options.forTestFloat)
        Preferences.userNodeForPackage(Options::class.java).remove("for-test-float")
    }

    @Test
    fun testDouble() {
        Preferences.userNodeForPackage(Options::class.java).remove("for-test-double")
        assertEquals(1.0, Options.forTestDouble)
        Options.forTestDouble = 1000.0
        assertEquals(1000.0, Options.forTestDouble)
        Preferences.userNodeForPackage(Options::class.java).remove("for-test-double")
    }

    @Test
    fun testBoolean() {
        Preferences.userNodeForPackage(Options::class.java).remove("for-test-boolean")
        assertEquals(true, Options.forTestBoolean)
        Options.forTestBoolean = false
        assertEquals(false, Options.forTestBoolean)
        Preferences.userNodeForPackage(Options::class.java).remove("for-test-boolean")
    }

    @Test
    fun testByteArray() {
        val expected = "test".toByteArray(Charsets.UTF_8)
        Preferences.userNodeForPackage(Options::class.java).remove("for-test-byte-array")
        assertArrayEquals(expected, Options.forTestByteArray)
        val expected2 = "testing".toByteArray(Charsets.UTF_8)
        Options.forTestByteArray = expected2
        assertArrayEquals(expected2, Options.forTestByteArray)
        Preferences.userNodeForPackage(Options::class.java).remove("for-test-byte-array")
    }

    @Test
    fun testUnknownOptionType() {
        val exceptionGet = assertThrows(IllegalStateException::class.java) {
            Options.forTestUnknown
        }
        assertEquals(
            "Unsupported preference type class com.github.morinb.kotlin.core.KPluginsFilter.",
            exceptionGet.message
        )
        val exceptionPut = assertThrows(IllegalStateException::class.java) {
            Options.forTestUnknown = KPluginsFilter
        }
        assertEquals(
            "Unsupported preference type class com.github.morinb.kotlin.core.KPluginsFilter.",
            exceptionPut.message
        )
    }
}