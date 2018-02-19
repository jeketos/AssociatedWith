package com.jeketos.associatedwith.screen.control

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.jeketos.associatedwith.R
import com.jeketos.associatedwith.data.Lobby
import com.jeketos.associatedwith.ext.replace
import com.jeketos.associatedwith.screen.control.riddle.RiddlerFragment
import com.jeketos.associatedwith.support.InjectorActivity
import com.jeketos.associatedwith.support.ProgressDelegate
import dagger.android.AndroidInjection
import javax.inject.Inject

class GameControlActivity : InjectorActivity() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: GameControlViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(GameControlViewModel::class.java)
    }
    val lobbyId: String by lazy { intent.extras.getString("lobbyId") }
    private val progressDelegate: ProgressDelegate by lazy { ProgressDelegate(supportFragmentManager) }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_game_control)
        viewModel.state.observe(this, Observer {
            val state = it!!
            when(state){
                GameControlViewModel.State.Progress -> showProgress()
                is GameControlViewModel.State.Riddler -> {
                    hideProgress()
                    showRiddler(state.lobby)
                }
                is GameControlViewModel.State.Guesser -> {
                    hideProgress()
                    showGuesser(state.lobby)
                }
            }
        })
    }

    private fun showGuesser(lobby: Lobby) {
    }

    private fun showRiddler(lobby: Lobby) {
        supportFragmentManager.replace(RiddlerFragment.newInstance(lobby), R.id.fragmentContainer)
    }

    private fun showProgress(){
        progressDelegate.showProgress()
    }

    private fun hideProgress(){
        progressDelegate.hideProgress()
    }

}