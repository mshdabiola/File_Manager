package com.mshdabiola.filemanager.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mshdabiola.filemanager.ui.screen.home.HomeUiState
import com.mshdabiola.filemanager.ui.screen.main.MainUiState

@Composable
fun ManagerNavHost(navHostController: NavHostController, mainUiState: State<MainUiState>, homeUiState: HomeUiState) {
    
    NavHost(navController = navHostController,
        startDestination = "home",
        modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal+ WindowInsetsSides.Vertical))
        ){
        composable("main"){

            LaunchedEffect(key1 = it.arguments, block = {
                mainUiState.value.getAllFiles("")
            })
            MainScreen(mainUiState = mainUiState,navHostController)
        }

        composable("second/{file}"){

            val name by remember {
                mutableStateOf(it.arguments?.getString("file"))
            }



            LaunchedEffect(key1 = name, block = {
                name?.let { it1 -> mainUiState.value.getAllFiles(it1.replace("*","/")) }
            })

            MainScreen(mainUiState = mainUiState,navHostController)
        }

        composable("home"){
            HomeScreen(navController = navHostController, homeUiState = homeUiState)
        }
    }
    
}