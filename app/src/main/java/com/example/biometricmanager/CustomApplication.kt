package com.example.biometricmanager

import android.app.Application
import com.example.biometricmanager.fingerprint.BiometricKeys

class CustomApplication : Application() {


    override fun onCreate() {
        super.onCreate()

        BiometricKeys.setKeyFingerprint("")
    }

}