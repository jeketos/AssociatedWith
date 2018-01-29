package com.jeketos.associatedwith.screen.play

import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface FindGameSubcomponent: AndroidInjector<FindGameActivity> {

    @Subcomponent.Builder
    abstract class Builder: AndroidInjector.Builder<FindGameActivity>()
}