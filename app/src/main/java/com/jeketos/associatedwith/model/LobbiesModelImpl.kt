package com.jeketos.associatedwith.model

import com.google.firebase.database.DatabaseReference
import com.jeketos.associatedwith.data.DataEvent
import com.jeketos.associatedwith.data.Lobby
import com.jeketos.associatedwith.data.PrivateLobby
import com.jeketos.associatedwith.data.toMember
import com.jeketos.associatedwith.ext.getRxObservableChildSnapshot
import com.jeketos.associatedwith.ext.setValueRx
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class LobbiesModelImpl @Inject constructor(
    rootNode: DatabaseReference
): LobbiesModel {

    private val privateLobbiesNode = rootNode.child("lobbies").child("private")!!
    private val publicLobbiesNode = rootNode.child("lobbies").child("public")!!

    override fun observePrivateLobbies(): Observable<DataEvent<PrivateLobby>>{
      return  privateLobbiesNode.getRxObservableChildSnapshot()
                .map {
                    val l = it.snapshot!!.getValue(PrivateLobby::class.java)!!
                    val lobby = l.copy(
                            id = it.snapshot.key,
                            members = it.snapshot
                                    .child("members")
                                    .children
                                    .map { it.child("drawer") }
                                    .map { it.toMember() }
                    )
                    DataEvent(it.op, lobby)
                }
    }

    override fun createPrivateLobby(name: String, password: String): Single<PrivateLobby>{
        val push = privateLobbiesNode.push()
        return push.setValueRx(mapOf("name" to name, "password" to password))
                .toSingleDefault(PrivateLobby(push.key, name, password))

    }

    override fun observePublicLobbies(): Observable<DataEvent<Lobby>>{
        return  publicLobbiesNode.getRxObservableChildSnapshot()
                .map {
                    val l = it.snapshot!!.getValue(Lobby::class.java)!!
                    val lobby = l.copy(
                            id = it.snapshot.key,
                            members = it.snapshot
                                    .child("members")
                                    .children
                                    .map { it.child("drawer") }
                                    .map { it.toMember() }
                    )
                    DataEvent(it.op, lobby)
                }
    }

}