package com.codewithkael.androidcryptography

import java.io.File
import javax.crypto.SecretKey

interface CryptoService {
    fun generateKey(keyLength: Int): SecretKey
    fun convertKeyToString(secretKey: SecretKey): String
    fun convertStringToKey(keyString: String): SecretKey
    suspend fun encryptText(text: String, key: SecretKey): String?
    suspend fun decryptText(encryptedText: String, key: SecretKey): String?
    suspend fun encryptFile(inputFile:File,outPutPath:String,key: SecretKey) :File?
}