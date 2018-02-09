package com.jeketos.associatedwith.screen.lobbies.privatelobbies

import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface PrivateLobbiesSubcomponent : AndroidInjector<PrivateLobbiesSubcomponent> {

    @Subcomponent.Builder
    abstract class Builder: AndroidInjector.Builder<PrivateLobbiesSubcomponent>()

}