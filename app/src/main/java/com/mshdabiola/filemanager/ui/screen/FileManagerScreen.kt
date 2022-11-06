package com.mshdabiola.filemanager.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.mshdabiola.filemanager.R
import com.mshdabiola.filemanager.data.model.ActionBottomSheetUiState
import com.mshdabiola.filemanager.data.model.FileManagerUiState
import com.mshdabiola.filemanager.data.model.FileUiState
import com.mshdabiola.filemanager.ui.component.*
import kotlin.io.path.Path

@Composable
fun FileManagerScreen(mainUiState: FileManagerUiState, navController: NavController) {


    val isDirectorySelected by remember(mainUiState.fileUiStateList) {
        derivedStateOf { mainUiState.fileUiStateList.any { it.isSelected && it.isDirectory } }
    }

    val numOfFileSelect by remember(mainUiState.fileUiStateList) {
        derivedStateOf { mainUiState.fileUiStateList.count { it.isSelected } }
    }
    val onBackClicked: () -> Unit = {
        navController.navigateUp()
    }


    FileManagerContent(
        mainUiState,
        navigateToAnotherScreen = {
            mainUiState.onDeselectedAllFile()
            navController.navigate(it)

        },
        numOfSelectedFile = numOfFileSelect,
        isDirectorySelected = isDirectorySelected,
        onBackClicked = onBackClicked
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileManagerContent(
    fileManagerUiState: FileManagerUiState = FileManagerUiState("File Manager"),
    navigateToAnotherScreen: (String) -> Unit = {},
    numOfSelectedFile: Int = 0,
    onBackClicked: () -> Unit = {},
    isDirectorySelected: Boolean = false
) {
    Box(
        modifier = Modifier

            .fillMaxSize()


    ) {
        Scaffold(
            topBar = {
                if (fileManagerUiState.isInSelectedMode) {
                    SmallTopAppBar(
                        title = {
                            Text(text = "$numOfSelectedFile / ${fileManagerUiState.fileUiStateList.size} Selected")
                        },
                        actions = {


                            IconButton(onClick = { fileManagerUiState.onSelectedAllFile() }) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_select_all_24),
                                    contentDescription = "select all"
                                )
                            }

                            if (numOfSelectedFile > 0) {
                                IconButton(onClick = { fileManagerUiState.openActionBottomSheet() }) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = "more option"
                                    )
                                }
                            }

                        },
                        navigationIcon = {

                            IconButton(onClick = fileManagerUiState.onDeselectedAllFile) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "deselected icon"
                                )
                            }

                        },
                        colors =
                        TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary)

                    )
                } else {
                    SmallTopAppBar(
                        title = {
                            Text(text = fileManagerUiState.name)
                        },
                        actions = {},
                        navigationIcon = {

                            IconButton(onClick = onBackClicked) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "back arrow"
                                )
                            }

                        }
                    )
                }


            }
        ) { values ->


            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.base)),
                modifier = Modifier
                    .padding(values)
                    .padding(horizontal = dimensionResource(id = R.dimen.base_x2))
            ) {

                items(fileManagerUiState.fileUiStateList, key = { it.id }) { fileUiState ->

                    File(
                        fileUiState = fileUiState,
                        onDirectoryClick = {
                            navigateToAnotherScreen(
                                "second/${
                                    it.replace(
                                        "/",
                                        "*"
                                    )
                                }"
                            )
                        },
                        isInSelectedMode = fileManagerUiState.isInSelectedMode
                    )

                }
            }

        }
        ActionBottomSheet(
            modifier = Modifier.align(Alignment.BottomCenter),
            fileManagerUiState.actionBottomSheetUiState,
            navigateToFileInfo = { navigateToAnotherScreen("fileinfo") },
            isDirectory = fileManagerUiState.currentFileUiState.isDirectory,
            isDirectorySelected = isDirectorySelected,
            numOfItemSelected = numOfSelectedFile,
            isSelectMode = fileManagerUiState.isInSelectedMode
        )
        MoveAndCopyBottomSheet(
            modifier = Modifier.align(Alignment.BottomCenter),
            moveAndCopyBottomSheetUiState = fileManagerUiState.moveAndCopyBottomSheetUiState,
            onStorageClick = { navigateToAnotherScreen("select/${it.replace("/", "*")}") }
        )
        RenameDialog(
            modifier = Modifier.align(Alignment.Center),
            renameUiState = fileManagerUiState.renameUiState
        )
        DeleteDialog(
            modifier = Modifier.align(Alignment.Center),
            deleteUiState = fileManagerUiState.deleteUiState
        )

    }
}

@Preview(showSystemUi = true)
@Composable
fun MainContentPreview() {


    var show by remember {
        mutableStateOf(true)
    }
    val pathstr =
        "/Users/user/AndroidStudioProjects/FileManager/app/src/main/res/drawable/ic_baseline_android_24.xml"
    val path = Path(pathstr)

    val paths = (1..8).map { if (it % 2 == 0) path else path.parent }
        .map { FileUiState(path = it, onMoreClicked = { show = true }) }
    FileManagerContent(
        fileManagerUiState = FileManagerUiState(
            fileUiStateList = paths,
            name = "File Manager",
            actionBottomSheetUiState = ActionBottomSheetUiState(
                show = show,
                onDismiss = { show = false })
        )
    )
}




