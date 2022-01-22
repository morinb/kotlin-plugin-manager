/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.github.morinb.kotlin.shared

import org.slf4j.Logger
import java.time.LocalDateTime

/**
 * Base interface of all plugins.
 */
@Suppress("unused")
interface KPlugin {
    val logger: Logger


    /**
     * Returns the name of the plugin.
     * Default to the plugin [id]
     */
    fun name(): String = id()

    /**
     * Returns the unique identifier of the plugin
     */
    fun id(): PluginId


    /**
     * Called before the plugin is enabled. Does nothing by default.
     */
    fun preEnable() {
        // Default implementation does nothing
        logger.debug("Pre enable")

    }

    /**
     * Called after the plugin is enabled. Does nothing by default.
     */
    fun postEnable() {
        // Default implementation does nothing
        logger.debug("Post enable")

    }

    /**
     * Called before the plugin is disabled. Does nothing by default.
     */

    fun preDisable() {
        // Default implementation does nothing
        logger.debug("Pre Disable")
    }

    /**
     * Called after the plugin is disabled. Does nothing by default.
     */

    fun postDisable() {
        // Default implementation does nothing
        logger.debug("Post disable")
    }

    /**
     * Defines a list of Plugins the plugin depends on. Those plugins **must** (and will) be enabled before enabling this plugin.
     * Defaults to an empty list.
     */
    fun dependsOn(): List<PluginId> = emptyList()

    /**
     * Returns the plugin authors.
     * Defaults to an empty list.
     */
    fun authors(): List<Author> = emptyList()


    /**
     * date time at which the plugin was last enabled.
     */
    var enabledAt: LocalDateTime?

    /**
     * Is the plugin enabled ?
     */
    fun isEnabled(): Boolean = enabledAt != null

}