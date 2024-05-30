package com.ratriretno.recipe.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(recipe: LocalRecipe)

    @Insert
    suspend fun insertList(recipe: List<LocalRecipe>)

    @Query("SELECT * FROM recipes WHERE id = :id")
    fun getById(id: String): Flow <LocalRecipe>

    @Update
    suspend fun update(recipe: LocalRecipe)

    @Query("SELECT * FROM recipes")
    suspend fun getAll (): List<LocalRecipe>

    @Query("SELECT * FROM recipes")
    fun observeAll(): Flow<List<LocalRecipe>>

    @Query("SELECT * FROM recipes WHERE isFavorite = true")
    fun observeAllFavorite(): Flow<List<LocalRecipe>>

    @Query("SELECT * FROM recipes ORDER BY title ASC LIMIT :limit OFFSET :offset")
    suspend fun getPagingRecipe(limit: Int, offset: Int): List<LocalRecipe>

    @Query("SELECT * FROM recipes  WHERE title LIKE '%' || :query || '%' ORDER BY title ASC LIMIT :limit OFFSET :offset")
    fun getPagingRecipeSearch(limit: Int, offset: Int, query : String): List<LocalRecipe>

    @Query("SELECT * FROM recipes WHERE isFavorite=true ORDER BY title ASC LIMIT :limit OFFSET :offset")
    suspend fun getPagingRecipeFavorite(limit: Int, offset: Int): List<LocalRecipe>

}