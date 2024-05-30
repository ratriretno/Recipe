package com.ratriretno.recipe.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ratriretno.recipe.R
import com.ratriretno.recipe.data.local.LocalRecipe
import com.ratriretno.recipe.ui.viewmodel.DetailViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    viewModel : DetailViewModel = hiltViewModel(),
    scope: CoroutineScope = rememberCoroutineScope()
    ) {

   val emptyRecipe = LocalRecipe(0, "", "", "", "", "", 0, 0, 0,"", false, url = "")

    val recipe by viewModel.getRecipe().collectAsState(initial = emptyRecipe)

    Log.d("recipe", recipe.toString())

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()


//    Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    Scaffold(modifier = Modifier,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Detail")
                },

                navigationIcon = {

                    IconButton(onClick =  {}
                    ) {
                        Icon(Icons.Filled.ArrowBack, stringResource(id = R.string.back))
                    }
                },

                actions = {
                    MoreTasksMenu(recipe, viewModel, scope  )
                }

            )
        }
    ) {
        it
        Column(modifier = Modifier.padding(it).fillMaxSize().verticalScroll(rememberScrollState())) {
            Card(
                modifier = Modifier.padding(10.dp),
                shape = RoundedCornerShape(8.dp)
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

                    Text(
                        text = recipe.description,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.padding(15.dp)
                    )
                }
            }

            Card(
                modifier = Modifier.padding(10.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Ingredients",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start
                    )

                    Text(
                        text = recipe.ingredients,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.padding(15.dp)
                    )
                }
            }

            Card(
                modifier = Modifier.padding(10.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Directions",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start
                    )

                    Text(
                        text = recipe.directions,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.padding(15.dp)
                    )
                }
            }

        }

    }
}

@Composable
private fun MoreTasksMenu(
    localRecipe: LocalRecipe,
    viewModel: DetailViewModel,
    scope: CoroutineScope,
) {

        Row {
            IconButton(onClick = {scope.launch{viewModel.updateRecipe(localRecipe)}}) {
                val icon : ImageVector
                if (localRecipe.isFavorite){
                    icon = Icons.Filled.Favorite
                } else{
                   icon =  Icons.Filled.FavoriteBorder
                }

                Icon(icon, stringResource(id = R.string.share))
            }

            IconButton(onClick = { }) {
                Icon(Icons.Filled.Share, stringResource(id = R.string.share))
            }

        }
}