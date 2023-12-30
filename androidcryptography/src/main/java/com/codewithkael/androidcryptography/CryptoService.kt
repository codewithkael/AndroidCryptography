package com.codewithkael.androidcryptography

import javax.crypto.SecretKey

interface CryptoService {
    fun generateKey(keyLength: Int): SecretKey
    fun convertKeyToString(secretKey: SecretKey): String
    fun convertStringToKey(keyString: String): SecretKey
    suspend fun encryptText(text: String, key: SecretKey): Pair<String, String>?
    suspend fun decryptText(text: String, iv: String, key: SecretKey): String?
}