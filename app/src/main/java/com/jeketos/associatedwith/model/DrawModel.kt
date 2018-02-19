package com.jeketos.associatedwith.model

import com.jeketos.associatedwith.data.Point
import io.reactivex.Observable

interface DrawModel {
    fun sendPoint(lobbyId: String, point: Point)
    fun observePoints(lobbyId: String): Observable<Point>
}