package com.jeketos.associatedwith.model

import android.content.Context
import com.jeketos.associatedwith.ext.get
import com.jeketos.associatedwith.ext.getPreferences
import com.jeketos.associatedwith.ext.subscribeIoObserveMain
import com.jeketos.associatedwith.rest.RestService
import io.reactivex.Single
import javax.inject.Inject

class RestModelImpl @Inject constructor(
    private val service: RestService,
    val context: Context
): RestModel{

    override fun findGame(): Single<String> =
            service.findGame(context.getPreferences().get("userId", ""))
                    .subscribeIoObserveMain()

}