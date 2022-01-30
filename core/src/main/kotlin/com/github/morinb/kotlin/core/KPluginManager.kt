/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.github.morinb.kotlin.core

import com.github.morinb.kotlin.shared.KPlugin
import com.github.morinb.kotlin.shared.KPluginManager
import com.github.morinb.kotlin.shared.PluginFilter
import com.github.morinb.kotlin.shared.PluginId
import java.time.LocalDateTime

/**
 * Access, enable and disable a plugin by its id
 */
object PluginManager : KPluginManager {

    private val pluginCache = mutableMapOf<PluginId, KPlugin>()

    override fun getPlugin(id: PluginId): KPlugin? = pluginCache[id]

    override fun enable(id: PluginId) =
        getPlugin(id)?.apply {
            preEnable()
            enabledAt = LocalDateTime.now()
            postEnable()
        }


    override fun disable(id: PluginId) =
        getPlugin(id)?.apply {
            preDisable()
            enabledAt = null
            postDisable()
        }

    override fun register(plugin: KPlugin): PluginId {
        val id = plugin.id()
        pluginCache.computeIfAbsent(id) { plugin }
        return id
    }

    override fun plugins(filter: PluginFilter): List<KPlugin> {
        return pluginCache.values.filter(filter).toList()
    }

    override fun clearCache() {
        pluginCache.clear()
    }

}