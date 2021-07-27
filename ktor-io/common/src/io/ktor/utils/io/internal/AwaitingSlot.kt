/*
* Copyright 2014-2021 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
*/

package io.ktor.utils.io.internal

import io.ktor.utils.io.*
import kotlinx.atomicfu.*
import kotlinx.coroutines.*
import kotlin.coroutines.*

/**
 * Exclusive slot for waiting.
 * Only one waiter allowed.
 */
internal class AwaitingSlot {
    private val suspension: AtomicRef<Continuation<Unit>?> = atomic(null)

    init {
        makeShared()
    }

    /**
     * Wait for other [sleep] or resume.
     */
    suspend fun sleep() {
        if (trySuspend()) {
            return
        }

        resume()
    }

    /**
     * Resume waiter.
     */
    fun resume() {
        suspension.getAndSet(null)?.resume(Unit)
    }

    /**
     * Cancel waiter.
     */
    fun cancel(cause: Throwable?) {
        val continuation = suspension.getAndSet(null) ?: return

        if (cause != null) {
            continuation.resumeWithException(cause)
        } else {
            continuation.resume(Unit)
        }
    }

    private suspend fun trySuspend(): Boolean {
        var suspended = false

        suspendCancellableCoroutine<Unit> {
            if (suspension.compareAndSet(null, it)) {
                suspended = true
            } else {
                it.resume(Unit)
            }
        }

        return suspended
    }
}
