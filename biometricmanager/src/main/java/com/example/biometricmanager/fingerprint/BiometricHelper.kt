package com.example.biometricmanager.fingerprint

import android.os.Build
import android.support.annotation.RequiresApi
import android.support.annotation.RequiresPermission
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat
import android.support.v4.os.CancellationSignal

@RequiresApi(Build.VERSION_CODES.M)
internal class BiometricHelper : FingerprintManagerCompat.AuthenticationCallback() {

    private lateinit var cancellationSignal: CancellationSignal

    private var onSuccess: ((result: FingerprintManagerCompat.AuthenticationResult?) -> Unit)? = null
    private var onError: ((messageError: String, dismiss: Boolean) -> Unit)? = null
    private var onFailed: (() -> Unit)? = null
    private var onInformation: ((message: String) -> Unit)? = null

    @RequiresPermission("android.permission.USE_FINGERPRINT")
    fun attachCallback(
            onSuccess: (result: FingerprintManagerCompat.AuthenticationResult?) -> Unit,
            onError: (messageError: String, dismiss: Boolean) -> Unit,
            onFailed: () -> Unit,
            onInformation: (message: String) -> Unit
    ) {
        this.onSuccess = onSuccess
        this.onError = onError
        this.onInformation = onInformation
        this.onFailed = onFailed
    }

    @RequiresPermission("android.permission.USE_FINGERPRINT")
    fun startAuth(managerCompat: FingerprintManagerCompat, cryptoObject: FingerprintManagerCompat.CryptoObject) {
        cancellationSignal = CancellationSignal()
        managerCompat.authenticate(cryptoObject, 0, cancellationSignal, this, null);
    }

    override fun onAuthenticationSucceeded(result: FingerprintManagerCompat.AuthenticationResult?) {
        this.onSuccess?.invoke(result)
        super.onAuthenticationSucceeded(result)
    }

    override fun onAuthenticationFailed() {
        this.onFailed?.invoke()
        super.onAuthenticationFailed()
    }

    override fun onAuthenticationError(errMsgId: Int, errString: CharSequence?) {
        this.onError?.invoke(errString.toString(), errMsgId == 7 || errMsgId == 5)
        super.onAuthenticationError(errMsgId, errString)
    }

    override fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence?) {
        this.onInformation?.invoke(helpString.toString())
        super.onAuthenticationHelp(helpMsgId, helpString)
    }

    open fun onDismiss() {
        cancellationSignal.cancel()
    }

}