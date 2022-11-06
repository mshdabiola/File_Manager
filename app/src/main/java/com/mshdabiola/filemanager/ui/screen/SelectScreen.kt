package com.mshdabiola.filemanager.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.mshdabiola.filemanager.data.model.SelectUiState
import com.mshdabiola.filemanager.ui.component.File
import kotlin.io.path.pathString


@Composable
fun SelectScreen(navController: NavController, selectUiState: SelectUiState) {

    SelectContent(selectUiState, navTo = {
        navController.navigate("select/${it}")
    }, backNav = { navController.navigateUp() }, popTo = {

        val numOfSelect = navController.backQueue.filter {
            it.destination.route?.contains("select") ?: false
        }.size
        val last = if (numOfSelect > 1) navController.backQueue.removeLast() else null

        navController.backQueue
            .removeIf {
                it.destination.route?.contains("select") ?: false
            }
        last?.let {
            navController.backQueue.addLast(it)
        }



        navController.popBackStack()
    }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectContent(
    selectUiState: SelectUiState = SelectUiState(),
    navTo: (String) -> Unit = {},
    backNav: () -> Unit = {},
    popTo: () -> Unit = {}
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(
                        text = selectUiState.folderName
                    )
                },
                navigationIcon = {
                    IconButton(onClick = backNav) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back button"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                //add item for add new Folder

                //list of folder
                items(
                    items = selectUiState.listOfFileUiState,
                    key = { selectUiState.listOfFileUiState.indexOf(it) }) { fileUiState ->
                    File(
                        fileUiState = fileUiState,
                        onDirectoryClick = { navTo(it.replace("/", "*")) })
                }
            }
            Button(onClick = {
                selectUiState.onSelectClick(selectUiState.currentPath!!.pathString)
                popTo()
            }) {
                Text(text = "Select this Folder")
            }
        }

    }

}

@Preview
@Composable
fun SelectContentPreview() {
    SelectContent()
}