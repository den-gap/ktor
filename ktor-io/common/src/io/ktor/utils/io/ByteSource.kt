/*
 * Copyright 2014-2021 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.utils.io

import io.ktor.utils.io.bits.*

public class Bytes

public abstract class BytesSource {

    public abstract val closeReason: Throwable?

    protected abstract fun read(): Memory

    public abstract suspend fun awaitBytes()

    public abstract fun cancel(cause: Throwable = CancellationException("Source cancelled"))
}


