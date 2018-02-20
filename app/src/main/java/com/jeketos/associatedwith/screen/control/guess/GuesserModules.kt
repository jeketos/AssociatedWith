package com.jeketos.associatedwith.screen.control.guess

import android.arch.lifecycle.ViewModel
import com.jeketos.associatedwith.di.ViewModelKey
import com.jeketos.associatedwith.di.scope.FragmentScope
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
class GuesserProvidersModule{
    @Provides fun lobby(fragment: GuesserFragment) = fragment.lobby
}

@Module(includes = [GuesserProvidersModule::class])
interface GuesserViewModelModule{
    @Binds @IntoMap
    @ViewModelKey(GuesserViewModel::class)
    abstract fun guesserViewModel(m: GuesserViewModel): ViewModel
}

@Module
interface GuesserModule{
    @FragmentScope
    @ContributesAndroidInjector(modules = [GuesserViewModelModule::class])
    fun inject(): GuesserFragment
}