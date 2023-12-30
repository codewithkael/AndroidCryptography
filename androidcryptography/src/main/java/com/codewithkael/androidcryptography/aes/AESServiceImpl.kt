package com.codewithkael.androidcryptography.aes

import com.codewithkael.androidcryptography.CryptoService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class AESServiceImpl : CryptoService {

    private val symmetricAlgorithm = "AES/CBC/PKCS5PADDING"
    private var cipher: Cipher = Cipher.getInstance(symmetricAlgorithm)
    private val keyGenerator: KeyGenerator = KeyGenerator.getInstance("AES")

    override fun generateKey(keyLength: Int): SecretKey {
        keyGenerator.init(keyLength)
        return keyGenerator.generateKey()
    }

    override fun convertKeyToString(secretKey: SecretKey): String {
        return Base64.getEncoder().encodeToString(secretKey.encoded)
    }

    override fun convertStringToKey(keyString: String): SecretKey {
        val decodedBytes = Base64.getDecoder().decode(keyString)
        return SecretKeySpec(decodedBytes, symmetricAlgorithm)
    }

    override suspend fun encryptText(text: String, key: SecretKey): Pair<String, String>? =
        withContext(Dispatchers.Default) {
            cipher.init(Cipher.ENCRYPT_MODE, key)
            val data = try {
                val encryptedBytes = cipher.doFinal(text.toByteArray())
                val encryptedBytesB64 = Base64.getEncoder().encode(encryptedBytes)
                String(encryptedBytesB64)
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext null
            }
            val iv = try {
                String(Base64.getEncoder().encode(cipher.iv))
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext null
            }
            return@withContext data to iv
        }

    override suspend fun decryptText(text: String, iv: String, key: SecretKey): String? =
        withContext(Dispatchers.Default) {
            val keyIv = Base64.getDecoder().decode(iv)
            cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(keyIv))
            return@withContext try {
                String(cipher.doFinal(Base64.getDecoder().decode(text)))
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
}
