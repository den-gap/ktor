/*
* Copyright 2014-2021 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
*/

package io.ktor.utils.io.internal

import io.ktor.utils.io.*
import kotlinx.atomicfu.*
import kotlinx.coroutines.internal.*
import kotlin.coroutines.*
import kotlin.coroutines.cancellation.*
import kotlin.coroutines.intrinsics.*

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
        val value = suspension.value
        if (value is CancelledSlot) {
            value.resume(Unit)
            return
        }

        suspension.getAndSet(null)?.resume(Unit)
    }

    /**
     * Cancel waiter.
     */
    fun cancel(cause: Throwable?) {
        val slot = CancelledSlot(cause)
        while (true) {
            val current = suspension.value
            if (current is CancelledSlot) {
                current.resume(Unit)
                break
            }

            if (!suspension.compareAndSet(current, slot)) continue
            current ?: break

            if (cause != null) {
                current.resumeWithException(CancellationException(cause))
            } else {
                current.resume(Unit)
            }

            break
        }
    }

    private suspend fun trySuspend(): Boolean {
        var suspended = false

        val current = suspension.value
        if (current is CancelledSlot) {
            current.resume(Unit)
            return false
        }

        suspendCoroutineUninterceptedOrReturn<Unit> {
            if (!suspension.compareAndSet(null, it)) return@suspendCoroutineUninterceptedOrReturn Unit

            suspended = true
            COROUTINE_SUSPENDED
        }

        return suspended
    }
}
