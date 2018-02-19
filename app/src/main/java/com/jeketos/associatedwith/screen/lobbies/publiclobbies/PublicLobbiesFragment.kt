package com.jeketos.associatedwith.screen.lobbies.publiclobbies

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jeketos.associatedwith.R
import com.jeketos.associatedwith.ext.addDividerItemDecoration
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.screen_public_lobbies.*
import javax.inject.Inject

class PublicLobbiesFragment: Fragment() {

    companion object {
        @JvmStatic fun newInstance(): PublicLobbiesFragment = PublicLobbiesFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: PublicLobbiesViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(PublicLobbiesViewModel::class.java)
    }
    private val controller by lazy { PublicLobbiesController() }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.screen_public_lobbies, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.setController(controller)
        recyclerView.addDividerItemDecoration()
        viewModel.lobbiesState.observe(this, Observer {
            val state = it!!
            when (state){
                LobbiesState.Idle -> {}
                is LobbiesState.Add -> controller.updateItem(state.lobby)
                is LobbiesState.Change -> controller.updateItem(state.lobby)
                is LobbiesState.Move -> {}
                is LobbiesState.Remove -> controller.removeItem(state.lobby)
            }
        })
    }
}