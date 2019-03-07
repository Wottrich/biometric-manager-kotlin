package com.example.biometricmanager.fingerprint

import android.content.Context
import android.support.annotation.RequiresPermission
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat


    @RequiresPermission("android.permission.USE_FINGERPRINT")
    fun Context.isHardwareDetected () : Boolean{
        return FingerprintManagerCompat.from(this).isHardwareDetected
    }

    @RequiresPermission("android.permission.USE_FINGERPRINT")
    fun Context.hasEnrolledBiometrics () : Boolean{
        return FingerprintManagerCompat.from(this).hasEnrolledFingerprints()
    }

    @RequiresPermission("android.permission.USE_FINGERPRINT")
    fun Context.isBiometricAvailable () : Boolean{
        return this.isHardwareDetected() && this.hasEnrolledBiometrics()
    }

