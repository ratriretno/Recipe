package com.ratriretno.recipe.data.local

import kotlinx.coroutines.flow.Flow

interface RecipeRepository {

    fun getAllStream(): Flow<List<LocalRecipe>>

    fun getAllFavoriteStream(): Flow<List<LocalRecipe>>

    suspend fun insertList(list: List<LocalRecipe>)

    suspend fun update (localRecipe: LocalRecipe)

    fun getRecipe (id : String) : LocalRecipe

}