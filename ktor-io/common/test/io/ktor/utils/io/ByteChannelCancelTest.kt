package io.ktor.utils.io

import io.ktor.test.dispatcher.*
import io.ktor.utils.io.errors.*
import kotlinx.coroutines.*
import kotlin.test.*

class ByteChannelCancelTest {

    @Test
    fun testCopyAndCloseSourceCancel() = testSuspend {
        val source = ByteChannel()
        val destination = ByteChannel()
        val origin = IOException("FOO")

        launch(Dispatchers.Unconfined) {
            try {
                source.copyAndClose(destination)
            } catch (cause: Throwable) {
                assertEquals(origin, cause.cause)
            }
        }

        source.close(origin)
        assertEquals(origin, destination.closedCause?.cause?.cause)
    }

    @Test
    fun testCopyAndCloseDestinationCancel() = testSuspend {
        val source = ByteChannel()
        val destination = ByteChannel()
        val origin = IOException("FOO")

        launch(Dispatchers.Unconfined) {
            try {
                source.copyAndClose(destination)
            } catch (cause: Throwable) {
                assertEquals(origin, cause.cause?.cause?.cause)
            }
        }

        destination.cancel(origin)
        assertEquals(origin, source.closedCause?.cause?.cause)
    }
}
