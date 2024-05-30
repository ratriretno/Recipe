package com.ratriretno.recipe.data.local

import android.util.Log
import com.ratriretno.recipe.di.ApplicationScope
import com.ratriretno.recipe.di.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DefaultRecipeRepository @Inject constructor (
    private val localDataSource: RecipeDao,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
    @ApplicationScope private val scope: CoroutineScope,
) : RecipeRepository{

    override fun getAllStream(): Flow<List<LocalRecipe>> {
        return localDataSource.observeAll()
    }

    override suspend fun getAll(): List<LocalRecipe> {
        return localDataSource.getAll()
    }

    override fun getAllFavoriteStream(): Flow<List<LocalRecipe>> {
        return localDataSource.observeAllFavorite()
    }


    override suspend fun insertList(list: List<LocalRecipe>) {
        localDataSource.insertList(list)
    }

    override suspend fun insert(localRecipe: LocalRecipe) {
            localDataSource.insert(localRecipe)
    }

    override suspend fun update(localRecipe: LocalRecipe) {
        localDataSource.update(localRecipe)
    }

    override fun getRecipe(id: String): Flow <LocalRecipe> {
        return localDataSource.getById(id)
    }

    override suspend fun getPagingRecipe(limit: Int, offset: Int): List<LocalRecipe> {
        return localDataSource.getPagingRecipe(limit, offset)
    }

    override fun getPagingRecipeSearch(
        limit: Int,
        offset: Int,
        query: String
    ): List<LocalRecipe>{
        Log.d("RecipeSearch  limit",  limit.toString())
        Log.d("RecipeSearch  offset",  offset.toString())
        Log.d("RecipeSearch  query",  query)
        Log.d("RecipeSearch  end ",  "------------------------------")
        return  localDataSource.getPagingRecipeSearch(limit,offset, query)
    }

    override suspend fun getPagingRecipeFavorite(limit: Int, offset: Int): List<LocalRecipe> {
        return localDataSource.getPagingRecipeFavorite(limit, offset)
    }
}