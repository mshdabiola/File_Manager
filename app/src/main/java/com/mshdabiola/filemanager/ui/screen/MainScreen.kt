package com.mshdabiola.filemanager.ui.screen

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.mshdabiola.filemanager.main.MainUiState

@Composable
fun MainScreen(mainUiState: State<MainUiState>) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = mainUiState.value.onPermissionRequest
    )

    MainContent(
        mainUiState.value,
    laucher = {
        launcher.launch(it)
    }
        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(mainUiState: MainUiState = MainUiState("File Manager"),
                laucher : (String)->Unit={}

                ) {
    Scaffold(
        topBar = {
            SmallTopAppBar (
                title = {
                        Text(text = mainUiState.name)
                },
                actions = {},
                modifier = Modifier
                )
        }
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
            .padding(it)
        ) {
            if (mainUiState.askPermission){

                Button(onClick = {
                    laucher.invoke(Manifest.permission.READ_EXTERNAL_STORAGE)
                },
                    modifier = Modifier.align(Alignment.Center)


                ) {
                    Text(text = "Ask Permission")

                }
            }


        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun MainContentPreview() {
    MainContent()
}