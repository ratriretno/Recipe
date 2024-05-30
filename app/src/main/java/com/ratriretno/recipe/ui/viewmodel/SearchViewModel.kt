package com.ratriretno.recipe.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ratriretno.recipe.data.local.LocalRecipe
import com.ratriretno.recipe.data.local.RecipeRepository
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
class SearchViewModel @Inject constructor(
    private val repository: RecipeRepository,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher
)  : ViewModel() {

    private val emptySearchQuery = ""
    private val _searchQuery = MutableStateFlow(emptySearchQuery)
    val searchQuery : StateFlow<String> = _searchQuery

    private  val _isSearchActive = MutableStateFlow(true)
    val isSearchActive : StateFlow<Boolean> = _isSearchActive

    private val emptyList = emptyList<LocalRecipe>()

    private val _recipesList =
        MutableStateFlow<MutableList<LocalRecipe>>(mutableListOf())
    val recipesList: StateFlow<List<LocalRecipe>>
        get() = _recipesList.asStateFlow()

    private val _pagingState =
        MutableStateFlow<PaginationState>(PaginationState.EMPTY)
    val pagingState: StateFlow<PaginationState>
        get() = _pagingState.asStateFlow()

    private var page = HomeViewModel.INITIAL_PAGE
    var canPaginate by mutableStateOf(false)


    //loading first page
    private val _isLoadingFirstPage = MutableStateFlow(false)
    val isLoadingFirstPage : StateFlow<Boolean> = _isLoadingFirstPage

    fun getNewRecipe(){
        clearPaging()
        _recipesList.value.clear()
        if (page== INITIAL_PAGE){
            updateLoadingFirstPage(true)
        }
        getRecipesPaging()
        updateLoadingFirstPage(false)
        activeSearch(false)

    }

    fun getRecipesPaging() {
        Log.d("page", page.toString())

        if (page == INITIAL_PAGE || (page != INITIAL_PAGE && canPaginate) && _pagingState.value == PaginationState.REQUEST_INACTIVE) {
            _pagingState.update {
                if (page == INITIAL_PAGE) PaginationState.LOADING else PaginationState.PAGINATING }
        }

        Log.d("page", page.toString())


        viewModelScope.launch(dispatcher) {
            try {
                val result = repository.getPagingRecipeSearch(PAGE_SIZE, page * PAGE_SIZE, searchQuery.value)

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

    fun clearPaging() {
        page = INITIAL_PAGE
        _pagingState.update { PaginationState.LOADING }
        canPaginate = false
        _recipesList.value.clear()
    }

    companion object {
        const val PAGE_SIZE = 5
        const val INITIAL_PAGE = 0
    }

    fun updateSearchQuery (query : String){
        _searchQuery.update { query }
    }

    fun emptySearchQuery (){
        _searchQuery.update {emptySearchQuery }
    }

    fun activeSearch (status: Boolean){
        _isSearchActive.update { status }
    }

    fun updateLoadingFirstPage (status : Boolean){
        _isLoadingFirstPage.update {status}
    }
}