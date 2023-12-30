package com.codewithkael.androidcryptography

import com.codewithkael.androidcryptography.aes.AESServiceImpl

class CryptoSessionImpl : CryptoSession {

    override fun getAESService(): CryptoService {
        return AESServiceImpl()
    }
}