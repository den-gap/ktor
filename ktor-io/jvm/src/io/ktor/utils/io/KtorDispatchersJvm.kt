/*
 * Copyright 2014-2021 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.utils.io

import io.ktor.utils.io.core.*
import kotlinx.coroutines.*
import org.slf4j.*
import java.lang.Runnable
import java.util.concurrent.*
import kotlin.coroutines.*

private val logger: Logger = LoggerFactory.getLogger("io.ktor.utils.io.KtorIoDispatcher")

internal val AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors()

public actual object KtorDispatchers {
    public actual val Default: CoroutineDispatcher = KtorIoDispatcher(
        "KtorDispatcher",
        minOf(64, AVAILABLE_PROCESSORS)
    )

    public actual fun createIoDispatcher(name: String, threadCount: Int): CoroutineDispatcher =
        KtorIoDispatcher(name, threadCount)
}

private class KtorIoDispatcher(
    val name: String,
    private val threadCount: Int
) : CoroutineDispatcher(), Closeable {
    private val dispatcherThreadGroup = ThreadGroup("io-pool-group-sub")

    private val tasks = ConcurrentLinkedQueue<Runnable>()

    init {
        require(threadCount > 0) { "nThreads should be positive but $threadCount specified" }

        logger.debug("Starting $name dispatcher with $threadCount threads")
    }

    private val threads = Array(threadCount) {
        IOThread(it + 1, tasks, dispatcherThreadGroup)
    }

    init {
        threads.forEach {
            it.start()
        }
    }

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        tasks.add(block)
    }

    /**
     * Gracefully shutdown dispatcher.
     */
    override fun close() {
    }
}

private class IOThread(
    private val number: Int,
    private val tasks: ConcurrentLinkedQueue<Runnable>,
    dispatcherThreadGroup: ThreadGroup
) : Thread(dispatcherThreadGroup, "io-thread-$number") {
    init {
        isDaemon = true
    }

    override fun run() {
        try {
            while (true) {
                val task: Runnable? = tasks.poll()
                if (task == null) {
                    onSpinWait()
                    continue
                }

                try {
                    task.run()
                } catch (cause: Throwable) {
                    onException(ExecutionException("Task failed", cause))
                }
            }
        } catch (cause: Throwable) {
            onException(ExecutionException("Thread pool worker failed", cause))
        }
    }

    private fun onException(t: Throwable) {
        currentThread().uncaughtExceptionHandler.uncaughtException(this, t)
    }
}
