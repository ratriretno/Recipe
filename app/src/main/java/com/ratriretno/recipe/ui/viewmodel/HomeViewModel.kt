package com.ratriretno.recipe.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khush.newsapp.common.networkhelper.NetworkHelper
import com.ratriretno.recipe.data.local.LocalRecipe
import com.ratriretno.recipe.data.local.RecipeRepository
import com.ratriretno.recipe.data.model.ApiRecipe
import com.ratriretno.recipe.data.network.ApiInterface
import com.ratriretno.recipe.di.DefaultDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val network: ApiInterface,
    private val networkHelper: NetworkHelper,
    private val repository: RecipeRepository,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher

    ) : ViewModel() {


    //loading first page
    private val _isLoadingFirstPage = MutableStateFlow(false)
    val isLoadingFirstPage : StateFlow<Boolean> = _isLoadingFirstPage

    private val _recipesList =
        MutableStateFlow<MutableList<LocalRecipe>>(mutableListOf())
    val recipesList: StateFlow<List<LocalRecipe>>
        get() = _recipesList.asStateFlow()

    private val _pagingState =
        MutableStateFlow<PaginationState>(PaginationState.LOADING)
    val pagingState: StateFlow<PaginationState>
        get() = _pagingState.asStateFlow()

    private var page = INITIAL_PAGE
    var canPaginate by mutableStateOf(false)


    fun getData(){
        viewModelScope.launch {
            if (repository.getAll().isEmpty()){
                Log.d("repository.getAll()", repository.getAll().toString() )
                getRecipes()
            } else{
                Log.d("repository.getAll()", repository.getAll().size.toString() )
                clearPaging()
                getRecipesPaging()
            }
        }
    }

    private fun getRecipes (){
       updateLoadingFirstPage(true)
        viewModelScope.launch {
            kotlin.runCatching {
              val recipes = network.getRecipes()
              insertAllRecipe(recipes)
                getRecipesPaging()
            }.onSuccess {
                updateLoadingFirstPage(false)
            }.onFailure {
                updateLoadingFirstPage(false)
            }
        }
    }


    private fun updateLoadingFirstPage (loading : Boolean){
        _isLoadingFirstPage.update { loading }
    }

    fun insertRecipe (apiRecipe: ApiRecipe){
        Log.d("insert recipe", apiRecipe.toString())
        val recipes = toLocalRecipe(apiRecipe)

        viewModelScope.launch {
            repository.insert(recipes)
        }
    }

   private suspend fun insertAllRecipe(recipelist : List<ApiRecipe>){
        viewModelScope.launch {
            var localRecipeList = emptyList<LocalRecipe>()
            recipelist.forEach { item ->
                val localRecipe = toLocalRecipe(item)
//                if (localRecipe.title!=""&& localRecipe.photoUrl!=""){
//                    localRecipeList = localRecipeList + toLocalRecipe(item)
//                }

                localRecipeList = localRecipeList + toLocalRecipe(item)

            }
            repository.insertList(localRecipeList)
        }
    }

    private fun toLocalRecipe(apiRecipe: ApiRecipe): LocalRecipe {
        return LocalRecipe(
            id = apiRecipe.id,
            title = apiRecipe.title,
            description = apiRecipe.description,
            photoUrl = apiRecipe.photoUrl,
            mainIngredient = apiRecipe.mainIngredient,
            ingredients = apiRecipe.ingredients,
            prepTime = apiRecipe.prepTime,
            cookTime = apiRecipe.cookTime,
            totalTime = apiRecipe.totalTime,
            directions = apiRecipe.directions,
            isFavorite = false,
            url = apiRecipe.url
        )
    }

    fun getRecipesPaging() {
        Log.d("page", page.toString())
        if (page== INITIAL_PAGE){
            updateLoadingFirstPage(true)
        } else{
            updateLoadingFirstPage(false)
        }

        if (page == INITIAL_PAGE || (page != INITIAL_PAGE && canPaginate) && _pagingState.value == PaginationState.REQUEST_INACTIVE) {
            _pagingState.update {
                if (page == INITIAL_PAGE) PaginationState.LOADING else PaginationState.PAGINATING }
        }

        Log.d("page", page.toString())


        viewModelScope.launch(dispatcher) {
            try {
                val result = repository.getPagingRecipe(PAGE_SIZE, page * PAGE_SIZE)

                canPaginate = result.size == PAGE_SIZE

                updateLoadingFirstPage(false)

                if (page == INITIAL_PAGE) {
                    if (result.isEmpty()) {
                        _pagingState.update { PaginationState.EMPTY }
                        return@launch
                    }
                    _recipesList.value.clear()
                    _recipesList.value.addAll(result)
                } else {
                    _recipesList.value.addAll(result)
                }

                _pagingState.update { PaginationState.REQUEST_INACTIVE }

                if (canPaginate) {
                    page++
                }

                if (!canPaginate) {
                    _pagingState.update { PaginationState.PAGINATION_EXHAUST }
                }
            } catch (e: Exception) {
                _pagingState.update { if (page == INITIAL_PAGE) PaginationState.ERROR else PaginationState.PAGINATION_EXHAUST }
            }
        }
    }

    fun clearPaging() {
        page = INITIAL_PAGE
        _pagingState.update { PaginationState.LOADING }
        canPaginate = false
    }

    companion object {
        const val PAGE_SIZE = 5
        const val INITIAL_PAGE = 0
    }

    fun getRecipesPagingFavorite() {
        Log.d("page", page.toString())

        if (page == INITIAL_PAGE || (page != INITIAL_PAGE && canPaginate) && _pagingState.value == PaginationState.REQUEST_INACTIVE) {
            _pagingState.update { if (page == INITIAL_PAGE) PaginationState.LOADING else PaginationState.PAGINATING }
        }

        Log.d("page", page.toString())


        viewModelScope.launch(dispatcher) {
            try {
                val result = repository.getPagingRecipeFavorite(PAGE_SIZE, page * PAGE_SIZE)

                canPaginate = result.size == PAGE_SIZE

                if (page == INITIAL_PAGE) {
                    if (result.isEmpty()) {
                        _pagingState.update { PaginationState.EMPTY }
                        return@launch
                    }
                    _recipesList.value.clear()
                    _recipesList.value.addAll(result)
                } else {
                    _recipesList.value.addAll(result)
                }

                _pagingState.update { PaginationState.REQUEST_INACTIVE }

                if (canPaginate) {
                    page++
                }

                if (!canPaginate) {
                    _pagingState.update { PaginationState.PAGINATION_EXHAUST }
                }
            } catch (e: Exception) {
                _pagingState.update { if (page == INITIAL_PAGE) PaginationState.ERROR else PaginationState.PAGINATION_EXHAUST }
            }
        }
    }
}


