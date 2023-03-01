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

fun checkCardDetails(
    cardNumber: String,
    expiryMonth: String,
    expiryYear: String,
    securityCode: String,
): CardCheck {
    return when {
        cardNumber.length < 12 -> CardCheck.IncorrectCardNumber
        expiryMonth.isEmpty() -> CardCheck.IncorrectMonth
        expiryMonth.toInt() == 0 -> CardCheck.IncorrectMonth
        expiryMonth.toInt() > 12 -> CardCheck.IncorrectMonth
        expiryYear.length < 2 -> CardCheck.IncorrectYear
        expiryYear.toInt() < 23 -> CardCheck.IncorrectYear
        expiryYear.toInt() > 27 -> CardCheck.IncorrectYear
        securityCode.length < 3 -> CardCheck.IncompleteCode
        else -> CardCheck.GoodToGo
    }
}

fun checkAddress(
    street: String,
    vicinity: String
): AddressCheck {
    return when {
        street.isEmpty() -> AddressCheck.NoStreetSet
        vicinity.isEmpty() -> AddressCheck.NoVicinitySet
        else -> AddressCheck.AllSet
    }
}

enum class CardTypes {
    MASTERCARD,
    VERVE,
    VISA
}

enum class CardCheck {
    IncorrectCardNumber,
    IncorrectMonth,
    IncorrectYear,
    IncompleteCode,
    GoodToGo
}

enum class AddressCheck {
    NoStreetSet,
    NoVicinitySet,
    AllSet
}