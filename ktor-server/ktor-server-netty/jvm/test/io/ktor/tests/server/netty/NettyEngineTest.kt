/*
* Copyright 2014-2021 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
*/

package io.ktor.tests.server.netty

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.server.netty.*
import io.ktor.server.testing.*
import io.ktor.server.testing.suites.*
import org.junit.*
import org.junit.Test
import kotlin.test.*

class NettyCompressionTest : CompressionTestSuite<NettyApplicationEngine, NettyApplicationEngine.Configuration>(Netty) {
    init {
        enableSsl = true
    }

    override fun configure(configuration: NettyApplicationEngine.Configuration) {
        configuration.shareWorkGroup = true
    }
}

class NettyContentTest : ContentTestSuite<NettyApplicationEngine, NettyApplicationEngine.Configuration>(Netty) {
    init {
        enableSsl = true
    }

    override fun configure(configuration: NettyApplicationEngine.Configuration) {
        configuration.shareWorkGroup = true
    }
}

class NettyHttpServerTest : HttpServerTestSuite<NettyApplicationEngine, NettyApplicationEngine.Configuration>(Netty) {
    init {
        enableSsl = true
    }

    override fun configure(configuration: NettyApplicationEngine.Configuration) {
        configuration.shareWorkGroup = true
        configuration.tcpKeepAlive = true
    }
}

class NettyHttp2ServerTest : HttpServerTestSuite<NettyApplicationEngine, NettyApplicationEngine.Configuration>(Netty) {
    init {
        enableSsl = true
        enableHttp2 = true
    }

    override fun configure(configuration: NettyApplicationEngine.Configuration) {
        configuration.shareWorkGroup = true
    }
}

class NettySustainabilityTest : SustainabilityTestSuite<NettyApplicationEngine, NettyApplicationEngine.Configuration>(
    Netty
) {
    init {
        enableSsl = true
    }

    override fun configure(configuration: NettyApplicationEngine.Configuration) {
        configuration.shareWorkGroup = true
    }
}

class NettyConfigTest : ConfigTestSuite(Netty)

class QueryParameterTest : EngineTestBase<NettyApplicationEngine, NettyApplicationEngine.Configuration>(Netty) {
    @Test
    fun queryParameterContainingSemicolon() {
        createAndStartServer {
            handle {
                assertEquals("01;21", call.request.queryParameters["code"])
                call.respond(HttpStatusCode.OK)
            }
        }

        withUrl("/", {
            url {
                parameters.urlEncodingOption = UrlEncodingOption.NO_ENCODING
                parameters.append("code", "01;21")
            }
        }) {
            Assert.assertEquals(200, status.value)
        }
    }
}
