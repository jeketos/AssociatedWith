package com.jeketos.associatedwith.screen.control.riddle

import android.arch.lifecycle.ViewModel
import com.jeketos.associatedwith.di.ViewModelKey
import com.jeketos.associatedwith.di.scope.FragmentScope
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
class RiddlerProvidesModule{
    @Provides
    fun lobby(fragment: RiddlerFragment) = fragment.lobby
}

@Module(
    includes = [RiddlerProvidesModule::class]
)
interface RiddlerViewModelModule{
    @Binds @IntoMap
    @ViewModelKey(RiddlerViewModel::class)
    abstract fun riddlerViewModel(model: RiddlerViewModel): ViewModel
}

@Module
interface  RiddlerModule{
    @FragmentScope
    @ContributesAndroidInjector(modules = [RiddlerViewModelModule::class])
    fun inject(): RiddlerFragment
}