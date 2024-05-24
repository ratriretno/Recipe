package com.ratriretno.recipe.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

@Dao
interface RecipeDao {

    @Insert
    suspend fun insert(recipe: LocalRecipe)

    @Insert
    suspend fun insertList(recipe: List<LocalRecipe>)

    @Query("SELECT * FROM recipes WHERE id = :id")
    suspend fun getById(id: String): LocalRecipe?

    @Update
    suspend fun update(recipe: LocalRecipe)

    @Query("SELECT * FROM recipes")
    suspend fun getAll (): List<LocalRecipe>

    @Query("SELECT * FROM recipes")
    fun observeAll(): Flow<List<LocalRecipe>>

    @Query("SELECT * FROM recipes WHERE isFavorite = true")
    fun observeAllFavorite(): Flow<List<LocalRecipe>>
}