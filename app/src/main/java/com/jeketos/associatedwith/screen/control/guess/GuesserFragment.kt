package com.jeketos.associatedwith.screen.control.guess

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.os.bundleOf
import androidx.view.doOnLayout
import com.jeketos.associatedwith.R
import com.jeketos.associatedwith.data.Lobby
import com.jeketos.associatedwith.data.Point
import com.jeketos.associatedwith.data.actionEnum
import com.jeketos.associatedwith.ext.parkinsonClick
import com.jeketos.associatedwith.ext.replace
import com.jeketos.associatedwith.ext.setEnterKeyListener
import com.jeketos.associatedwith.screen.control.chat.ChatListFragment
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.screen_guesser.*
import javax.inject.Inject

class GuesserFragment: DaggerFragment() {

    companion object {
        @JvmStatic fun newInstance(lobby: Lobby) =
                GuesserFragment().apply { arguments = bundleOf("lobby" to lobby) }
    }

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(GuesserViewModel::class.java)
    }
    val lobby: Lobby by lazy { arguments!!["lobby"] as Lobby }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.screen_guesser, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(savedInstanceState == null){
            childFragmentManager.replace(
                    ChatListFragment.newInstance(lobby.id, ChatListFragment.Type.GUESSER),
                    R.id.chatContainer
            )
        }
        viewModel.observeState(this){
            when(it){
                GuesserViewModel.State.Idle -> {}
                is GuesserViewModel.State.OnPoint -> drawPoint(it.point)
            }
        }
        drawingView.doOnLayout {
            viewModel.observePoints()
        }
        sendButton.parkinsonClick {
            sendMessage()
        }
        messageToSend.setEnterKeyListener {
            sendMessage()
        }
    }

    private fun sendMessage() {
        viewModel.sendMessage(messageToSend.text.toString())
        messageToSend.setText("")
    }

    private fun drawPoint(point: Point) {
        when (point.actionEnum){
            Point.ACTION.START -> drawingView.actionDown(point.x, point.y)
            Point.ACTION.STOP -> drawingView.actionUp()
            Point.ACTION.MOVE -> drawingView.actionMove(point.x, point.y)
        }
    }
}