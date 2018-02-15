package com.jeketos.associatedwith

import android.app.Activity
import android.support.multidex.MultiDexApplication
import com.jeketos.associatedwith.di.DaggerAppComponent
import com.jeketos.associatedwith.ext.get
import com.jeketos.associatedwith.ext.getPreferences
import com.jeketos.associatedwith.ext.put
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import java.util.*
import javax.inject.Inject

class App: MultiDexApplication(), HasActivityInjector {

    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingActivityInjector

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this)
        val id = getPreferences().get("userId", "")
        if(id.isEmpty()){
            getPreferences().put("userId", UUID.randomUUID())
        }
    }
}