package com.ratriretno.recipe.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ratriretno.recipe.R
import com.ratriretno.recipe.data.local.LocalRecipe
import com.ratriretno.recipe.ui.viewmodel.HomeViewModel
import com.ratriretno.recipe.ui.viewmodel.PaginationState
import kotlinx.coroutines.CoroutineScope

@Composable
fun FavoriteScreen (
    onListClick: (LocalRecipe) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
    scope: CoroutineScope = rememberCoroutineScope(),
) {

    val nestedScrollInterop = rememberNestedScrollInteropConnection()

    val isLoadingFirstPage by viewModel.isLoadingFirstPage.collectAsState()


    val lazyColumnListState = rememberLazyListState()

    val recipeList = viewModel.recipesList.collectAsStateWithLifecycle()
    val pagingState = viewModel.pagingState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.clearPaging()
        viewModel.getRecipesPagingFavorite()
    }

    val shouldPaginate = remember {
        derivedStateOf {
            viewModel.canPaginate && (
                    lazyColumnListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                        ?: -5
                    ) >= (lazyColumnListState.layoutInfo.totalItemsCount - 3)
        }
    }

    Log.d("LaunchedEffect", shouldPaginate.value.toString() )

    LaunchedEffect(key1 = shouldPaginate.value) {
        Log.d("LaunchedEffect", "launch" )
        if (shouldPaginate.value && pagingState.value == PaginationState.REQUEST_INACTIVE) {
            Log.d("LaunchedEffect", "launch" )
            viewModel.getRecipesPagingFavorite()
        }
    }

    ScrollContent(
        recipeList,
        nestedScrollInterop,
        isLoadingFirstPage,
        onListClick,
        viewModel,
        lazyColumnListState,
        shouldPaginate,
        pagingState)

}
