package com.jeketos.associatedwith.screen.lobbies

import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface AllLobbiesSubcomponent: AndroidInjector<AllLobbiesActivity> {

    @Subcomponent.Builder
    abstract class Builder: AndroidInjector.Builder<AllLobbiesActivity>()

}