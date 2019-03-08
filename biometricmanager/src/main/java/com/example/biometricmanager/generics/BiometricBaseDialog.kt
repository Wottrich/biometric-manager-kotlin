package com.example.biometricmanager.generics

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.annotation.RequiresApi
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.*
import com.example.biometricmanager.exceptions.NonLayoutException

abstract class BiometricBaseDialog : DialogFragment() {


    @LayoutRes
    var layout: Int = 0
    lateinit var activity: Activity
    var baseView: View? = null

    open fun show(fragmentManager: FragmentManager) {
        this.show(fragmentManager, "BiometricDialog")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        val mDialog = dialog

        mDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val params = mDialog.window?.attributes as WindowManager.LayoutParams
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        params.dimAmount = 0f
        mDialog.window?.attributes = params
        mDialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (baseView == null)
            baseView = inflater.inflate(layout, container)

        if (layout != 0) this.initAuth()
        else NonLayoutException()

        return baseView
    }


    abstract fun initAuth()

    open fun Context.createBiometricView() {
        if (baseView == null) {
            baseView = LayoutInflater.from(this).inflate(layout, null, false)
        }
    }


}