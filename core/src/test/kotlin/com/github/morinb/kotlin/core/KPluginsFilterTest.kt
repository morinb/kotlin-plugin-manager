/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.github.morinb.kotlin.core

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.File

internal class KPluginsFilterTest {

    @Test
    fun accept() {
        lateinit var fileJar: File
        lateinit var fileTxt: File

        try {
            fileJar = File.createTempFile("test", ".jar")
            fileTxt = File.createTempFile("test", ".txt")

            assertTrue(KPluginsFilter.accept(fileJar))
            assertFalse(KPluginsFilter.accept(fileTxt))
        } finally {
            fileJar.delete()
            fileTxt.delete()
        }
    }
}