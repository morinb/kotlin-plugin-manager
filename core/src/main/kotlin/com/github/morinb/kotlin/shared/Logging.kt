/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.github.morinb.kotlin.shared

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * inline reified function to create a SLF4J [Logger] easily.
 */
@Suppress("unused")
inline fun <reified T> logger(): Logger = LoggerFactory.getLogger(T::class.java)
