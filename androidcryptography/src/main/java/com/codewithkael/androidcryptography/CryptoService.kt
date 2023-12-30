package com.codewithkael.androidcryptography

import javax.crypto.SecretKey

interface CryptoService {
    fun generateKey(keyLength:Int):SecretKey
    suspend fun encryptText(text:String,key:SecretKey):Pair<ByteArray,ByteArray>?
    suspend fun decryptText(textByte: ByteArray,iv:ByteArray,key: SecretKey): String?
}