/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.github.morinb.kotlin.shared

typealias PluginId = String
typealias Author = String
typealias PluginFilter = (KPlugin) -> Boolean

/**
 * Plugin filter enums to be used with [KPluginManager.plugins] function.
 */
@Suppress("unused")
enum class PluginFilters(private val filter: PluginFilter) : PluginFilter {
    ALLOW_ALL({ true }),
    ENABLED({ it.isEnabled() }),
    DISABLED({ !it.isEnabled() }),
    ;


    /**
     * Overridden invoke function to return the underlying filter instead of the enum object.
     * Thus allowing us to use the enum object as a plugin filter directly.
     */
    override fun invoke(kPlugin: KPlugin): Boolean {
        return filter.invoke(kPlugin)
    }

}
