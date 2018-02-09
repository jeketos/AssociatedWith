package com.jeketos.associatedwith.di

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.jeketos.associatedwith.App
import com.jeketos.associatedwith.di.scope.AppScope
import com.jeketos.associatedwith.screen.lobbies.AllLobbiesActivity
import com.jeketos.associatedwith.screen.lobbies.AllLobbiesSubcomponent
import com.jeketos.associatedwith.screen.lobbies.AllLobbiesViewModel
import com.jeketos.associatedwith.screen.lobbies.privatelobbies.PrivateLobbiesFragment
import com.jeketos.associatedwith.screen.lobbies.privatelobbies.PrivateLobbiesSubcomponent
import com.jeketos.associatedwith.screen.lobbies.privatelobbies.PrivateLobbiesViewModel
import com.jeketos.associatedwith.screen.lobbies.publiclobbies.PublicLobbiesFragment
import com.jeketos.associatedwith.screen.lobbies.publiclobbies.PublicLobbiesSubcomponent
import com.jeketos.associatedwith.screen.lobbies.publiclobbies.PublicLobbiesViewModel
import com.jeketos.associatedwith.screen.play.FindGameActivity
import com.jeketos.associatedwith.screen.play.FindGameSubcomponent
import com.jeketos.associatedwith.screen.play.FindGameViewModel
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.android.AndroidInjectionModule
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap


@AppScope
@Component(
        modules = [AndroidInjectionModule::class, AppModule::class, BuildersModule::class]
)
interface AppComponent {
    @Component.Builder
    interface Builder{
        @BindsInstance
        fun application(app: Application): Builder
        fun build(): AppComponent
    }
    fun inject(app: App)
}

@Module(
        subcomponents = [
            FindGameSubcomponent::class,
            AllLobbiesSubcomponent::class,
            PrivateLobbiesSubcomponent::class,
            PublicLobbiesSubcomponent::class
        ],
        includes = [ViewModelModule::class]
)
class AppModule{
//    @Provides
//    fun context(app: Application) = app.applicationContext!!
}

@Module
abstract class BuildersModule{

    @ContributesAndroidInjector
    abstract fun findGameActivity(): FindGameActivity

    @ContributesAndroidInjector
    abstract fun allLobbiesActivity(): AllLobbiesActivity

    @ContributesAndroidInjector
    abstract fun privateLobbiesFragment(): PrivateLobbiesFragment

    @ContributesAndroidInjector
    abstract fun publicLobbiesFragment(): PublicLobbiesFragment

}

@Module
abstract class ViewModelModule{

    @Binds
    @IntoMap
    @ViewModelKey(FindGameViewModel::class)
    abstract fun findGameViewModel(model: FindGameViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AllLobbiesViewModel::class)
    abstract fun allLobbiesViewModel(model: AllLobbiesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PrivateLobbiesViewModel::class)
    abstract fun privateLobbiesViewModel(model: PrivateLobbiesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PublicLobbiesViewModel::class)
    abstract fun publicLobbiesViewModel(model: PublicLobbiesViewModel): ViewModel


    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}