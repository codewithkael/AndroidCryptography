package com.codewithkael.androidcryptography.aes

import com.codewithkael.androidcryptography.CryptoService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class AESServiceImpl : CryptoService {

    private var cipher: Cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
    private val keyGenerator: KeyGenerator = KeyGenerator.getInstance("AES")

    override fun generateKey(keyLength: Int): SecretKey {
        keyGenerator.init(keyLength)
        return keyGenerator.generateKey()
    }

    override suspend fun encryptText(text: String, key: SecretKey): Pair<ByteArray, ByteArray> =
        withContext(Dispatchers.Default) {
            cipher.init(Cipher.ENCRYPT_MODE, key)
            return@withContext cipher.doFinal(text.toByteArray()) to cipher.iv
        }

    override suspend fun decryptText(textByte: ByteArray, iv: ByteArray, key: SecretKey): String? =
        withContext(Dispatchers.Default) {
            cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(iv))
            return@withContext try {
                String(cipher.doFinal(textByte))
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
}
