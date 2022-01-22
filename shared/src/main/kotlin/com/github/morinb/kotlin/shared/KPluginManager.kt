/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.github.morinb.kotlin.shared

@Suppress("unused")
interface KPluginManager {
    fun getPlugin(id: PluginId): KPlugin?

    /**
     * Enables a specific plugin.
     */
    fun enable(id: PluginId)

    /**
     * Disables a specific plugin
     */
    fun disable(id: PluginId)

    /**
     * Register a newly discovered plugin.
     */
    fun register(plugin: KPlugin): PluginId?


    /**
     * List all known plugins, filtered by a plugin filter.
     * Example plugin filters can be found in the [PluginFilters] enum class.
     */
    fun plugins(filter: PluginFilter = PluginFilters.ALLOW_ALL): List<KPlugin>


}