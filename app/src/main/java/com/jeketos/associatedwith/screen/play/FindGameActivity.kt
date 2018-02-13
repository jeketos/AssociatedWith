package com.jeketos.associatedwith.screen.play

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.jeketos.associatedwith.R
import com.jeketos.associatedwith.ext.parkinsonClick
import com.jeketos.associatedwith.screen.createlobby.CreateLobbyDialogFragment
import com.jeketos.associatedwith.screen.lobbies.AllLobbiesActivity
import com.jeketos.associatedwith.support.InjectorActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.screen_find_game.*
import org.jetbrains.anko.startActivity
import javax.inject.Inject

class FindGameActivity : InjectorActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: FindGameViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(FindGameViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_find_game)
        allLobbiesButton.parkinsonClick {
            goToAllLobbies()
        }
        createLobbyButton.parkinsonClick {
            goToCreateLobby()
        }
    }

    private fun goToCreateLobby() {
        CreateLobbyDialogFragment.newInstance().show(supportFragmentManager, "create_lobby")
    }

    private fun goToAllLobbies() {
        startActivity<AllLobbiesActivity>()
    }
}
