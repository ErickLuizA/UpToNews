package com.deverick.uptonews.repositories

import com.deverick.uptonews.models.News
import com.deverick.uptonews.models.NewsResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NewsRepositoryImpl : NewsRepository {
    override suspend fun getRecommendedNews(): Flow<NewsResponse> {
        val newsResponse = NewsResponse(
            listOf(
                News(
                    id = "id 1",
                    title = "Title 1",
                    image = "https://i.picsum.photos/id/902/500/300.jpg?hmac=95zI0jikRG0n4KB1siaAPfnLB4kJfNNzPVKkHIK6Uqs",
                    description = "Description 1",
                    date = "May 20",
                    readTime = "6 min",
                ),
                News(
                    id = "id 2",
                    title = "Title 2",
                    image = "https://i.picsum.photos/id/902/500/300.jpg?hmac=95zI0jikRG0n4KB1siaAPfnLB4kJfNNzPVKkHIK6Uqs",
                    description = "Description 2",
                    date = "May 20",
                    readTime = "6 min",
                ),
                News(
                    id = "id 3",
                    title = "Title 3",
                    image = "https://i.picsum.photos/id/902/500/300.jpg?hmac=95zI0jikRG0n4KB1siaAPfnLB4kJfNNzPVKkHIK6Uqs",
                    description = "Description 3",
                    date = "May 20",
                    readTime = "6 min",
                ),
                News(
                    id = "id 4",
                    title = "Title 4",
                    image = "https://i.picsum.photos/id/902/500/300.jpg?hmac=95zI0jikRG0n4KB1siaAPfnLB4kJfNNzPVKkHIK6Uqs",
                    description = "Description 4",
                    date = "May 20",
                    readTime = "6 min",
                ),
            )
        )

        return flow {
            delay(3000)

            emit(newsResponse)
        }
    }
}