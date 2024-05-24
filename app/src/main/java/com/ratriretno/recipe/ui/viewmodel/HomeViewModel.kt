package com.ratriretno.recipe.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khush.newsapp.common.logger.Logger
import com.khush.newsapp.common.networkhelper.NetworkHelper
import com.ratriretno.recipe.data.model.ApiRecipe
import com.ratriretno.recipe.data.network.ApiInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val logger: Logger,
    private val network: ApiInterface,
    private val networkHelper: NetworkHelper

) : ViewModel() {

    private val emptyList = emptyList<ApiRecipe>()
    private val _currentListStream = MutableStateFlow(emptyList)
    val currentListStream: StateFlow<List<ApiRecipe>> = _currentListStream

    private val _status = MutableStateFlow(false)
    val status : StateFlow<Boolean> = _status

    init {
        getRecipes()
    }

    private fun getRecipes (){
        updateLoading(true)
        viewModelScope.launch {

            kotlin.runCatching {
                val recipes = network.getRecipes()
                updateList(recipes)

            }.onSuccess {
                updateLoading(false)
                logger.d("NewsViewModel", it.toString())
            }.onFailure {
                logger.d("fail", it.toString())
                updateLoading(false)
            }
        }
    }

    private fun updateList (recipes: List<ApiRecipe>){
        _currentListStream.update { recipes }
    }

    private fun updateLoading (loading : Boolean){
        _status.update { loading }
    }

}
