package com.example.biometricmanager.dialog

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.annotation.LayoutRes
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.biometricmanager.exceptions.CancelButtonException
import com.example.biometricmanager.generics.BiometricBaseDialog


@SuppressLint("ValidFragment")
class BiometricDialog() : BiometricBaseDialog(), View.OnClickListener {

    private var btnCancel: Button? = null
    private var tvCancel: TextView? = null

    private var listenerCanceled: View.OnClickListener? = null

    private lateinit var onSuccess: (FingerprintManagerCompat.AuthenticationResult) -> Unit
    private lateinit var onError: (messageError: String, dismiss: Boolean) -> Unit
    private lateinit var onInformation: (informationMessage: String) -> Unit
    private lateinit var onFailure: () -> Unit
    private lateinit var onCanceled: () -> Unit

    constructor(activity: Activity, @LayoutRes layout: Int) : this () {
        this.activity = activity
        this.layout = layout
        this.activity.createBiometricView()
    }

    constructor(activity: Activity, @LayoutRes layout: Int, btnCancel: Button) : this () {
        this.activity = activity
        this.layout = layout
        this.btnCancel = btnCancel
        this.activity.createBiometricView()
    }

    constructor(activity: Activity, @LayoutRes layout: Int, tvCancel: TextView) : this () {
        this.activity = activity
        this.layout = layout
        this.tvCancel = tvCancel
        this.activity.createBiometricView()
    }


    override fun initAuth() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (btnCancel == null || tvCancel == null)
                CancelButtonException()
            else {
                if (btnCancel != null)
                    btnCancel!!.setOnClickListener(this)

                if (tvCancel != null)
                    tvCancel!!.setOnClickListener(this)
            }
            if (activity.checkSelfPermission(Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED) {
                if (! activity.shouldShowRequestPermissionRationale(Manifest.permission.USE_BIOMETRIC)) {
                    activity.requestPermissions(arrayOf(Manifest.permission.USE_BIOMETRIC), 1)
                }
            } else {

            }
        }
    }

    //<editor-folder defaultstate="Collapsed" desc="Callback`s and Listeners">
    override fun onClick(v: View?) {
        dismiss()
        listenerCanceled?.onClick(v)
    }

    open fun setOnSuccess (onSuccess: (FingerprintManagerCompat.AuthenticationResult) -> Unit) {
        this.onSuccess = onSuccess
    }

    open fun setOnError (onError: (messageError: String, dismiss: Boolean) -> Unit) {
        this.onError = onError
    }

    open fun setOnFailure (onFailure: () -> Unit) {
        this.onFailure = onFailure
    }

    open fun setOnCanceled (onCanceled: () -> Unit) {
        this.onCanceled = onCanceled
    }

    open fun setOnInformation(onInformation: (informationMessage: String) -> Unit) {
        this.onInformation = onInformation
    }
    //</editor-folder>

}