package com.jeketos.associatedwith.screen.control.riddle

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.os.bundleOf
import com.jeketos.associatedwith.R
import com.jeketos.associatedwith.data.Lobby
import com.jeketos.associatedwith.ext.get
import com.jeketos.associatedwith.ext.replace
import com.jeketos.associatedwith.screen.control.chat.ChatListFragment
import com.jeketos.associatedwith.support.ProgressDelegate
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.screen_riddler.*
import org.jetbrains.anko.toast
import javax.inject.Inject

class RiddlerFragment : DaggerFragment() {

    companion object {
        @JvmStatic fun newInstance(lobby: Lobby) = RiddlerFragment().apply { arguments = bundleOf("lobby" to lobby) }
    }

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(RiddlerViewModel::class)
    }
    val lobby by lazy { arguments!!["lobby"] as Lobby }
    private val progressDelegate by lazy { ProgressDelegate(fragmentManager!!) }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.screen_riddler, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(savedInstanceState == null){
            childFragmentManager.replace(
                    ChatListFragment.newInstance(lobby.id, ChatListFragment.Type.RIDDLER),
                    R.id.chatContainer
            )
        }
        viewModel.observe(this){
            when (it){
                RiddlerViewModel.State.Progress -> showProgress()
                is RiddlerViewModel.State.ShowWords -> {
                    hideProgress()
                    showWordsDialog(it.words)
                }
            }
        }
        drawingView.setDrawColor(ContextCompat.getColor(context!!, R.color.red))
        drawingView.setOnDrawListener {
            viewModel.sendPoint(it)
        }
    }
    private fun showWordsDialog(words: List<String>) {
        AlertDialog.Builder(context!!)
                .setCancelable(false)
                .setSingleChoiceItems(
                        words.toTypedArray(),
                        0,
                        { dialog, pos ->
                            context!!.toast(words[pos])
                            viewModel.onWordSelected(words[pos])
                            dialog.dismiss()
                        }
                ).show()
    }

    private fun showProgress(){
        progressDelegate.showProgress()
    }

    private fun hideProgress(){
        progressDelegate.hideProgress()
    }

}