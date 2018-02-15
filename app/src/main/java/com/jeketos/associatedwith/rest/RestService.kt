package com.jeketos.associatedwith.rest

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RestService{

    @GET("findGame")
    fun findGame(@Query("id")id: String = ""): Single<String>

}