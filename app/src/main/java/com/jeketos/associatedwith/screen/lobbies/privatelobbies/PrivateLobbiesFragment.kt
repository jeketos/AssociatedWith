package com.jeketos.associatedwith.screen.lobbies.privatelobbies

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jeketos.associatedwith.R
import com.jeketos.associatedwith.ext.logd
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class PrivateLobbiesFragment: Fragment() {

    companion object {
        @JvmStatic fun newInstance(): PrivateLobbiesFragment = PrivateLobbiesFragment()
    }

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: PrivateLobbiesViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(PrivateLobbiesViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.screen_private_lobbies, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.lobbiesState.observe(this, Observer {
            when(it!!){
                LobbiesState.Idle -> logd("idle")
                is LobbiesState.Add -> logd("Add")
                is LobbiesState.Change -> logd("Change")
                is LobbiesState.Move -> logd("Move")
                is LobbiesState.Remove -> logd("Remove")
            }
        })
    }
}