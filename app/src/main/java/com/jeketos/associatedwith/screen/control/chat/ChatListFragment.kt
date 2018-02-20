package com.jeketos.associatedwith.screen.control.chat

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.os.bundleOf
import com.jeketos.associatedwith.R
import com.jeketos.associatedwith.ext.KObserver
import com.jeketos.associatedwith.ext.get
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.screen_chat_list.*
import javax.inject.Inject

class ChatListFragment: Fragment() {

    companion object {
        @JvmStatic fun newInstance(lobbyId: String) =
                ChatListFragment().apply {
                    arguments = bundleOf("lobbyId" to lobbyId)
                }
    }
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(ChatListViewModel::class) }
    val lobbyId by lazy { arguments!!["lobbyId"] as String }
    private val controller by lazy { ChatListController() }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.screen_chat_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.setController(controller)
        viewModel.state.observe(this, KObserver{
            controller.updateItem(it)
        })
    }

}