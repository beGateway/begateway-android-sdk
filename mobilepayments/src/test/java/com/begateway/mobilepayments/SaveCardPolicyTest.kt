package com.begateway.mobilepayments

import com.begateway.mobilepayments.models.network.request.SaveCardPolicy
import com.begateway.mobilepayments.models.network.request.Settings
import com.begateway.mobilepayments.models.network.response.PaymentData
import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Test

class SaveCardPolicyTest {
    private val gson = Gson()

    @Test
    fun checkoutResponseShowsToggleWithoutCustomerContract() {
        val data = gson.fromJson(
            """{"checkout":{"token":"test","settings":{"save_card_toggle":{"display":true,"customer_contract":false}}}}""",
            PaymentData::class.java
        )
        val policy = data.checkout.settings?.saveCardPolicy
        assertEquals(false, policy?.customerContract)
        assertTrue(policy?.isDisplayEnabled == true)
    }

    @Test
    fun displayControlsVisibilityIndependentlyOfCustomerContract() {
        for (display in listOf(true, false)) {
            for (contract in listOf("true", "false", "null")) {
                val policy = gson.fromJson(
                    """{"display":$display,"customer_contract":$contract}""",
                    SaveCardPolicy::class.java
                )
                assertEquals(display, policy.display)
                assertEquals(display, policy.isDisplayEnabled)
                assertEquals(contract.toBooleanStrictOrNull(), policy.customerContract)
            }
        }
    }

    @Test
    fun missingOrNullDisplayDefaultsToVisible() {
        for (json in listOf("{}", """{"customer_contract":false}""",
            """{"customer_contract":true}""", """{"display":null}""")) {
            val policy = gson.fromJson(json, SaveCardPolicy::class.java)
            assertNull(policy.display)
            assertTrue(policy.isDisplayEnabled)
        }
    }

    @Test
    fun absentPolicyRemainsHidden() {
        for (json in listOf("{}", """{"save_card_toggle":null}""")) {
            val settings = gson.fromJson(json, Settings::class.java)
            assertFalse(settings.saveCardPolicy?.isDisplayEnabled == true)
        }
    }

    @Test
    fun existingPositionalConstructorStillSetsCustomerContract() {
        val policy = SaveCardPolicy(false)
        assertEquals(false, policy.customerContract)
        assertTrue(policy.isDisplayEnabled)
    }
}
