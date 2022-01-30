/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.github.morinb.kotlin.core

import org.junit.jupiter.api.Assertions.assertEquals
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
}