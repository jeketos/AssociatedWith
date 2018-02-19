package com.jeketos.associatedwith.model

import com.google.firebase.database.DatabaseReference
import com.jeketos.associatedwith.data.Nodes
import com.jeketos.associatedwith.data.Point
import com.jeketos.associatedwith.ext.Op
import com.jeketos.associatedwith.ext.getRxObservableChildSnapshot
import com.jeketos.associatedwith.ext.loge
import com.jeketos.associatedwith.ext.setValueRx
import io.reactivex.Observable
import javax.inject.Inject

class DrawModelImpl @Inject constructor(
    rootNode: DatabaseReference
): DrawModel{

    private val drawPointsNode = rootNode.child(Nodes.drawPoints)

    override fun sendPoint(lobbyId: String, point: Point){
        drawPointsNode.child(lobbyId).push().setValueRx(point)
                .subscribe({},{loge(it)})
    }

    override fun observePoints(lobbyId: String): Observable<Point> =
            drawPointsNode.child(lobbyId).getRxObservableChildSnapshot()
                    .filter { it.op == Op.ADD }
                    .map {
                        it.snapshot!!.getValue(Point::class.java)!!
                    }


}