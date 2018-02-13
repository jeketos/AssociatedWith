package com.jeketos.associatedwith.screen.lobbies.publiclobbies

import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface PublicLobbiesSubcomponent: AndroidInjector<PublicLobbiesFragment> {

    @Subcomponent.Builder
    abstract class Builder: AndroidInjector.Builder<PublicLobbiesFragment>()
}