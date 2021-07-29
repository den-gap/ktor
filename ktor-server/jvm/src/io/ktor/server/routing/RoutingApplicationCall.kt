/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.server.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.*
import kotlinx.coroutines.*

/**
 * Represents an application call being handled by [Routing]
 * @property route is the selected route
 */
public class RoutingApplicationCall(
    private val call: ApplicationCall,
    public val route: Route,
    private val job: Job,
    receivePipeline: ApplicationReceivePipeline,
    responsePipeline: ApplicationSendPipeline,
    parameters: Parameters
) : ApplicationCall {

    override val application: Application get() = call.application
    override val attributes: Attributes get() = call.attributes

    override val request: RoutingApplicationRequest = RoutingApplicationRequest(this, receivePipeline, call.request)

    override val response: RoutingApplicationResponse =
        RoutingApplicationResponse(this, responsePipeline, call.response)

    override val parameters: Parameters by lazy(LazyThreadSafetyMode.NONE) {
        Parameters.build {
            appendAll(call.parameters)
            appendMissing(parameters)
        }
    }

    override fun onCallFinish(handler: (Throwable?) -> Unit) {
        job.invokeOnCompletion(handler)
    }

    override fun toString(): String = "RoutingApplicationCall(route=$route)"
}

/**
 * Represents an application request being handled by [Routing]
 */
public class RoutingApplicationRequest(
    override val call: RoutingApplicationCall,
    override val pipeline: ApplicationReceivePipeline,
    request: ApplicationRequest
) : ApplicationRequest by request

/**
 * Represents an application response being handled by [Routing]
 */
public class RoutingApplicationResponse(
    override val call: RoutingApplicationCall,
    override val pipeline: ApplicationSendPipeline,
    response: ApplicationResponse
) : ApplicationResponse by response
