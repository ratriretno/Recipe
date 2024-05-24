package com.ratriretno.recipe.data.repository

import com.ratriretno.recipe.data.model.ApiRecipe
import com.ratriretno.recipe.data.network.ApiInterface
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RecipeRepository @Inject constructor(
//    private val database: DatabaseService,
    private val network: ApiInterface
) {

    suspend fun getRecipes(): List<ApiRecipe> {
        return network.getRecipes()
    }

}