package com.mshdabiola.filemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.mshdabiola.filemanager.ui.navigation.ManagerNavHost
import com.mshdabiola.filemanager.ui.theme.FileManagerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainViewModel by viewModels<MainViewModel>()


        setContent {
            val mainUiState by mainViewModel.fileManagerUiState.collectAsState()
            val homeUiState by mainViewModel.mainUiState.collectAsState()
            val selectUiState by mainViewModel.selectUiState.collectAsState()
            val navHostController = rememberNavController()
            FileManagerTheme {
                ManagerNavHost(
                    navHostController = navHostController,
                    fileManagerUiState = mainUiState,
                    mainUiState = homeUiState,
                    selectUiState = selectUiState
                )
                // A surface container using the 'background' color from the theme

            }
        }
    }
}
