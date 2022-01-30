/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.github.morinb.kotlin.shared

import org.junit.jupiter.api.Test
import org.slf4j.Logger
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class KPluginTest {
    private val plugin = object : KPlugin {
        override val logger: Logger
            get() = logger<KPluginTest>()

        override fun id(): PluginId = "plugin-id"

        override var enabledAt: LocalDateTime? = null
    }

    @Test
    fun `default plugin name is its id`() {
        assertEquals("plugin-id", plugin.name())
    }

    @Test
    fun `default plugin authors list is empty`() {
        assertNotNull(plugin.authors())
        assertTrue { plugin.authors().isEmpty() }
    }

    @Test
    fun `default plugin depends on nothing`() {
        assertTrue { plugin.dependsOn().isEmpty() }
    }

}