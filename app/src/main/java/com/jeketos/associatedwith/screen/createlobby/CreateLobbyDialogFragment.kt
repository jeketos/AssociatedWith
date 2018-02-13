package com.jeketos.associatedwith.screen.createlobby

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jeketos.associatedwith.R
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class CreateLobbyDialogFragment : DialogFragment() {

    companion object {
        @JvmStatic fun newInstance(): CreateLobbyDialogFragment = CreateLobbyDialogFragment()
    }

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val viewModel by lazy { ViewModelProviders.of(this, factory).get(CreateLobbyViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.screen_create_lobby, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}