package com.deverick.uptonews.data.repositories

import com.deverick.uptonews.models.News
import com.deverick.uptonews.utils.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

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

    override fun removeHistoryItem(userId: String, news: News): Boolean {
        val collection = firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(HISTORY_COLLECTION)

        return try {
            collection.document(news.id!!).delete()

            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getHistory(userId: String): Flow<Resource<List<News>>> = flow {
        emit(Resource.Loading())

        val collection = firestore
            .collection(USERS_COLLECTION)
            .document(userId)
            .collection(HISTORY_COLLECTION)

        val snapshot = collection.get().await()

        val news = snapshot.documents.map {
            it.toObject(News::class.java)!!
        }

        emit(Resource.Success(news))
    }.catch {
        emit(Resource.Error(it.message.toString()))
    }.flowOn(Dispatchers.IO)
}
