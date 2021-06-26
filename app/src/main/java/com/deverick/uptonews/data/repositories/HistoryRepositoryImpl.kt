package com.deverick.uptonews.data.repositories

import com.deverick.uptonews.models.News
import com.deverick.uptonews.utils.*
import com.google.firebase.firestore.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

class HistoryRepositoryImpl(
    private val firestore: FirebaseFirestore,
) : HistoryRepository {
    override fun addHistoryItem(userId: String, news: News) {
        val collection = firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(HISTORY_COLLECTION)

        try {
            collection.document(news.id!!).set(news)
        } catch (e: Exception) {
        }
    }

    override fun removeHistoryItem(userId: String, news: News) {
        val collection = firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(HISTORY_COLLECTION)

        try {
            collection.document(news.id!!).delete()
        } catch (e: Exception) {
        }
    }

    @ExperimentalCoroutinesApi
    override suspend fun getHistory(userId: String) = callbackFlow<Resource<List<News>>> {
        val collection = firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(HISTORY_COLLECTION)

        send(Resource.Loading())

        val listener = collection.addSnapshotListener { value, error ->
            error?.let {
                trySend(Resource.Error(it.message.toString()))
            }

            value?.let { snapshot ->
                val news = snapshot.documents.map {
                    it.toObject(News::class.java)!!
                }

                trySend(Resource.Success(news))
            }
        }

        awaitClose {
            listener.remove()
        }
    }
}