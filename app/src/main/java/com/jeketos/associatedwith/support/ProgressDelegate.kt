package com.jeketos.associatedwith.support

import android.support.v4.app.FragmentManager

class ProgressDelegate constructor(
        val fragmentManager: FragmentManager
) {
    private var dialog: ProgressDialog? = null


    fun showProgress(){
        if (dialog == null){
            dialog = ProgressDialog.newInstance()
            dialog?.show(fragmentManager, "progress_dialog")
        }
    }

    fun  hideProgress(){
        dialog?.dismiss()
        dialog = null
    }

}