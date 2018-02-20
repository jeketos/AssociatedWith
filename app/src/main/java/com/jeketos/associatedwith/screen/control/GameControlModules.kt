package com.jeketos.associatedwith.screen.control

import android.arch.lifecycle.ViewModel
import com.jeketos.associatedwith.di.ViewModelKey
import com.jeketos.associatedwith.di.scope.ActivityScope
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import javax.inject.Named

@Module
class GameControlProvidesModule{
    @Provides
    @Named("lobbyId") fun lobbyId(activity: GameControlActivity) = activity.lobbyId
}

@Module(includes = [GameControlProvidesModule::class])
interface ViewModelModule{
    @Binds
    @IntoMap
    @ViewModelKey(GameControlViewModel::class)
    abstract fun gameControlViewModel(model: GameControlViewModel): ViewModel
}


@Module
interface GameControlModule{
    @ActivityScope
    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    fun inject(): GameControlActivity
}
