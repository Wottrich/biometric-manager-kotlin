package com.example.biometricmanager.fingerprint

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.support.annotation.RequiresApi
import java.lang.Exception
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

@RequiresApi(api = Build.VERSION_CODES.M)
internal open class BiometricSetup {

    internal lateinit var cipher: Cipher
    internal lateinit var keyStore: KeyStore


    @RequiresApi(api = Build.VERSION_CODES.M)
    open fun generateKey() : Boolean {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore")
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
            keyStore.load(null)
            keyGenerator.init(
                KeyGenParameterSpec.Builder(
                    BiometricKeys.KEY_ALIAS_DEFAULT_BIOMETRIC_LIB,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7).build()
            )
            keyGenerator.generateKey()
            return true
        } catch (e : Exception) {
            e.printStackTrace()
            return false
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    open fun initCipher () : Boolean {
        return try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES
                    + "/" + KeyProperties.BLOCK_MODE_CBC
                    + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7)

            keyStore.load(null)
            val key = keyStore.getKey(BiometricKeys.KEY_ALIAS_DEFAULT_BIOMETRIC_LIB, null) as SecretKey
            cipher.init(Cipher.ENCRYPT_MODE, key)
            true
        } catch (e : Exception) {
            e.printStackTrace()
            false
        }
    }
}