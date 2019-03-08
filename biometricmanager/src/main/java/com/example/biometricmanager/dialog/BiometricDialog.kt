package com.example.biometricmanager.dialog

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.support.annotation.LayoutRes
import android.support.annotation.RequiresApi
import android.support.annotation.RequiresPermission
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.biometricmanager.exceptions.CancelButtonException
import com.example.biometricmanager.exceptions.RequiresPermissionException
import com.example.biometricmanager.fingerprint.BiometricHelper
import com.example.biometricmanager.fingerprint.BiometricSetup
import com.example.biometricmanager.fingerprint.isBiometricAvailable
import com.example.biometricmanager.generics.BiometricBaseDialog
import java.lang.Exception


@SuppressLint("ValidFragment")
@RequiresApi(Build.VERSION_CODES.M)
class BiometricDialog(activity: Activity, layout: Int) : BiometricBaseDialog(), View.OnClickListener {

    private var btnCancel: Button? = null
    private var tvCancel: TextView? = null

    private var listenerCanceled: View.OnClickListener? = null

    private var onSuccess: ((result: FingerprintManagerCompat.AuthenticationResult?) -> Unit)? = null
    private var onError: ((messageError: String, dismiss: Boolean) -> Unit)? = null
    private var onFailed: (() -> Unit)? = null
    private var onInformation: ((message: String) -> Unit)? = null

    private lateinit var setup: BiometricSetup
    private lateinit var manager: FingerprintManagerCompat
    private lateinit var helper: BiometricHelper

    constructor(activity: Activity, @LayoutRes layout: Int, btnCancel: Button) : this (activity, layout) {
        this.activity = activity
        this.layout = layout
        this.btnCancel = btnCancel
        this.activity.createBiometricView()
    }

    constructor(activity: Activity, @LayoutRes layout: Int, tvCancel: TextView) : this (activity, layout) {
        this.activity = activity
        this.layout = layout
        this.tvCancel = tvCancel
        this.activity.createBiometricView()
    }


    @RequiresPermission("android.permission.USE_FINGERPRINT")
    override fun initAuth() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (btnCancel == null && tvCancel == null)
                throw CancelButtonException()
            else {
                if (btnCancel != null)
                    btnCancel!!.setOnClickListener(this)


                if (tvCancel != null)
                    tvCancel!!.setOnClickListener(this)
            }
            if (activity.checkSelfPermission(Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                throw RequiresPermissionException("Required android.permission.USE_FINGERPRINT")
            } else {
                manager = FingerprintManagerCompat.from(this.activity)
                setup = BiometricSetup()
                initBiometric()
            }
        }
    }

    @RequiresPermission("android.permission.USE_FINGERPRINT")
    @RequiresApi(Build.VERSION_CODES.M)
    private fun initBiometric() {
        try {
            if (activity.isBiometricAvailable()) {
                setupFingerprint()
            }
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }

    @RequiresPermission("android.permission.USE_FINGERPRINT")
    @RequiresApi(Build.VERSION_CODES.M)
    private fun setupFingerprint() {
        val keyguardManager = activity.getSystemService(Context.KEYGUARD_SERVICE) as? KeyguardManager
        if (keyguardManager != null && keyguardManager.isKeyguardSecure){
            if (setup.generateKey() && setup.initCipher()) {
                val cryptoObject = FingerprintManagerCompat.CryptoObject(setup.cipher)

                helper = BiometricHelper()
                helper.attachCallback(this::onSuccess, this::onError, this::onFailed, this::onInformation)
                helper.startAuth(manager, cryptoObject)
            }
        } else onError?.invoke("Error to generator fingerprint keyguard manager.", true)

    }

    override fun onDismiss(dialog: DialogInterface?) {
        helper.onDismiss()
        super.onDismiss(dialog)
    }

    fun setOnSuccess(onSuccess: (FingerprintManagerCompat.AuthenticationResult?) -> Unit) {
        this.onSuccess = onSuccess
    }

    fun setOnError(onError: (messageError: String, dismiss: Boolean) -> Unit) {
        this.onError = onError
    }

    fun setOnFailed(onFailed: () -> Unit) {
        this.onFailed = onFailed
    }

    fun setOnInformation(onInformation: (message: String) -> Unit) {
        this.onInformation = onInformation
    }

    //<editor-folder defaultstate="Collapsed" desc="Callback`s and Listeners">
    override fun onClick(v: View?) {
        dismiss()
        listenerCanceled?.onClick(v)
    }

    private fun onSuccess(result: FingerprintManagerCompat.AuthenticationResult?) {
        this.onSuccess?.invoke(result)
        this.dismiss()
    }

    private fun onError (messageError: String, dismiss: Boolean) {
        this.onError?.invoke(messageError, dismiss)

        if (dismiss)
            this.dismiss()
    }

    private fun onFailed () {
        this.onFailed?.invoke()
    }

    private fun onInformation (message: String) {
        this.onInformation?.invoke(message)
    }
    //</editor-folder>

}