package com.ratriretno.recipe.nav

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.ratriretno.recipe.R
import com.ratriretno.recipe.data.local.LocalRecipe
import com.ratriretno.recipe.nav.RecipeDestinationsArgs.RECIPE_ID_ARG
import com.ratriretno.recipe.nav.RecipeScreens.DETAIL_SCREEN
import com.ratriretno.recipe.nav.RecipeScreens.FAVORITE_SCREEN
import com.ratriretno.recipe.nav.RecipeScreens.HOME_SCREEN
import com.ratriretno.recipe.nav.RecipeScreens.RECIPE_SCREEN

/**
 * Screens used in [TodoDestinations]
 */
private object RecipeScreens {
    const val RECIPE_SCREEN = "recipe"
    const val HOME_SCREEN = "home"
    const val FAVORITE_SCREEN = "favorite"
    const val DETAIL_SCREEN = "detail"
}

/**
 * Arguments used in [TodoDestinations] routes
 */
object RecipeDestinationsArgs {
    const val USER_MESSAGE_ARG = "userMessage"
    const val RECIPE_ID_ARG = "recipeId"
    const val TITLE_ARG = "title"
    const val DESCRIPTION_ARG = "description"
    const val IMAGE_ARG = "image"
}

/**
 * Destinations used in the [TodoActivity]
 */
object RecipeDestinations {
    const val RECIPE_ROUTE = RECIPE_SCREEN
    const val HOME_ROUTE = HOME_SCREEN
    const val FAVORITE_ROUTE = FAVORITE_SCREEN
    const val DETAIL_ROUTE = "$DETAIL_SCREEN/{$RECIPE_ID_ARG}"
//    const val ADD_EDIT_TASK_ROUTE = "$ADD_EDIT_TASK_SCREEN/{$TITLE_ARG}?$TASK_ID_ARG={$TASK_ID_ARG}"
}

/**
 * Models the navigation actions in the app.
 */
class RecipeNavigationActions(private val navController: NavHostController) {

    fun navigateToHome() {
        navController.navigate(HOME_SCREEN)
    }

    fun navigateFavorite() {
        navController.navigate(FAVORITE_SCREEN)
    }

//    fun navigateToStatistics() {
//        navController.navigate(TodoDestinations.STATISTICS_ROUTE) {
//            // Pop up to the start destination of the graph to
//            // avoid building up a large stack of destinations
//            // on the back stack as users select items
//            popUpTo(navController.graph.findStartDestination().id) {
//                saveState = true
//            }
//            // Avoid multiple copies of the same destination when
//            // reselecting the same item
//            launchSingleTop = true
//            // Restore state when reselecting a previously selected item
//            restoreState = true
//        }
//    }

    fun navigateToDetail(recipe: LocalRecipe) {
        navController.navigate("$DETAIL_SCREEN/${recipe.id}")
    }

    fun navigateSingleTopTo(
        route: String,
        navController: NavHostController
    ) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id)
            launchSingleTop = true
        }
    }

//    fun navigateToAddEditTask(title: Int, taskId: String?) {
//        navController.navigate(
//            "$ADD_EDIT_TASK_SCREEN/$title".let {
//                if (taskId != null) "$it?$TASK_ID_ARG=$taskId" else it
//            }
//        )
//    }
}

sealed class Route(
    val route: String,
    @StringRes val resourceId: Int,
    val icon: ImageVector,
    val routeWithoutArgs: String = route
) {

    object Home : Route(RecipeDestinations.HOME_ROUTE, R.string.home, Icons.Filled.Home)
    object Favorite : Route(RecipeDestinations.FAVORITE_ROUTE, R.string.saved, Icons.Filled.Favorite)
}

val bottomBarScreens = listOf(
    Route.Home,
    Route.Favorite,
)

object NavigationUtil {

    fun navigateSingleTopTo(
        route: String,
        navController: NavHostController
    ) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id)
            launchSingleTop = true
        }
    }
    }
