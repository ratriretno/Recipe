package com.ratriretno.recipe.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ratriretno.recipe.R
import com.ratriretno.recipe.data.model.ApiRecipe
import com.ratriretno.recipe.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    onListClick: (ApiRecipe) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val list by viewModel.currentListStream.collectAsState()

    val nestedScrollInterop = rememberNestedScrollInteropConnection()

   val status by viewModel.status.collectAsState()

    ScrollContent(list, nestedScrollInterop, status, onListClick)


}

@Composable
fun ScrollContent(
    list: List<ApiRecipe>,
    nestedScrollInterop: NestedScrollConnection,
    status: Boolean,
    onListClick: (ApiRecipe) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        if (status){
            indeterminateCircularIndicator()
        } else{
            LazyColumn(
                Modifier
                    .nestedScroll(nestedScrollInterop)
                    .fillMaxSize()
                    .padding(10.dp)) {
                itemsIndexed(list) { index, item ->
                    if (item.description!=""){
                        ItemRecipe(item, modifier = Modifier.clickable { onListClick(item)}.padding(10.dp))
                    }


                }
            }
        }

    }
}

@Composable
private fun ItemRecipe (apiRecipe: ApiRecipe, modifier: Modifier){
    Column {
        Card(
            modifier = modifier,
            shape = RoundedCornerShape(8.dp),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = apiRecipe.title,
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
                        .data(apiRecipe.photoUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    error = painterResource(id = R.drawable.ic_broken_image),
                    placeholder = painterResource(id = R.drawable.loading_img)
                )
                val description : String
                if (apiRecipe.description.length>150){
                    description=apiRecipe.description.substring(0,120)
                } else {
                    description = apiRecipe.description
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




