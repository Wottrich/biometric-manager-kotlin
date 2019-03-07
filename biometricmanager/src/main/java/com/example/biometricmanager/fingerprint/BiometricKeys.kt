package com.example.biometricmanager.fingerprint

import com.example.biometricmanager.exceptions.EqualKeyException


open class BiometricKeys {

    var KEY_ALIAS_DEFAULT_BIOMETRIC_LIB = "FINGERPRINT_ALIAS_LOCK_LIB"
    var KEY_ALIAS_DEFAULT_ENCODE = "FINGERPRINT_ALIAS_ENCODE"

    companion object : BiometricKeys() {

        open fun setKeyFingerprint (key: String) {
            BiometricKeys.KEY_ALIAS_DEFAULT_BIOMETRIC_LIB = key
        }

        open fun setKeyEncode (key: String) {
            BiometricKeys.KEY_ALIAS_DEFAULT_ENCODE = key
        }

        open fun setKeys (biometricKey: String, encodeKey: String) {
            if (!biometricKey.isEmpty() && !encodeKey.isEmpty() && biometricKey != encodeKey) {
                BiometricKeys.KEY_ALIAS_DEFAULT_BIOMETRIC_LIB = biometricKey
                BiometricKeys.KEY_ALIAS_DEFAULT_ENCODE = encodeKey
            } else EqualKeyException()
        }
    }

}