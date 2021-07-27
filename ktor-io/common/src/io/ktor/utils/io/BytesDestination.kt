/*
 * Copyright 2014-2021 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.utils.io

import io.ktor.utils.io.bits.*

public abstract class BytesDestination {
    public abstract val isClosed: Boolean

    public abstract val closeReason: Throwable?

    protected abstract fun write(memory: Memory, startPosition: Int, endPosition: Int): Int

    public abstract suspend fun flush()

    public abstract fun close(cause: Throwable? = null)
}
