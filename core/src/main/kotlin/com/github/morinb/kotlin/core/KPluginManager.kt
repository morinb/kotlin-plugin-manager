/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.github.morinb.kotlin.core

import com.github.morinb.kotlin.shared.KPlugin
import com.github.morinb.kotlin.shared.PluginFilter
import com.github.morinb.kotlin.shared.PluginFilters
import com.github.morinb.kotlin.shared.PluginId
import java.time.LocalDateTime

/**
 * Access, enable and disable a plugin by its id
 */
object KPluginManager {

    private val pluginCache = mutableMapOf<PluginId, KPlugin>()

    fun getPlugin(id: PluginId): KPlugin? = pluginCache[id]

    fun enable(id: PluginId) =
        getPlugin(id)?.apply {
            preEnable()
            enabledAt = LocalDateTime.now()
            postEnable()
        }


    fun disable(id: PluginId) =
        getPlugin(id)?.apply {
            preDisable()
            enabledAt = null
            postDisable()
        }

    fun register(plugin: KPlugin): PluginId {
        return pluginCache.computeIfAbsent(plugin.id()) { plugin }.id()
    }

    fun plugins(filter: PluginFilter = PluginFilters.ALLOW_ALL): List<KPlugin> {
        return pluginCache.values.filter(filter).toList()
    }

    fun clearCache() {
        pluginCache.clear()
    }

}