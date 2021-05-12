package com.begateway.mobilepayments.network

import com.begateway.mobilepayments.models.network.gateway.GatewayPaymentRequest
import com.begateway.mobilepayments.models.network.request.PaymentRequest
import com.begateway.mobilepayments.models.network.request.TokenCheckoutData
import com.begateway.mobilepayments.models.network.response.BeGatewayResponse
import com.begateway.mobilepayments.models.network.response.CheckoutWithTokenData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


internal interface Api {
    @POST("checkouts")
    suspend fun getPaymentToken(
        @Body requestBody: TokenCheckoutData
    ): Response<CheckoutWithTokenData>

    @POST("payments")
    suspend fun payWithCard(
        @Body requestBody: PaymentRequest
    ): Response<BeGatewayResponse>

    @POST("payments")
    suspend fun payWithCardGateway(
        @Body requestBody: GatewayPaymentRequest
    ): Response<BeGatewayResponse>

    @GET("checkouts/{token}")
    suspend fun getPaymentStatus(
        @Path("token") token: String
    ): Response<BeGatewayResponse>
}