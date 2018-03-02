package com.jeketos.associatedwith.screen.control.chat

import android.arch.lifecycle.ViewModel
import com.jeketos.associatedwith.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import javax.inject.Named


@Module
class ChatListProvidesModule{
    @Provides @Named("lobbyId") fun lobbyId(fragment: ChatListFragment) = fragment.lobbyId
}

@Module(includes = [ChatListProvidesModule::class])
interface ChatListViewModelModule{

    @Binds @IntoMap
    @ViewModelKey(ChatListViewModel::class)
    abstract fun chatListViewModel(m: ChatListViewModel): ViewModel

}

@Module
interface ChatListModule{
    @ContributesAndroidInjector(modules = [ChatListViewModelModule::class])
    fun inject(): ChatListFragment
}