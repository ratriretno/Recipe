package com.ratriretno.recipe.nav

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ratriretno.recipe.data.model.ApiRecipe
import com.ratriretno.recipe.nav.RecipeDestinationsArgs.RECIPE_ID_ARG
import com.ratriretno.recipe.nav.RecipeDestinationsArgs.TITLE_ARG
import com.ratriretno.recipe.ui.DetailScreen
import com.ratriretno.recipe.ui.HomeScreen

@Composable
fun RecipeNavGraph (
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
    startDestination: String = RecipeDestinations.RECIPE_ROUTE,
    navActions: RecipeNavigationActions = remember(navController) {
        RecipeNavigationActions(navController)
    }
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = startDestination,
        ) {
            Recipe(navActions)
        }

        composable(route = RecipeDestinations.DETAIL_ROUTE,
                arguments = listOf(
                navArgument(RECIPE_ID_ARG) { type = NavType.IntType }
        )){
                entry ->
                val id = entry.arguments?.getInt(RECIPE_ID_ARG)!!
                DetailScreen(id)
        }

    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Recipe(
    navActions: RecipeNavigationActions
) {
    Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Text("Recipe")
                    }
                )
            },
            bottomBar = {
                RecipeBottomNavigation()
            }
        ) {
        it
            RecipeNavHost(
                navActions=navActions,
                modifier = Modifier.padding(it)
            )
        }
}

@Composable
private fun RecipeNavHost(
    navActions: RecipeNavigationActions,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    navActionsRecipe: RecipeNavigationActions = remember(navController) {
        RecipeNavigationActions(navController)
    }
)
    {

    NavHost(
        navController = navController,
        startDestination = RecipeDestinations.HOME_ROUTE,
        modifier = modifier
    ) {
        composable(route = RecipeDestinations.HOME_ROUTE) {
            Log.d("route", RecipeDestinations.HOME_ROUTE)
            HomeScreen(
                onListClick = { list -> navActions.navigateToDetail(list) }
            )
        }

//        composable(route = RecipeDestinations.DETAIL_ROUTE,
//            arguments = listOf(
//                navArgument(RECIPE_ID_ARG) { type = NavType.IntType },
//                navArgument(TITLE_ARG) { type = NavType.StringType }
//            )){
//                entry ->
//            val id = entry.arguments?.getInt(RECIPE_ID_ARG)!!
//            val title = entry.arguments?.getInt(TITLE_ARG)!!
//            DetailScreen(id)
//        }

//        composable(RecipeDestinations.DETAIL_ROUTE ){
//            DetailScreen(
////                onBack = { navController.popBackStack() }
////                onDeleteTask = { navActions.navigateToTasks(DELETE_RESULT_OK) }
//            )
//        }

    }
}

// Keys for navigation
const val ADD_EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 1
const val DELETE_RESULT_OK = Activity.RESULT_FIRST_USER + 2
const val EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 3


@Composable
fun RecipeBottomNavigation(
) {
    NavigationBar {

        NavigationBarItem(
            selected = true,
            label = {
                Text(text = "HOME")
            },
            icon = {
                Icon(
                    Icons.Filled.Home,
                    contentDescription = "Localized description",
                )
            },
            onClick = {}
        )

        NavigationBarItem(
            selected = false,
            label = {
                Text(text = "SAVE")
            },
            icon = {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = "Localized description",
                )
            },
            onClick = {}
        )

        NavigationBarItem(
            selected = false,
            label = {
                Text(text = "Search")
            },
            icon = {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = "Localized description",
                )
            },
            onClick = {}
        )

    }
}


