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
import com.jeketos.associatedwith.ext.setScrollToLast
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.screen_chat_list.*
import javax.inject.Inject

class ChatListFragment: Fragment() {

    companion object {
        @JvmStatic fun newInstance(lobbyId: String, type: Type) =
                ChatListFragment().apply {
                    arguments = bundleOf("lobbyId" to lobbyId, "type" to type)
                }
    }

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(ChatListViewModel::class) }
    val lobbyId by lazy { arguments!!["lobbyId"] as String }
    val type by lazy { arguments!!["type"] as Type }
    private val controller by lazy { ChatListController(
            type,
            {viewModel.updateMessage(it)}
    ) }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.screen_chat_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.setController(controller)
        recyclerView.setScrollToLast()
        viewModel.state.observe(this, KObserver{
            controller.updateItems(it)
        })
    }

    enum class Type{
        GUESSER, RIDDLER
    }

}