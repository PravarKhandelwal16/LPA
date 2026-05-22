package com.example.lpa.network.api

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Retrofit interface for communicating with an SM-DP+ (Subscription Manager
 * Data Preparation +) server for eSIM profile provisioning.
 *
 * **Note:** Placeholder for future implementation.
 * Actual SM-DP+ APIs (e.g. ES9+ interface) involve specific JSON structures
 * for endpoints like /gsma/rsp2/es9plus/initiateAuthentication.
 */
interface SmdpApi {

    /**
     * Placeholder endpoint for initiating an eSIM download session.
     */
    @POST("gsma/rsp2/es9plus/initiateAuthentication")
    suspend fun initiateAuthentication(
        @Body requestBody: Any // Placeholder: Will be replaced by a concrete Request DTO
    ): Any // Placeholder: Will be replaced by a concrete Response DTO

    /**
     * Placeholder endpoint for a generic SM-DP+ operation.
     */
    @POST("gsma/rsp2/es9plus/{operation}")
    suspend fun performOperation(
        @Path("operation") operation: String,
        @Body requestBody: Any
    ): Any
}
