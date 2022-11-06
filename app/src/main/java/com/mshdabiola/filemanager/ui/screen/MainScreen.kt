package com.mshdabiola.filemanager.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mshdabiola.filemanager.R
import com.mshdabiola.filemanager.data.model.CategoryUiState
import com.mshdabiola.filemanager.data.model.FileUiState
import com.mshdabiola.filemanager.data.model.MainUiState
import com.mshdabiola.filemanager.data.model.StorageUiState
import com.mshdabiola.filemanager.ui.component.CategoryCard
import com.mshdabiola.filemanager.ui.component.RecentFile
import com.mshdabiola.filemanager.ui.component.StorageCard
import com.mshdabiola.filemanager.ui.theme.FileManagerTheme
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString


@Composable
fun MainScreen(mainUiState: MainUiState, navController: NavController) {


    LaunchedEffect(key1 = Unit, block = {
        if (mainUiState.recentFiles.isEmpty()){
            mainUiState.getAllRecentFileUistate
        }
    })
    MainContent(
        mainUiState = mainUiState,
        onMemoryClicked = { navController.navigate("second/$it") })

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(mainUiState: MainUiState, onMemoryClicked: (String) -> Unit = {}) {
    val scrollState = rememberScrollState()

    Scaffold(topBar = {
        SmallTopAppBar(
            title = { Text(text = stringResource(id = R.string.app_name))
            },

        )
    }) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(paddingValues)
                .padding(
                    horizontal = dimensionResource(id = R.dimen.base_x2),
                    vertical = dimensionResource(
                        id = R.dimen.base_x2
                    )
                )

        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                mainUiState.storageUiStates.forEachIndexed { index, memoryUiState ->
                    StorageCard(modifier = Modifier
                        .clickable {
                            onMemoryClicked(
                                memoryUiState.path
                                    .absolutePathString()
                                    .replace("/", "*")
                            )
                        }
                        .weight(1f),
                        memoryUiState = memoryUiState)
                    if (index == 0) {
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }

            }

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.base_x2)))
            Text(
                text = "Categories",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.base)))
            val category = mainUiState.categoryUiStates
            val columnSize = 3
            var row = category.size / columnSize
            if (category.size % columnSize != 0) {
                row += 1
            }

            for (i in 0 until row) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (j in 0 until columnSize) {
                        val index = i * 3 + j

                        if (index < category.size) {
                            CategoryCard(category[index], onClicked = onMemoryClicked)
                        }

                    }
                }

            }


            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.base_x2)))
            Text(
                text = "Recent Files",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.base)))
            mainUiState.recentFiles.forEach {
                RecentFile(modifier = Modifier.fillMaxWidth(), fileUiState = it)

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.base)))
            }


        }

    }
}

@Preview(showSystemUi = true)
@Composable
fun HomeContentPreview() {
    val pathstr =
        "/Users/user/AndroidStudioProjects/FileManager/app/src/main/res/drawable/ic_baseline_android_24.xml"
    val path = Path(pathstr)

    val paths = (1..8).map { path }
        .map { FileUiState(path = it) }

    FileManagerTheme {
        FileManagerTheme {
            MainContent(
                mainUiState = MainUiState(
                    storageUiStates = arrayListOf(
                        StorageUiState(),
                        StorageUiState()
                    ),
                    categoryUiStates = (1..6).map { CategoryUiState() },
                    recentFiles = paths
                )
            )
        }

    }

}





