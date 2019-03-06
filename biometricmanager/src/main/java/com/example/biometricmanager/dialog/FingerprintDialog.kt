package com.example.biometricmanager.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.support.annotation.LayoutRes
import com.example.biometricmanager.generics.BiometricBaseDialog


@SuppressLint("ValidFragment")
class FingerprintDialog() : BiometricBaseDialog() {

    lateinit var ctx: Context

    constructor(context: Context, @LayoutRes layout: Int) : this () {
        this.ctx = context
        this.layout = layout
        this.ctx.createBiometricView()
    }


    override fun initAuth() {

    }

}