package com.roland.android.shrine.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val STREET_ADDRESS = stringPreferencesKey("street_address")
private val VICINITY = stringPreferencesKey("vicinity")
private val CARD_NUMBER = stringPreferencesKey("card_number")
private val EXPIRY_MONTH = stringPreferencesKey("expiry_month")
private val EXPIRY_YEAR = stringPreferencesKey("expiry_year")
private val SECURITY_CODE = stringPreferencesKey("security_code")

class AppDataStore(private val dataStore: DataStore<Preferences>) {
    suspend fun saveDeliveryAddress(address: Address) {
        dataStore.edit { preferences ->
            preferences[STREET_ADDRESS] = address.street
            preferences[VICINITY] = address.vicinity
        }
    }

    fun getAddress(): Flow<Address> {
        return dataStore.data.map { preferences ->
            Address(
                street = preferences[STREET_ADDRESS] ?: "",
                vicinity = preferences[VICINITY] ?: ""
            )
        }
    }

    suspend fun saveCardDetails(cardDetails: CardDetails) {
        dataStore.edit { preferences ->
            preferences[CARD_NUMBER] = cardDetails.cardNumber
            preferences[EXPIRY_MONTH] = cardDetails.expiryMonth
            preferences[EXPIRY_YEAR] = cardDetails.expiryYear
            preferences[SECURITY_CODE] = cardDetails.securityCode
        }
    }

    fun getCardDetails(): Flow<CardDetails> {
        return dataStore.data.map { preferences ->
            CardDetails(
                cardNumber = preferences[CARD_NUMBER] ?: "",
                expiryMonth = preferences[EXPIRY_MONTH] ?: "",
                expiryYear = preferences[EXPIRY_YEAR] ?: "",
                securityCode = preferences[SECURITY_CODE] ?: ""
            )
        }
    }
}

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