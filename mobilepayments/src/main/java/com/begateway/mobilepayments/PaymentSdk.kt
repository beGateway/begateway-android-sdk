package com.begateway.mobilepayments

import com.begateway.mobilepayments.model.PaymentSdkSettings
import com.begateway.mobilepayments.model.network.request.Checkout
import com.begateway.mobilepayments.model.network.request.GetPaymentTokenRequest
import com.begateway.mobilepayments.network.HttpResult
import com.begateway.mobilepayments.network.Rest

class PaymentSdk private constructor() {
    private lateinit var settings: PaymentSdkSettings
    private lateinit var rest: Rest

    internal fun applySettings(settings: PaymentSdkSettings) {
        this.settings = settings
        rest = Rest(settings.endpoint, settings.isDebugMode)
    }

    suspend fun getPaymentToken(publicKey: String, requestBody: GetPaymentTokenRequest): HttpResult<Any> {
        return rest.getPaymentToken(publicKey, requestBody)
    }


    companion object {
        internal var instance = PaymentSdk()
    }
}