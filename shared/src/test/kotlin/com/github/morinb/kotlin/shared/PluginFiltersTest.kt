/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.github.morinb.kotlin.shared

import org.slf4j.Logger
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals


class PluginFiltersTest {

    private val pluginsList = listOf(
        object : KPlugin {
            override val logger: Logger
                get() = logger<PluginFiltersTest>()

            override fun id(): PluginId {
                return "id1"
            }

            override var enabledAt: LocalDateTime? = null
        },
        object : KPlugin {
            override val logger: Logger
                get() = logger<PluginFiltersTest>()

            override fun id(): PluginId {
                return "id2"
            }

            override var enabledAt: LocalDateTime? = LocalDateTime.now().minusHours(3)
        },
        object : KPlugin {
            override val logger: Logger
                get() = logger<PluginFiltersTest>()

            override fun id(): PluginId {
                return "id3"
            }

            override var enabledAt: LocalDateTime? = LocalDateTime.now()
        },


        )


    @Test
    fun `test that PluginFilter ALLOW_ALL let all plugins be found`() {
        assertEquals(pluginsList.count(), pluginsList.count { PluginFilters.ALLOW_ALL(it) })
    }

    @Test
    fun `test that PluginFilter ENABLE let only enabled plugins be found`() {
        assertEquals(2, pluginsList.count { PluginFilters.ENABLED(it) })
    }

    @Test
    fun `test that PluginFilter DISABLED let only disabled plugins be found`() {
        assertEquals(1, pluginsList.count { PluginFilters.DISABLED(it) })
    }


}