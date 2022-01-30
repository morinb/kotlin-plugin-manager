/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.github.morinb.kotlin.shared

import org.slf4j.Logger
import java.time.LocalDateTime

class KPluginManagerImplTest : KPluginManager {
    private val logger: Logger = logger<KPluginManagerImplTest>()

    /**
     * basic implementation as a Map
     */
    private val plugins = mutableMapOf<PluginId, KPlugin>()

    override fun getPlugin(id: PluginId): KPlugin? {
        logger.debug("Getting plugin with id : $id")
        return plugins[id]
    }

    override fun enable(id: PluginId): KPlugin? {
        logger.debug("Enabling plugin with id : $id")

        val plugin = getPlugin(id)

        val dependsOn = plugin?.dependsOn()
        if (dependsOn?.isNotEmpty() == true) {
            dependsOn.forEach { enable(it) }
        }

        plugin?.preEnable()
        plugin?.enabledAt = LocalDateTime.now()
        plugin?.postEnable()

        return plugin
    }

    override fun disable(id: PluginId): KPlugin? {
        logger.debug("Disabling plugin with id : $id")

        val plugin = getPlugin(id)
        plugin?.preDisable()
        plugin?.enabledAt = null
        plugin?.postDisable()

        return plugin
    }

    override fun register(plugin: KPlugin): PluginId? {

        val id = plugin.id()
        return if (!plugins.containsKey(id)) {
            plugins[id] = plugin
            logger.debug("Registering plugin with id : $id")
            id
        } else {
            logger.debug("plugin with id : $id already registered")
            null
        }

    }

    override fun plugins(filter: PluginFilter): List<KPlugin> {
        logger.debug("Listing plugins")
        return plugins.filter { filter(it.value) }.values.toList()
    }

    override fun clearCache() {
        plugins.clear()
    }
}