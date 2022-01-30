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
import java.nio.file.Files

internal class KPluginsFilterTest {

    @Test
    fun accept() {
        var fileJar: File? = null
        var fileTxt: File? = null
        var folder: File? = null

        try {
            fileJar = File.createTempFile("test", ".jar")
            fileTxt = File.createTempFile("test", ".txt")
            val createDirectory = Files.createTempDirectory("test")
            println(createDirectory.toAbsolutePath())


            folder = createDirectory.toFile()

            assertTrue(KPluginsFilter.accept(fileJar))
            assertFalse(KPluginsFilter.accept(fileTxt))
            assertFalse(KPluginsFilter.accept(null))
            assertFalse(KPluginsFilter.accept(folder))
        } catch (ex: FileAlreadyExistsException) {
            ex.printStackTrace()

        } finally {
            fileJar?.delete()
            fileTxt?.delete()
            folder?.delete()
        }
    }
}