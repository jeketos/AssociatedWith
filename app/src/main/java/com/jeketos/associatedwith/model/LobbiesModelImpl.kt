package com.jeketos.associatedwith.model

import com.google.firebase.database.DatabaseReference
import com.jeketos.associatedwith.data.DataEvent
import com.jeketos.associatedwith.data.Lobby
import com.jeketos.associatedwith.data.PrivateLobby
import com.jeketos.associatedwith.ext.getRxObservableChildSnapshot
import io.reactivex.Observable
import javax.inject.Inject

class LobbiesModelImpl @Inject constructor(
    private val rootNode: DatabaseReference
): LobbiesModel {

    private val privateLobbiesNode = rootNode.child("lobbies").child("private")!!
    private val publicLobbiesNode = rootNode.child("lobbies").child("public")!!

    override fun observePrivateLobbies(): Observable<DataEvent<PrivateLobby>>{
      return  privateLobbiesNode.getRxObservableChildSnapshot()
                .map {
                    val l = it.snapshot!!.getValue(PrivateLobby::class.java)!!
                    val lobby = l.copy(members = it.snapshot.child("members").children.map { it.key })
                    DataEvent(it.op, lobby)
                }
    }

    override fun observePublicLobbies(): Observable<DataEvent<Lobby>>{
        return  publicLobbiesNode.getRxObservableChildSnapshot()
                .map {
                    val l = it.snapshot!!.getValue(Lobby::class.java)!!
                    val lobby = l.copy(
                            id = it.snapshot.key,
                            members = it.snapshot.child("members").children.map { it.key }
                    )
                    DataEvent(it.op, lobby)
                }
    }

}