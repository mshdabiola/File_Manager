package com.mshdabiola.filemanager.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mshdabiola.filemanager.data.model.FileManagerUiState
import com.mshdabiola.filemanager.data.model.MainUiState
import com.mshdabiola.filemanager.data.model.SelectUiState
import com.mshdabiola.filemanager.ui.screen.*

@Composable
fun ManagerNavHost(
    navHostController: NavHostController,
    fileManagerUiState: FileManagerUiState,
    mainUiState: MainUiState,
    selectUiState: SelectUiState
) {

    NavHost(
        navController = navHostController,
        startDestination = "splash",
        modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Vertical))
    ) {


        composable("second/{file}") {

            val name by remember {
                mutableStateOf(it.arguments?.getString("file"))
            }



            LaunchedEffect(key1 = name, block = {
                name?.let { it1 -> fileManagerUiState.getAllFiles(it1.replace("*", "/")) }
            })

            FileManagerScreen(mainUiState = fileManagerUiState, navHostController)
        }

        composable("select/{path}") {
            val path by remember {
                mutableStateOf(it.arguments?.getString("path"))
            }

            LaunchedEffect(key1 = path, block = {
                path?.let {
                    selectUiState.getAllDirectory(it.replace("*", "/"))
                }
            })

            SelectScreen(navController = navHostController, selectUiState = selectUiState)

        }

        composable("home") {
            MainScreen(navController = navHostController, mainUiState = mainUiState)
        }

        composable(route = "fileinfo") {
            FileInfoScreen(
                navController = navHostController,
                fileUiState = fileManagerUiState.currentFileUiState
            )
        }

        composable(route = "splash") {
            SplashScreen(navHostController)
        }
        composable(route = "onboarding") {
            OnBoardingScreen(navHostController)
        }

    }

}