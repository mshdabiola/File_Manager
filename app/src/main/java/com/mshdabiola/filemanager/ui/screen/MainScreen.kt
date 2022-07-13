package com.mshdabiola.filemanager.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mshdabiola.filemanager.main.MainUiState

@Composable
fun MainScreen(mainUiState: State<MainUiState>) {
    MainContent(mainUiState.value)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(mainUiState: MainUiState = MainUiState("File Manager")) {
    Scaffold(
        topBar = {
            SmallTopAppBar (
                title = {
                        Text(text = mainUiState.name)
                },
                actions = {}
                )
        }
    ) {
        Column(
            modifier = Modifier
            .padding(it)
        ) {

        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun MainContentPreview() {
    MainContent()
}