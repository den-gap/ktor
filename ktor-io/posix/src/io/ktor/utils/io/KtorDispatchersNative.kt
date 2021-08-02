/*
 * Copyright 2014-2021 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.utils.io

import kotlinx.coroutines.*

public actual object KtorDispatchers {
    public actual val Default: CoroutineDispatcher
        get() = Dispatchers.Default

    actual fun createIoDispatcher(
        name: String,
        threadCount: Int
    ): CoroutineDispatcher = TODO()

}
