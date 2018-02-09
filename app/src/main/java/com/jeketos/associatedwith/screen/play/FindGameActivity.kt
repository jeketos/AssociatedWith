package com.jeketos.associatedwith.screen.play

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jeketos.associatedwith.R
import com.jeketos.associatedwith.ext.parkinsonClick
import com.jeketos.associatedwith.screen.lobbies.AllLobbiesActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.screen_find_game.*
import org.jetbrains.anko.startActivity
import javax.inject.Inject

class FindGameActivity : AppCompatActivity() {

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
    }

    private fun goToAllLobbies() {
        startActivity<AllLobbiesActivity>()
    }
}
