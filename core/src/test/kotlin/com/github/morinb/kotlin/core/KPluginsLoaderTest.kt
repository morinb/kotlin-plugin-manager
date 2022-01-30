/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.github.morinb.kotlin.core

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

internal class KPluginsLoaderTest {

    @Test
    fun reloadPlugins() {

        val reloadPlugins = KPluginsLoader.reloadPlugins()

        assertNotNull(reloadPlugins)

    }
}