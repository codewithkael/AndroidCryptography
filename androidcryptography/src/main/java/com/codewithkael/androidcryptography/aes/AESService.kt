package com.codewithkael.androidcryptography.aes

import java.io.File
import javax.crypto.SecretKey

interface AESService {
    fun generateKey(keyLength: Int): SecretKey
    fun convertKeyToString(secretKey: SecretKey): String
    fun convertStringToKey(keyString: String): SecretKey
    suspend fun encryptText(text: String, key: SecretKey): String?
    suspend fun decryptText(encryptedText: String, key: SecretKey): String?
    suspend fun encryptFile(inputFile:File,outPutPath:String,key: SecretKey) :File?
    suspend fun decryptFile(encryptedFile: File, outputPath: String, key: SecretKey): File?
}