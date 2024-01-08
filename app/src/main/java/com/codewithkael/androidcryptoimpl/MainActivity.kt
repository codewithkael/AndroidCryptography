package com.codewithkael.androidcryptoimpl

import android.os.Bundle
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
                        filePickerLauncher?.launch("*/*")
//                        CoroutineScope(Dispatchers.Main).launch {
//                            val a = aesService.convertKeyToString(key)
//                            val b = aesService.convertStringToKey(a)
//                            val myText = "hello world yoohooo"
//                            val encrypted = aesService.encryptText(myText, b)
//                            Log.d(tag, "onCreate: ${encrypted!!}")
//                            val decrypted =
//                                aesService.decryptText(encrypted, b)
//                            Log.d(tag, "onCreate: decrypted $decrypted")
//                        }
                    }) {
                        Text(text = "click")
                    }
                }
            }
        }
    }
}

