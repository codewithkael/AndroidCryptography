package com.codewithkael.androidcryptoimpl

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.codewithkael.androidcryptography.CryptoSession
import com.codewithkael.androidcryptography.CryptoSessionImpl
import com.codewithkael.androidcryptoimpl.ui.theme.AndroidCryptoImplTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val cryptoSession: CryptoSession = CryptoSessionImpl()
            val aesService = cryptoSession.getAESService()

            AndroidCryptoImplTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Button(onClick = {
                        CoroutineScope(Dispatchers.Main).launch {
                            val key = aesService.generateKey(128)
                            val myText = "hello world yoohooo"
                            val encrypted = aesService.encryptText(myText, key)
                            Log.d(TAG, "onCreate: ${String(encrypted!!.first)}")
                            val decrypted =
                                aesService.decryptText(encrypted.first, encrypted.second, key)
                            Log.d(TAG, "onCreate: decrypted $decrypted")
                        }
                    }) {
                        Text(text = "click")
                    }
                }
            }
        }
    }
}

