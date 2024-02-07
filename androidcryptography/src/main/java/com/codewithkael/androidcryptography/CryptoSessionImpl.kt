package com.codewithkael.androidcryptography

import com.codewithkael.androidcryptography.CryptoSession.HashFunctions.*
import com.codewithkael.androidcryptography.aes.AESService
import com.codewithkael.androidcryptography.aes.AESServiceImpl
import com.codewithkael.androidcryptography.hash.HashService
import com.codewithkael.androidcryptography.hash.MD5Hash
import com.codewithkael.androidcryptography.hash.SHA256Hash
import com.codewithkael.androidcryptography.hash.SHA512Hash
import com.codewithkael.androidcryptography.rsa.RSAService
import com.codewithkael.androidcryptography.rsa.RSAServiceImpl

class CryptoSessionImpl : CryptoSession {

    override fun getAESService(): AESService {
        return AESServiceImpl()
    }

    override fun getRSAService(): RSAService {
        return RSAServiceImpl()
    }

    override fun getHashService(function: CryptoSession.HashFunctions): HashService {
        return when(function){
            SHA256 -> SHA256Hash()
            SHA512 -> SHA512Hash()
            MD5 -> MD5Hash()
        }
    }


}