package com.jeketos.associatedwith.di

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.jeketos.associatedwith.App
import com.jeketos.associatedwith.di.scope.AppScope
import com.jeketos.associatedwith.screen.play.FindGameActivity
import com.jeketos.associatedwith.screen.play.FindGameSubcomponent
import com.jeketos.associatedwith.screen.play.FindGameViewModel
import dagger.*
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
        subcomponents = [FindGameSubcomponent::class],
        includes = [ViewModelModule::class]
)
class AppModule{
    @Provides
    fun context(app: Application) = app.applicationContext!!
}

@Module
abstract class BuildersModule{

    @ContributesAndroidInjector
    abstract fun findGameActivity(): FindGameActivity

}

@Module
abstract class ViewModelModule{

    @Binds
    @IntoMap
    @ViewModelKey(FindGameViewModel::class)
    abstract fun findGameViewModel(model: FindGameViewModel): ViewModel


    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}