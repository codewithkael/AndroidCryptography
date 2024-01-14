package com.codewithkael.androidcryptography.rsa


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.Security
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64
import javax.crypto.Cipher

class RSAServiceImpl : RSAService {

    private val asymmetricAlgorithm = "RSA/ECB/PKCS1Padding"

    private var rsaCipher: Cipher = Cipher.getInstance(asymmetricAlgorithm)
    private val rsaKeyPairGenerator: KeyPairGenerator = KeyPairGenerator.getInstance("RSA")

    override suspend fun encryptText(text: String, keyPair: KeyPair): String? =
        withContext(Dispatchers.Default) {
            Security.addProvider(BouncyCastleProvider())
            rsaCipher.init(Cipher.ENCRYPT_MODE, keyPair.public)
            val encryptedBytes = try {
                rsaCipher.doFinal(text.toByteArray())
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext null
            }
            return@withContext Base64.getEncoder().encodeToString(encryptedBytes)
        }

    override suspend fun decryptText(encryptedText: String, keyPair: KeyPair): String? =
        withContext(Dispatchers.Default) {
            Security.addProvider(BouncyCastleProvider())
            rsaCipher.init(Cipher.DECRYPT_MODE, keyPair.private)
            val decodedData = Base64.getDecoder().decode(encryptedText)
            try {
                val decryptedBytes = rsaCipher.doFinal(decodedData)
                String(decryptedBytes)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

    override fun generateRSAKeyPair(keySize: Int): KeyPair {
        rsaKeyPairGenerator.initialize(keySize)
        return rsaKeyPairGenerator.generateKeyPair()
    }

    override fun convertKeyPairToBase64String(keyPair: KeyPair): Pair<String, String> {
        val publicKeyString = Base64.getEncoder().encodeToString(keyPair.public.encoded)
        val privateKeyString = Base64.getEncoder().encodeToString(keyPair.private.encoded)
        return Pair(publicKeyString, privateKeyString)
    }

    override fun convertBase64StringToKeyPair(
        publicKeyString: String,
        privateKeyString: String
    ): KeyPair {
        val publicKeyBytes = Base64.getDecoder().decode(publicKeyString)
        val privateKeyBytes = Base64.getDecoder().decode(privateKeyString)

        val keyFactory = KeyFactory.getInstance("RSA")

        val publicKeySpec = X509EncodedKeySpec(publicKeyBytes)
        val privateKeySpec = PKCS8EncodedKeySpec(privateKeyBytes)

        val publicKey = keyFactory.generatePublic(publicKeySpec)
        val privateKey = keyFactory.generatePrivate(privateKeySpec)

        return KeyPair(publicKey, privateKey)
    }
}
