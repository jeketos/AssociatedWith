package com.jeketos.associatedwith.di

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jeketos.associatedwith.App
import com.jeketos.associatedwith.BuildConfig
import com.jeketos.associatedwith.di.scope.AppScope
import com.jeketos.associatedwith.model.LobbiesModel
import com.jeketos.associatedwith.model.LobbiesModelImpl
import com.jeketos.associatedwith.screen.createlobby.CreateLobbyDialogFragment
import com.jeketos.associatedwith.screen.createlobby.CreateLobbySubcomponent
import com.jeketos.associatedwith.screen.createlobby.CreateLobbyViewModel
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
import dagger.*
import dagger.android.AndroidInjectionModule
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap


@AppScope
@Component(
        modules = [
            AndroidInjectionModule::class,
            AppModule::class,
            BuildersModule::class,
            ModelModule::class
        ]
)
interface AppComponent {
    @Component.Builder
    interface Builder{
        @BindsInstance
        fun application(app: Application): Builder
        fun build(): AppComponent
    }
    fun inject(app: App)
    fun rootNode(): DatabaseReference
    fun lobbiesModel(): LobbiesModel
}

@Module(
        subcomponents = [
            FindGameSubcomponent::class,
            AllLobbiesSubcomponent::class,
            PrivateLobbiesSubcomponent::class,
            PublicLobbiesSubcomponent::class,
            CreateLobbySubcomponent::class
        ],
        includes = [ViewModelModule::class]
)
class AppModule{
//    @Provides
//    fun context(app: Application) = app.applicationContext!!

    @AppScope
    @Provides
    fun rootFirebaseNode(): DatabaseReference =
            FirebaseDatabase.getInstance().reference.child(BuildConfig.ROOT_NODE)
}

@Module
class ModelModule{

    @AppScope
    @Provides
    fun lobbiesModel(model: LobbiesModelImpl): LobbiesModel = model

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

    @ContributesAndroidInjector
    abstract fun createLobbyDialogFragment(): CreateLobbyDialogFragment

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
    @IntoMap
    @ViewModelKey(CreateLobbyViewModel::class)
    abstract fun createLobbyViewModel(model: CreateLobbyViewModel): ViewModel


    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}