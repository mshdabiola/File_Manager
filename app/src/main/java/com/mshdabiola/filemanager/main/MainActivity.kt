package com.mshdabiola.filemanager.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.mshdabiola.filemanager.ui.ManagerNavHost
import com.mshdabiola.filemanager.ui.theme.FileManagerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainViewModel by viewModels<MainViewModel>()

        setContent {
            val mainUiState = mainViewModel.uistate.collectAsState()
            val navHostController = rememberNavController()
            FileManagerTheme {
                ManagerNavHost(navHostController = navHostController, mainUiState = mainUiState)
                // A surface container using the 'background' color from the theme

            }
        }
    }
}
