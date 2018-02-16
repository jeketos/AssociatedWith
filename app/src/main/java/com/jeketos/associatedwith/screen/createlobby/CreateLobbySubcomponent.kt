package com.jeketos.associatedwith.screen.createlobby

import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface CreateLobbySubcomponent: AndroidInjector<CreateLobbyDialogFragment> {

    @Subcomponent.Builder
    abstract class Builder: AndroidInjector.Builder<CreateLobbyDialogFragment>()

}