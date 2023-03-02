package com.roland.android.shrine.utils

import com.roland.android.shrine.utils.CardNumbers.CARD_EXPIRED
import com.roland.android.shrine.utils.CardNumbers.DATE_FORMAT
import com.roland.android.shrine.utils.CardNumbers.INVALID_DATE
import com.roland.android.shrine.utils.CardNumbers.MASTERCARD_CODE
import com.roland.android.shrine.utils.CardNumbers.VALID_DATE
import com.roland.android.shrine.utils.CardNumbers.VERVE_CODE
import com.roland.android.shrine.utils.CardNumbers.VISA_CODE
import java.text.SimpleDateFormat
import java.util.*

object CardNumbers {
    const val MASTERCARD_CODE = "5"
    const val VERVE_CODE = "5061"
    const val VISA_CODE = "4"
    const val DATE_FORMAT = "yy, MM"
    const val EXPIRY_DATE = "expiry_date"
    const val SECURITY_CODE = "security_code"
    const val CARD_EXPIRED = "expired_card"
    const val VALID_DATE = "valid_date"
    const val INVALID_DATE = "invalid_date"
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
        expiryYear.toInt() == 0 -> CardCheck.IncorrectYear
        securityCode.length < 3 -> CardCheck.IncompleteCode
        cardExpired(expiryMonth, expiryYear) == INVALID_DATE -> CardCheck.InvalidDate
        cardExpired(expiryMonth, expiryYear) == CARD_EXPIRED -> CardCheck.CardExpired
        else -> CardCheck.GoodToGo
    }
}

fun cardExpired(expiryMonth: String, expiryYear: String): String {
    val date = "$expiryYear, $expiryMonth"
    val format = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
    val parsedDate = format.parse(date)!!
    val expiryDate = format.format(parsedDate)
    val currentDate = format.format(Calendar.getInstance().time)
    val threeYearsTime = currentDate.take(2).toInt().plus(3)
    return when {
        expiryDate.take(2).toInt() > threeYearsTime -> INVALID_DATE
        expiryDate > currentDate -> VALID_DATE
        else -> CARD_EXPIRED
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
    InvalidDate,
    CardExpired,
    GoodToGo
}

enum class AddressCheck {
    NoStreetSet,
    NoVicinitySet,
    AllSet
}