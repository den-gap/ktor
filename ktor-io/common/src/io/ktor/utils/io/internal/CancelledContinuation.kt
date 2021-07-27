/*
 * Copyright 2014-2021 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.utils.io.internal

import kotlin.coroutines.*
import kotlin.coroutines.cancellation.*

internal class CancelledContinuation(val cause: Throwable?) : Continuation<Unit> {
    override val context: CoroutineContext
        get() = EmptyCoroutineContext

    override fun resumeWith(result: Result<Unit>) {
        cause?.let { throw CancellationException(it) }
    }
}
