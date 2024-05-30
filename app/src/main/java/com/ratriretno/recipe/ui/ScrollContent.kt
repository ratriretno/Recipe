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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ratriretno.recipe.R
import com.ratriretno.recipe.data.local.LocalRecipe
import com.ratriretno.recipe.ui.viewmodel.HomeViewModel
import com.ratriretno.recipe.ui.viewmodel.PaginationState

@Composable
fun ScrollContent(
    list: State<List<LocalRecipe>>,
    nestedScrollInterop: NestedScrollConnection,
    isLoadingFirstPage: Boolean,
    onListClick: (LocalRecipe) -> Unit,
    viewModel: HomeViewModel,
    lazyColumnListState: LazyListState,
    shouldPaginate: State<Boolean>,
    pagingState: State<PaginationState>
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {

        if (isLoadingFirstPage){
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
                    ItemRecipe(list.value[it],
                        modifier = Modifier
                            .clickable {
//                                            viewModel.insertRecipe(item)
                                onListClick(list.value[it])
                            }
                            .padding(10.dp))
                }

                when (pagingState.value) {
                    PaginationState.REQUEST_INACTIVE -> {
                    }

                    PaginationState.LOADING -> {
//                        item {
//                            smallLoading()
//                        }
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

@Composable
fun indeterminateCircularIndicator() {
//    if (!status) return

    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center){
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}

@Composable
fun smallLoading() {
//    if (!status) return

    Column(modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally){
        CircularProgressIndicator(
            modifier = Modifier.width(30.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}
