package com.ratriretno.recipe.data.network

import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class RecipeNetworkDataSource @Inject constructor() : NetworkDataSource {

    // A mutex is used to ensure that reads and writes are thread-safe.
    private val accessMutex = Mutex()
    private var tasks = listOf(
        NetworkTask(
            id = "PISA",
            title = "Build tower in Pisa",
            shortDescription = "Ground looks good, no foundation work required."
        ),
        NetworkTask(
            id = "TACOMA",
            title = "Finish bridge in Tacoma",
            shortDescription = "Found awesome girders at half the cost!"
        )
    )


    override suspend fun loadRecipe(): List<NetworkTask> {
        TODO("Not yet implemented")
    }

    override suspend fun saveRecipe(tasks: List<NetworkTask>) {
        TODO("Not yet implemented")
    }
}

private const val SERVICE_LATENCY_IN_MILLIS = 2000L
