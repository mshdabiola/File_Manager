package com.mshdabiola.filemanager.ui.screen

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mshdabiola.filemanager.main.FileUiState
import com.mshdabiola.filemanager.main.MainUiState
import com.mshdabiola.filemanager.main.MainViewModel
import kotlin.io.path.pathString

@Composable
fun MainScreen(mainUiState: State<MainUiState>, navController: NavController) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = mainUiState.value.onPermissionRequest
    )

    val showBackButton = remember {
        derivedStateOf { navController.backQueue.size>2 }
    }
    val onBackClicked = { navController.popBackStack()
    Unit
    }

    MainContent(
        mainUiState.value,
        laucher = {
            launcher.launch(it)
        },
        onDirectoryClicked = { navController.navigate("second/$it") },
        showBackButton=showBackButton.value,
        onBackClicked = onBackClicked
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
    mainUiState: MainUiState = MainUiState("File Manager"),
    laucher: (String) -> Unit = {},
    onDirectoryClicked: (String) -> Unit = {},
    showBackButton: Boolean = true,
    onBackClicked: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(text = mainUiState.name)
                },
                actions = {},
                navigationIcon = {
                    if (showBackButton) {
                        IconButton(onClick = onBackClicked) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "back arrow"
                            )
                        }
                    }
                },
                modifier = Modifier
            )
        }
    ) { values ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(values)
        ) {
            if (mainUiState.askPermission) {

                Button(
                    onClick = {
                        laucher.invoke(Manifest.permission.READ_EXTERNAL_STORAGE)
                    },
                    modifier = Modifier.align(Alignment.Center)


                ) {
                    Text(text = "Ask Permission")

                }
            } else {

                LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {

                    items(mainUiState.fileUiStateList) {
                        if (it.isDirectory) {
                            Directory(fileUiState = it, onDirectoryClicked)
                        } else {
                            File(fileUiState = it)
                        }
                    }
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Directory(fileUiState: FileUiState, onClicked: (String) -> Unit = {}) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            Modifier
                .height(50.dp)
                .fillMaxWidth()
                .padding(4.dp)
                .clickable {
                    onClicked(
                        fileUiState.path.pathString.replace("/", "*")
                    )
                }
        ) {
            Text(text = fileUiState.name)
        }

    }
}

@Preview
@Composable
fun DirectoryPreview() {
    Directory(fileUiState = FileUiState("Directory"))
}

@Composable
fun File(fileUiState: FileUiState) {
    Text(text = fileUiState.name, modifier = Modifier.padding(4.dp))
}