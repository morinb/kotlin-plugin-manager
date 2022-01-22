/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.github.morinb.kotlin.shared

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

    private lateinit var manager: KPluginManager
    private lateinit var defaultPlugin: KPlugin


    @BeforeEach
    fun setUp() {
        manager = KPluginManagerImplTest()
        defaultPlugin = object : KPlugin {
            override val logger: Logger
                get() = logger<KPluginManagerTest>()

            override fun id(): PluginId = "id-123"
            override var enabledAt: LocalDateTime? = null
        }
    }

    @Test
    fun `Plugin Manager register function should store plugin`() {
        assertEquals(0, manager.plugins().count())
        manager.register(defaultPlugin)
        assertEquals(1, manager.plugins().count())
    }

    @Test
    fun `Default plugin should not be enabled`() {
        val id = manager.register(defaultPlugin)!!
        assertFalse { manager.getPlugin(id)!!.isEnabled() }
    }

    @Test
    fun `Enabling a plugin should define its enabledAt date`() {
        val id = manager.register(defaultPlugin)!!
        manager.enable(id)
        assertNotNull(manager.getPlugin(id)!!.enabledAt)
    }

    @Test
    fun `Registering twice a plugin should do nothing`() {
        assertNotNull(manager.register(defaultPlugin))
        assertNull(manager.register(defaultPlugin))
    }

    @Test
    fun `Disabling a plugin should nullify its enableAt date`() {
        val id = manager.register(defaultPlugin)!!
        manager.enable(id)
        manager.disable(id)
        assertNull(manager.getPlugin(id)!!.enabledAt)
    }

    @Test
    fun `plugins method can have a filter`() {
        manager.register(defaultPlugin)
        assertEquals(1, manager.plugins(PluginFilters.DISABLED).count())
    }
}