/*
 * Copyright 2014-2021 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.utils.io

import io.ktor.utils.io.bits.*
import io.ktor.utils.io.internal.*
import io.ktor.utils.io.internal.CancelledContinuation
import kotlinx.atomicfu.*
import kotlinx.coroutines.*
import kotlin.coroutines.*

/**
 * TODO()
 */
public class ByteChannel {
    private val _isClosed = atomic(false)
    private val _closeReason = atomic<Throwable?>(null)

    public val source: BytesSource = object : BytesSource() {
        override val isClosed: Boolean
            get() = _isClosed.value

        override val closeReason: Throwable?
            get() = _closeReason.value

        override fun read(): Memory {
            TODO()
        }

        override suspend fun awaitBytes() {
        }

        override fun cancel(cause: Throwable) {
        }
    }

    public val destination: BytesDestination = object : BytesDestination() {
        override val isClosed: Boolean
            get() = _isClosed.value

        override val closeReason: Throwable?
            get() = _closeReason.value

        override fun write(memory: Memory, startPosition: Int, endPosition: Int): Int {
            if (buffer.value != null) return 0
            buffer.value = memory
            return endPosition
        }

        override suspend fun flush() {
            if (buffer.value == null) return
        }

        override fun close(cause: Throwable?) {
            this@ByteChannel.close(cause)
        }
    }
}
