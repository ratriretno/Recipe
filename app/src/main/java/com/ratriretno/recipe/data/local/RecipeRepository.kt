package com.ratriretno.recipe.data.local

import kotlinx.coroutines.flow.Flow

interface RecipeRepository {

    fun getAllStream(): Flow<List<LocalRecipe>>

    suspend fun getAll(): List<LocalRecipe>

    fun getAllFavoriteStream(): Flow<List<LocalRecipe>>

    suspend fun insertList(list: List<LocalRecipe>)
    suspend fun insert(localRecipe: LocalRecipe)

    suspend fun update (localRecipe: LocalRecipe)

    fun getRecipe (id : String) : Flow <LocalRecipe>

    suspend fun getPagingRecipe(limit: Int, offset: Int): List<LocalRecipe>
    suspend fun getPagingRecipeFavorite(limit: Int, offset: Int): List<LocalRecipe>

}