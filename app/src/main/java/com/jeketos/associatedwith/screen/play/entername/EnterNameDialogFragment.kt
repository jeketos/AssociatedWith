package com.jeketos.associatedwith.screen.play.entername

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jeketos.associatedwith.R
import com.jeketos.associatedwith.data.USER_NAME
import com.jeketos.associatedwith.ext.getPreferences
import com.jeketos.associatedwith.ext.parkinsonClick
import com.jeketos.associatedwith.ext.put
import kotlinx.android.synthetic.main.screen_enter_name.*

class EnterNameDialogFragment  : DialogFragment() {

    companion object {
        @JvmStatic fun newInstance(): EnterNameDialogFragment = EnterNameDialogFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.screen_enter_name, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enter.parkinsonClick {
            if(name.text.isNotEmpty()){
                context!!.getPreferences().put(USER_NAME, name.text.trim().toString())
                dismiss()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val params = dialog.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        dialog.window!!.attributes = params as android.view.WindowManager.LayoutParams
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
    }

}