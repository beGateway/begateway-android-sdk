package com.begateway.mobilepayments.parser

import com.begateway.mobilepayments.PaymentSdk
import com.begateway.mobilepayments.model.network.response.BepaidResponse
import com.begateway.mobilepayments.model.network.response.ResponseStatus
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.lang.reflect.Type


class BepaidResponseParser : JsonDeserializer<BepaidResponse> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): BepaidResponse = parseJson(json)

    fun parseJson(json: JsonElement?): BepaidResponse {
        return if (json != null && !json.isJsonNull) {
            var status: String? = null
            var message: String? = null
            var url: String? = null
            var paymentToken: String? = null
            json.asJsonObject?.let { body ->
                val jsonObject = when {
                    body.has("response") -> {
                        val response = body.getAsJsonObject("response").asJsonObject
                        if (PaymentSdk.instance.isSaveCard) {
                            val creditCard = response.getAsJsonObject("credit_card")
                            if (creditCard != null && !creditCard.isJsonNull) {
                                PaymentSdk.instance.cardToken = getString("token", response)
                            }
                        }
                        response
                    }
                    body.has("checkout") -> {
                        body.getAsJsonObject("checkout").asJsonObject
                    }
                    else -> {
                        body
                    }
                }
                status = getString("status", jsonObject)
                message = getString("message", jsonObject)
                url = getString("url", jsonObject)
                paymentToken = getString("token", jsonObject)
            }

            BepaidResponse(
                ResponseStatus.getStatus(status),
                message,
                paymentToken,
                url,
            )
        } else {
            BepaidResponse()
        }
    }

    private fun getString(key: String, json: JsonObject): String? {
        val value = json.get(key)
        return if (value == null || value.isJsonNull) {
            null
        } else {
            value.asString
        }
    }
}