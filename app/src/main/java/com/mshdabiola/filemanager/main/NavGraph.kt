package com.mshdabiola.filemanager.ui

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mshdabiola.filemanager.main.MainUiState
import com.mshdabiola.filemanager.ui.screen.MainScreen
import java.nio.file.Path

@Composable
fun ManagerNavHost(navHostController: NavHostController,mainUiState: State<MainUiState>) {
    
    NavHost(navController = navHostController,
        startDestination = "main",
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
    }
    
}