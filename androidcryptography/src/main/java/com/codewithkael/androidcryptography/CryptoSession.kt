package com.codewithkael.androidcryptography

import com.codewithkael.androidcryptography.aes.AESService
import com.codewithkael.androidcryptography.hash.HashService
import com.codewithkael.androidcryptography.rsa.RSAService

interface CryptoSession {
    enum class HashFunctions{
        SHA256,SHA512,MD5
    }

    fun getAESService(): AESService
    fun getRSAService(): RSAService
    fun getHashService(function: HashFunctions): HashService
}