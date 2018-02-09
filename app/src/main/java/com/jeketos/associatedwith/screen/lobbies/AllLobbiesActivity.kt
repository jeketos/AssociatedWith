package com.jeketos.associatedwith.screen.lobbies

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jeketos.associatedwith.R
import dagger.android.AndroidInjection

class AllLobbiesActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_all_lobbies)
    }

}