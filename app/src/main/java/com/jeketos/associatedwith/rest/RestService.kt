package com.jeketos.associatedwith.rest

import com.jeketos.associatedwith.data.ValueHolder
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RestService{

    @GET("findGame")
    fun findGame(@Query("id")id: String = ""): Single<ValueHolder<String>>

    @GET("getWords")
    fun getWords(@Query("count")count: Int = 3): Single<List<String>>

}