package com.codewithkael.androidcryptography.rsa

import java.security.KeyPair

interface RSAService {
    suspend fun decryptText(encryptedText: String, keyPair: KeyPair): String?
    suspend fun encryptText(text: String,keyPair: KeyPair): String?
    fun convertKeyPairToBase64String(keyPair: KeyPair): Pair<String, String>
    fun convertBase64StringToKeyPair(publicKeyString: String, privateKeyString: String): KeyPair
    fun generateRSAKeyPair(keySize: Int): KeyPair
}