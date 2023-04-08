package com.roland.android.shrine.data

data class User(
	val firstName: String = "",
	val lastName: String = "",
	val login_pin: String = ""
)

data class Address(
	val street: String = "",
	val vicinity: String = ""
)

data class CardDetails(
	val cardNumber: String = "",
	val expiryMonth: String = "",
	val expiryYear: String = "",
	val securityCode: String = ""
)