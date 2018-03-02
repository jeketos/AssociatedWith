package com.jeketos.associatedwith.screen.control.winner

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import androidx.os.bundleOf
import com.jeketos.associatedwith.R
import com.jeketos.associatedwith.data.USER_ID
import com.jeketos.associatedwith.data.Winner
import com.jeketos.associatedwith.ext.get
import com.jeketos.associatedwith.ext.getPreferences
import com.jeketos.associatedwith.screen.control.GameControlActivity
import org.jetbrains.anko.startActivity

class WinnerDialogFragment : DialogFragment() {

    companion object {
        @JvmStatic fun newInstance(winner: Winner) = WinnerDialogFragment().apply {
            arguments = bundleOf("winner" to winner)
        }
    }

    val winner by lazy { arguments!!["winner"] as Winner }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val message = if (winner.userId == context!!.getPreferences().get<String>(USER_ID))
            getString(R.string.message_you_are_winner)
        else getString(R.string.message_winner, winner.userId)
        val builder = AlertDialog.Builder(context!!)
                .setTitle(R.string.title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, { _, _ ->
                    activity?.finish()
                    context?.startActivity<GameControlActivity>("lobbyId" to winner.nextLobbyId)
                })
        return builder.create()
    }

}