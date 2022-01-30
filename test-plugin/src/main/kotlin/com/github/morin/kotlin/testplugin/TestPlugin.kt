/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.github.morin.kotlin.testplugin

import com.github.morinb.kotlin.shared.KPlugin
import com.github.morinb.kotlin.shared.PluginId
import com.github.morinb.kotlin.shared.logger
import org.slf4j.Logger
import java.time.LocalDateTime

@Suppress("unused")
class TestPlugin : KPlugin {
    override val logger: Logger
        get() = logger<TestPlugin>()

    override fun name(): String {
        return "Test Plugin"
    }

    override fun id(): PluginId = "Test Plugin ID"


    override var enabledAt: LocalDateTime? = null
}