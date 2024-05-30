package com.ratriretno.recipe.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ratriretno.recipe.R
import com.ratriretno.recipe.data.local.LocalRecipe
import com.ratriretno.recipe.ui.viewmodel.HomeViewModel
import com.ratriretno.recipe.ui.viewmodel.PaginationState
import com.ratriretno.recipe.ui.viewmodel.SearchViewModel


@Composable
fun SearchScreen(
    onListClick: (LocalRecipe) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()) {

    val searchQuery by viewModel.searchQuery.collectAsState()

    val nestedScrollInterop = rememberNestedScrollInteropConnection()

    val isLoadingFirstPage by viewModel.isLoadingFirstPage.collectAsState()

    val isSearchActive by viewModel.isSearchActive.collectAsState()
    val focusRequester = remember { FocusRequester() }



    val lazyColumnListState = rememberLazyListState()

    val recipeList = viewModel.recipesList.collectAsStateWithLifecycle()
    val pagingState = viewModel.pagingState.collectAsStateWithLifecycle()

    val shouldPaginate = remember {
        derivedStateOf {
            viewModel.canPaginate && (
                    lazyColumnListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                        ?: -5
                    ) >= (lazyColumnListState.layoutInfo.totalItemsCount - 3)
        }
    }

    Log.d("init", isSearchActive.toString() )

    LaunchedEffect(isSearchActive) {
        if (isSearchActive){
            focusRequester.requestFocus()
        }
    }

    LaunchedEffect(key1 = shouldPaginate.value) {
//        viewModel.activeSearch()
        Log.d("LaunchedEffect", isSearchActive.toString() )
        if (shouldPaginate.value && pagingState.value == PaginationState.REQUEST_INACTIVE) {
            Log.d("LaunchedEffect", "launch" )
            viewModel.getRecipesPaging()
        }
    }

    Scaffold(modifier = Modifier,
        topBar = {
            CustomSearchBar(
                searchQuery,
                viewModel,
                onSearchClicked = { viewModel.getNewRecipe()},
                isSearchActive = isSearchActive,
                focusRequester = focusRequester
                )
        }
    ) {
        it
            ScrollContent(
                recipeList,
                nestedScrollInterop,
                isLoadingFirstPage,
                onListClick,
                viewModel,
                lazyColumnListState,
                shouldPaginate,
                pagingState,
                it)
    }
}

@Composable
fun CustomSearchBar(
    searchQuery: String,
    viewModel: SearchViewModel,
    modifier: Modifier = Modifier,
    height: Dp = 60.dp,
    elevation: Dp = 3.dp,
    cornerShape: Shape = RoundedCornerShape(8.dp),
    backgroundColor: Color = Color.White,
    onSearchClicked: () -> Unit = {},
    onTextChange: (String) -> Unit = {},
    isSearchActive : Boolean,
    focusRequester: FocusRequester
) {
    Log.d("CustomSearchBar", isSearchActive.toString() )

    Row(
        modifier = Modifier
            .padding(3.dp)
            .height(height)
            .fillMaxWidth()
            .shadow(elevation = elevation, shape = cornerShape)
            .background(color = backgroundColor, shape = cornerShape)
            .clickable {
                viewModel.activeSearch()
                focusRequester.requestFocus()
                       },
        verticalAlignment = Alignment.CenterVertically,
    ) {

            IconButton(onClick =  {}
            ) {
                Icon(Icons.Filled.ArrowBack, stringResource(id = R.string.back))
            }

        BasicTextField(
            modifier = modifier
                .weight(5f)
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .focusRequester(focusRequester),
            value = searchQuery,
            onValueChange = {
                    query -> viewModel.updateSearchQuery(query)
            },
            enabled = isSearchActive,
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.primary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            ),
            decorationBox = { innerTextField ->
                if (searchQuery.isEmpty()) {
                    Text(
                        text = searchQuery,
                        color = Color.Gray.copy(alpha = 0.5f),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
                innerTextField()
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                onSearchClicked() }),
            singleLine = true
        )
        Box(
            modifier = modifier
                .weight(1f)
                .size(40.dp)
                .background(color = Color.Transparent, shape = CircleShape)
                .clickable() {
                    if (searchQuery.isNotEmpty()) {
                        viewModel.emptySearchQuery()
//                        onTextChange("")
                        viewModel.activeSearch()
                    }
                },
        ) {
            if (searchQuery.isNotEmpty()) {
                Icon(Icons.Filled.Clear,
                    modifier = modifier
                        .fillMaxSize()
                        .padding(5.dp),
                    contentDescription = stringResource(R.string.search),
                    tint = MaterialTheme.colorScheme.primary,
                )
            } else {
                Icon(Icons.Filled.Search,
                    modifier = modifier
                        .fillMaxSize()
                        .padding(5.dp)
                        .clickable(enabled = false) {},
                    contentDescription = stringResource(R.string.search),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Composable
fun ScrollContent(
    list: State<List<LocalRecipe>>,
    nestedScrollInterop: NestedScrollConnection,
    isLoadingFirstPage: Boolean,
    onListClick: (LocalRecipe) -> Unit,
    viewModel: SearchViewModel,
    lazyColumnListState: LazyListState,
    shouldPaginate: State<Boolean>,
    pagingState: State<PaginationState>,
    it: PaddingValues

) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(it),
        verticalArrangement = Arrangement.Center
    ) {

        if (isLoadingFirstPage){
            Log.d("updateLoadingFirstPage(true)", isLoadingFirstPage.toString())
            indeterminateCircularIndicator()
        } else{
            LazyColumn(
                state = lazyColumnListState,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(10.dp).fillMaxSize(),
            ) {

                items(
                    list.value.size,
                    key = { list.value[it].id },
                ) {
                    Log.d("recipe", list.value[it].id.toString())
                    ItemRecipe(list.value[it],
                        modifier = Modifier
                            .clickable {
//                                            viewModel.insertRecipe(item)
                                onListClick(list.value[it])
                            }
                            .padding(10.dp))
                }

                Log.d("pagingState", pagingState.value.toString())

                when (pagingState.value) {
                    PaginationState.REQUEST_INACTIVE -> {
                    }

                    PaginationState.LOADING -> {
                        item {
                            smallLoading()
                        }
                    }

                    PaginationState.PAGINATING -> {
                        item {
                            smallLoading()
                        }
                    }

                    PaginationState.ERROR -> TODO()
                    PaginationState.PAGINATION_EXHAUST -> {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(8.dp),
                            ) {
                                Text(text = stringResource(id = R.string.record_list_end_text))
                            }
                        }
                    }

                    PaginationState.EMPTY -> {
                        item {
//                            EmptyScreen(
//                                onAddItemClick = { onAddNewRecord.invoke() },
//                                rationaleText = R.string.records_list_no_recordsFount_text,
//                            )
                        }
                    }
                }
//            LazyColumn(
//                Modifier
//                    .nestedScroll(nestedScrollInterop)
//                    .fillMaxSize()
//                    .padding(10.dp)) {
//                itemsIndexed(list) { index, item ->
//                    if (item.description!=""){
//                        ItemRecipe(item,
//                            modifier = Modifier
//                                .clickable {
//                                            viewModel.insertRecipe(item)
//                                            onListClick(item)}
//                                .padding(10.dp))
//                    }
//                }
//            }
//        }
            }

        }
    }
}


@Composable
private fun ItemRecipe (recipe: LocalRecipe, modifier: Modifier){
    Column {
        Card(
            modifier = modifier,
            shape = RoundedCornerShape(8.dp),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = recipe.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                )

                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(recipe.photoUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    error = painterResource(id = R.drawable.ic_broken_image),
                    placeholder = painterResource(id = R.drawable.loading_img)
                )
                val description : String
                if (recipe.description.length>150){
                    description=recipe.description.substring(0,120)
                } else {
                    description = recipe.description
                }

                Text(
                    text = description,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.padding(15.dp)
                )
            }
        }
    }

}
