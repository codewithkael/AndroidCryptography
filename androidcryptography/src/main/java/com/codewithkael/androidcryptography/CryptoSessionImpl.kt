package com.codewithkael.androidcryptography

import com.codewithkael.androidcryptography.aes.AESService
import com.codewithkael.androidcryptography.aes.AESServiceImpl
import com.codewithkael.androidcryptography.rsa.RSAService
import com.codewithkael.androidcryptography.rsa.RSAServiceImpl

class CryptoSessionImpl : CryptoSession {

    override fun getAESService(): AESService {
        return AESServiceImpl()
    }

    override fun getRSAService(): RSAService {
        return RSAServiceImpl()
    }
}