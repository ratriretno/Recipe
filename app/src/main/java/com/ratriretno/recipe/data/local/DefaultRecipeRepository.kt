package com.ratriretno.recipe.data.local

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

    override fun getAllFavoriteStream(): Flow<List<LocalRecipe>> {
        return localDataSource.observeAllFavorite()
    }


    override suspend fun insertList(list: List<LocalRecipe>) {
        TODO("Not yet implemented")
    }

    override suspend fun update(localRecipe: LocalRecipe) {
        TODO("Not yet implemented")
    }

    override fun getRecipe(id: String): LocalRecipe {
        TODO("Not yet implemented")
    }
}