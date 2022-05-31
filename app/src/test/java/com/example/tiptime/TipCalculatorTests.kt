package com.example.tiptime

import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.NumberFormat

// Local Test -> On the computer
class TipCalculatorTests {
    @Test
    fun calculate_20_percentage_tip_no_roundup() {
        val amount = 10.00
        val tipPercent = 20.00
//        val expectedTip = "$2.00"
//        val expectedTip = "2,00 €"
        val expectedTip = NumberFormat.getCurrencyInstance().format(2.00)
        val actualTip = calculateTip(amount = amount, tipPercent = tipPercent, false)
        assertEquals(expectedTip, actualTip)
    }
}