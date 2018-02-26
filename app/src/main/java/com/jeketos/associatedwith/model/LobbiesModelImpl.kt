package com.jeketos.associatedwith.model

import com.google.firebase.database.DatabaseReference
import com.jeketos.associatedwith.data.*
import com.jeketos.associatedwith.ext.*
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class LobbiesModelImpl @Inject constructor(
    rootNode: DatabaseReference
): LobbiesModel {

    private val privateLobbiesNode = rootNode.child(Nodes.privateLobbies)!!
    private val publicLobbiesNode = rootNode.child(Nodes.publicLobbies)!!
    private val selectedWordsNode = rootNode.child(Nodes.selectedWords)!!
    private val winnersNode = rootNode.child(Nodes.winners)!!

    override fun observePrivateLobbies(): Observable<DataEvent<PrivateLobby>>{
      return  privateLobbiesNode.getRxObservableChildSnapshot()
                .map {
                    val l = it.snapshot!!.getValue(PrivateLobby::class.java)!!
                    val lobby = l.copy(
                            id = it.snapshot.key,
                            members = it.snapshot
                                    .child("members")
                                    .children
                                    .map { it.getValue(Member::class.java)!!.copy(id = it.key) }
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
                                    .map { it.getValue(Member::class.java)!!.copy(id = it.key) }
                    )
                    DataEvent(it.op, lobby)
                }
    }

    override fun getLobby(lobbyId: String): Single<Lobby> =
            publicLobbiesNode.child(lobbyId)
                    .getRxSingleSnapshot()
                    .map {
                        val l = it.getValue(Lobby::class.java)!!
                        l.copy(
                                id = lobbyId,
                                members = it.child("members")
                                .children
                                .map { it.getValue(Member::class.java)!!.copy(id = it.key) }
                        )
                    }

    override fun setSelectedWord(lobbyId: String, word: String){
        selectedWordsNode.child(lobbyId).child("selectedWord").setValueRx(word)
                .subscribe({},{loge(it)})
    }

    override fun observeWinner(lobbyId: String): Observable<Winner> =
            winnersNode.child(lobbyId)
                    .getRxObservableSnapshot()
                    .filter { it.value != null }
                    .map { it.getValue(Winner::class) }
}