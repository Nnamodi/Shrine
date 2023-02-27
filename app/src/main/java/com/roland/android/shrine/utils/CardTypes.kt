package com.roland.android.shrine.utils

import com.roland.android.shrine.utils.CardNumbers.MASTERCARD_CODE
import com.roland.android.shrine.utils.CardNumbers.VERVE_CODE
import com.roland.android.shrine.utils.CardNumbers.VISA_CODE

object CardNumbers {
    const val MASTERCARD_CODE = "5"
    const val VERVE_CODE = "5061"
    const val VISA_CODE = "4"
}

fun cardType(cardNumber: String): String {
    return when {
        cardNumber.startsWith(VERVE_CODE) -> CardTypes.VERVE
        cardNumber.startsWith(MASTERCARD_CODE) -> CardTypes.MASTERCARD
        cardNumber.startsWith(VISA_CODE) -> CardTypes.VISA
        else -> ""
    }.toString()
}

enum class CardTypes {
    MASTERCARD,
    VERVE,
    VISA
}