package com.mshdabiola.filemanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mshdabiola.filemanager.main.MainUiState
import com.mshdabiola.filemanager.ui.screen.MainScreen

@Composable
fun ManagerNavHost(navHostController: NavHostController,mainUiState: State<MainUiState>) {
    
    NavHost(navController = navHostController,
        startDestination = "main",
        modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal+ WindowInsetsSides.Vertical))
        ){
        composable("main"){
            MainScreen(mainUiState = mainUiState)
        }
    }
    
}