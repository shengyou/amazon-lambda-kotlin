package io.kraftsman.functions

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import io.kraftsman.services.ContactService
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.IOException
import java.net.HttpURLConnection
import kotlin.jvm.Throws
import kotlin.time.ExperimentalTime

class ContactLambda : RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @ExperimentalTime
    @Throws(IOException::class)
    override fun handleRequest(request: APIGatewayProxyRequestEvent, context: Context): APIGatewayProxyResponseEvent {

        val logger = context.logger
        logger.log("Lambda log")

        val param = request.queryStringParameters["amount"]
        val amount = param?.toIntOrNull() ?: 10

        val contacts = ContactService().generate(amount)
        val jsonString = Json.encodeToString(mapOf("data" to contacts))

        return APIGatewayProxyResponseEvent()
            .withHeaders(
                mapOf("Content-Type" to "application/json")
            )
            .withStatusCode(HttpURLConnection.HTTP_OK)
            .withBody(jsonString)
    }
}
