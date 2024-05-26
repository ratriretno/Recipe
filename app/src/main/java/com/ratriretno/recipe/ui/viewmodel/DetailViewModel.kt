package com.ratriretno.recipe.ui.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ratriretno.recipe.data.local.LocalRecipe
import com.ratriretno.recipe.data.local.RecipeRepository
import com.ratriretno.recipe.nav.RecipeDestinationsArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val state: SavedStateHandle
) : ViewModel (){

    fun getRecipe () : Flow<LocalRecipe> {
        val id: String = state[RecipeDestinationsArgs.RECIPE_ID_ARG]!!
        return recipeRepository.getRecipe(id)
    }

    suspend fun updateRecipe (localRecipe: LocalRecipe) {
        val updateRecipe =localRecipe.copy()
        updateRecipe.isFavorite = !localRecipe.isFavorite
        recipeRepository.update(updateRecipe)
    }
}