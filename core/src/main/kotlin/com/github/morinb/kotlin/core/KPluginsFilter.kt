/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.github.morinb.kotlin.core

import java.io.File
import java.io.FileFilter

internal object KPluginsFilter : FileFilter {
    override fun accept(pathname: File?): Boolean {
        return pathname?.isFile == true && pathname.name.lowercase().endsWith(".jar")
    }

}