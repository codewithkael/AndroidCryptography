package com.codewithkael.androidcryptography

import com.codewithkael.androidcryptography.aes.AESService
import com.codewithkael.androidcryptography.rsa.RSAService

interface CryptoSession {

    fun getAESService(): AESService
    fun getRSAService(): RSAService
}