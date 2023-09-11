package com.example.newsroomapp.util

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.newsroomapp.ui.theme.article_screen.ArticleScreen
import com.example.newsroomapp.ui.theme.news_screen.NewsScreen
import com.example.newsroomapp.ui.theme.news_screen.NewsScreenViewmodel

@Composable
fun NavGraphSetup(
    navHostController: NavHostController
) {
    val argKey ="web_url"
    NavHost(
        navController = navHostController,
        startDestination = "news_Screen"
    ) {
        composable(route = "news_Screen") {
            val viewmodel: NewsScreenViewmodel = hiltViewModel()
            NewsScreen(
                state = viewmodel.state,
                onEvent = viewmodel::onEvent,
                onReadFullStoryButtonClicked = { url ->
                    navHostController.navigate("article_screen?$argKey= $url")

                }
            )
        }

        composable(
            route = "article_screen?$argKey={$argKey}",
            arguments = listOf(navArgument(name = argKey) {
                type = NavType.StringType

            })
        ) { backStackEntry ->
            ArticleScreen(
                url = backStackEntry.arguments?.getString(argKey),
                onBackPressed = {
                    navHostController.navigateUp()

                }
            )
        }
    }
}