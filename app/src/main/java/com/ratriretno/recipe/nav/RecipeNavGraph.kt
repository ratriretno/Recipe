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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ratriretno.recipe.nav.NavigationUtil.navigateSingleTopTo
import com.ratriretno.recipe.nav.RecipeDestinationsArgs.RECIPE_ID_ARG
import com.ratriretno.recipe.ui.DetailScreen
import com.ratriretno.recipe.ui.FavoriteScreen
import com.ratriretno.recipe.ui.HomeScreen
import com.ratriretno.recipe.ui.SearchScreen

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
                navArgument(RECIPE_ID_ARG) { type = NavType.StringType }
        )){
                DetailScreen(navigateBack = { navController.popBackStack() })
        }

        composable(route = RecipeDestinations.SEARCH_ROUTE){
            SearchScreen (
                onListClick = { recipe -> navActions.navigateToDetail(recipe) },
                navigateBack = {navController.popBackStack()}
                )
        }

    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Recipe(
    navActions: RecipeNavigationActions,
    navController: NavHostController = rememberNavController(),
    navActionsRecipe: RecipeNavigationActions = remember(navController) {
        RecipeNavigationActions(navController)},

) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentScreen = bottomBarScreens.find { it.route == currentDestination?.route } ?: Route.Home

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
//                RecipeBottomNavigation(navActionsRecipe)
                BottomNavigation(currentScreen = currentScreen, navActions= navActions) {
                    navigateSingleTopTo(it.route, navController)
                }
            }
        ) {
        it

        NavHost(
            navController = navController,
            startDestination = RecipeDestinations.HOME_ROUTE,
            modifier = Modifier.padding(it),
        ) {
            composable(route = RecipeDestinations.HOME_ROUTE) {
                Log.d("route", RecipeDestinations.HOME_ROUTE)
                HomeScreen(
                    onListClick = { recipe -> navActions.navigateToDetail(recipe) }
                )
            }

            composable(route = RecipeDestinations.FAVORITE_ROUTE) {
                Log.d("route", RecipeDestinations.FAVORITE_ROUTE)
                FavoriteScreen(
                    onListClick = { recipe -> navActions.navigateToDetail(recipe) }
                )
            }

            composable(route = RecipeDestinations.SEARCH_ROUTE) {
                SearchScreen(
                    onListClick = { recipe -> navActions.navigateToDetail(recipe) },
                    navigateBack ={ navController.popBackStack() }
                )
            }
        }
}
}

// Keys for navigation
const val ADD_EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 1
const val DELETE_RESULT_OK = Activity.RESULT_FIRST_USER + 2
const val EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 3


@Composable
fun RecipeBottomNavigation(navActionsRecipe: RecipeNavigationActions) {
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
            onClick = {navActionsRecipe.navigateToHome()}
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
            onClick = {navActionsRecipe.navigateFavorite()}
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

@Composable
fun BottomNavigation(
    currentScreen: Route,
    navActions: RecipeNavigationActions,
    onIconSelected: (Route) -> Unit
) {
    NavigationBar {
        bottomBarScreens.forEach { screen ->
            if (screen.route!=Route.Search.route){
                NavigationBarItem(
                    selected = screen == currentScreen,
                    label = {
                        Text(text = stringResource(id = screen.resourceId))
                    },
                    icon = {
                        Icon(screen.icon, null)
                    },
                    onClick = {
                        onIconSelected.invoke(screen)
                    }
                ) } else{
                    NavigationBarItem(
                        selected = screen == currentScreen,
                        label = {
                            Text(text = stringResource(id = screen.resourceId))
                        },
                        icon = {
                            Icon(screen.icon, null)
                        },
                        onClick = {
                            navActions.navigateSearch()
                        }
                    )
                }
            }

        }
}


