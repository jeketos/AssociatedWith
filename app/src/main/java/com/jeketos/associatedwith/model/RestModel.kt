package com.jeketos.associatedwith.model

import io.reactivex.Single

interface RestModel {

    fun findGame(): Single<String>

}