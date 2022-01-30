/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.github.morinb.kotlin.core

import com.github.morinb.kotlin.shared.KPlugin
import com.github.morinb.kotlin.shared.PluginFilters
import com.github.morinb.kotlin.shared.PluginId
import com.github.morinb.kotlin.shared.logger
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.slf4j.Logger
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KPluginManagerTest {

    private lateinit var defaultPlugin: KPlugin


    @BeforeEach
    fun setUp() {
        KPluginManager.clearCache()
        defaultPlugin = object : KPlugin {
            override val logger: Logger
                get() = logger<KPluginManagerTest>()

            override fun id(): PluginId = "id-123"
            override var enabledAt: LocalDateTime? = null
        }
    }

    @Test
    fun `Plugin Manager register function should store plugin`() {
        assertEquals(0, KPluginManager.plugins().count())
        KPluginManager.register(defaultPlugin)
        assertEquals(1, KPluginManager.plugins().count())
    }

    @Test
    fun `Default plugin should not be enabled`() {
        val id = KPluginManager.register(defaultPlugin)
        assertFalse { KPluginManager.getPlugin(id)!!.isEnabled() }
    }

    @Test
    fun `Enabling a plugin should define its enabledAt date`() {
        val id = KPluginManager.register(defaultPlugin)
        KPluginManager.enable(id)
        assertNotNull(KPluginManager.getPlugin(id)!!.enabledAt)
    }

    @Test
    fun `Registering twice a plugin should do nothing`() {
        assertEquals(0, KPluginManager.plugins().count())
        KPluginManager.register(defaultPlugin)
        assertEquals(1, KPluginManager.plugins().count())
        KPluginManager.register(defaultPlugin)
        assertEquals(1, KPluginManager.plugins().count())
    }

    @Test
    fun `Disabling a plugin should nullify its enableAt date`() {
        val id = KPluginManager.register(defaultPlugin)
        KPluginManager.enable(id)
        KPluginManager.disable(id)
        assertNull(KPluginManager.getPlugin(id)!!.enabledAt)
    }

    @Test
    fun `plugins method can have a filter`() {
        KPluginManager.register(defaultPlugin)
        assertEquals(1, KPluginManager.plugins(PluginFilters.DISABLED).count())
    }

    @Test
    fun `plugins does not accept null filter`() {
        KPluginManager.register(defaultPlugin)
        KPluginManager.plugins { false }
    }
}