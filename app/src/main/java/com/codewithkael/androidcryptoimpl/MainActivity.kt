package com.codewithkael.androidcryptoimpl

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.codewithkael.androidcryptography.CryptoSession
import com.codewithkael.androidcryptography.CryptoSessionImpl
import com.codewithkael.androidcryptography.utils.FileHelper
import com.codewithkael.androidcryptography.utils.osDownloadDirectory
import com.codewithkael.androidcryptoimpl.ui.theme.AndroidCryptoImplTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val cryptoSession: CryptoSession = CryptoSessionImpl()
    private val aesService = cryptoSession.getAESService()
    private val key = aesService.generateKey(128)

    private val rsaService = cryptoSession.getRSAService()
    val rsaKey = rsaService.generateRSAKeyPair(2048)

    private var filePickerLauncher: ActivityResultLauncher<String>? = null
    override fun onStart() {
        super.onStart()
        filePickerLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                if (uri != null) {
                    val fileName =
                        FileHelper.getFileName(contentResolver, uri).replace(" ", "")

                    // Split 'fileName' into parts (name, extension) and remove trailing empty strings.
                    val split: Array<String> =
                        fileName.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
                            .toTypedArray()

                    val fileToEncrypt =
                        FileHelper.createCacheFileFromUri(
                            applicationContext,
                            uri,
                            split[0],
                            "." + split[1]
                        )
                    val encryptedFilePath = osDownloadDirectory() + fileName + ".enc"
                    fileToEncrypt?.let {
                        CoroutineScope(Dispatchers.Main).launch {
                            val encryptedFile = aesService.encryptFile(it, encryptedFilePath, key)

                            val decryptedOutput = osDownloadDirectory() + "decrypted-" + fileName

                            encryptedFile?.let { it1 ->
                                aesService.decryptFile(
                                    it1,
                                    decryptedOutput,
                                    key
                                )
                            }
                        }
                    }
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {


            AndroidCryptoImplTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Button(onClick = {
//                        filePickerLauncher?.launch("*/*")

                        CoroutineScope(Dispatchers.Main).launch {
                            val textToEncrypt = "Hello world this is masoud"
                            val encryptedText = rsaService.encryptText(textToEncrypt,rsaKey)
                            Log.d("TAG", "onCreate: encrypted $encryptedText")

                            val converted = rsaService.convertKeyPairToBase64String(rsaKey)
                            val deConverted = rsaService.convertBase64StringToKeyPair(converted.first,converted.second)
                            val decryptedText =
                                encryptedText?.let { rsaService.decryptText(it,deConverted) }
                            Log.d("TAG", "onCreate: decryptedText $decryptedText")

                        }
                    }) {
                        Text(text = "click")
                    }
                }
            }
        }
    }
}

