package com.mshdabiola.filemanager.ui.screen

import android.Manifest
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.mshdabiola.filemanager.R
import com.mshdabiola.filemanager.formatSize
import com.mshdabiola.filemanager.ui.screen.main.FileUiState
import com.mshdabiola.filemanager.ui.screen.main.MainUiState
import com.mshdabiola.filemanager.ui.screen.main.MainViewModel
import kotlin.io.path.pathString

@Composable
fun MainScreen(mainUiState: State<MainUiState>, navController: NavController) {

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = mainUiState.value.onPermissionRequest
    )

    val showBackButton = remember {
        derivedStateOf { navController.backQueue.size > 2 }
    }
    val onBackClicked = {
        navController.popBackStack()
        Unit
    }

    val onFileClicked = { path: String ->
        val file = java.io.File(path)
        val uri = FileProvider.getUriForFile(
            context,
            context.applicationContext.packageName + ".provider",
            file
        )
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(intent)
    }

    MainContent(
        mainUiState.value,
        laucher = {
            launcher.launch(it)
        },
        onDirectoryClicked = { navController.navigate("second/$it") },
        showBackButton = showBackButton.value,
        onBackClicked = onBackClicked,
        onFileClicked = onFileClicked
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
    mainUiState: MainUiState = MainUiState("File Manager"),
    laucher: (String) -> Unit = {},
    onDirectoryClicked: (String) -> Unit = {},
    onFileClicked: (String) -> Unit = {},
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
                            Directory(fileUiState = it, onClicked = onDirectoryClicked)
                        } else {
                            File(fileUiState = it, onClicked = onFileClicked)
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
fun Directory(modifier: Modifier = Modifier,
              contentModifier: Modifier =Modifier,
              fileUiState: FileUiState = FileUiState(),
              onClicked: (String) -> Unit={}) {

    ElevatedCard(modifier = modifier.clickable { onClicked(fileUiState.path.replace("/","*")) }) {

        Row(modifier = contentModifier
            .padding(4.dp)
            .fillMaxWidth()) {
            Image(
                painter = painterResource(id = R.drawable.ic_baseline_folder_24),
                contentDescription = "file",
                modifier = contentModifier.size(50.dp)
            )
            Column(contentModifier.weight(1f)) {
                Text(
                    text = fileUiState.name,
                    style = MaterialTheme.typography.bodyMedium, modifier = contentModifier
                )
                Row {
                    Text(text =  if (fileUiState.numOfItems>1) "${fileUiState.numOfItems} items" else "${fileUiState.numOfItems} item", style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = contentModifier.width(8.dp))
                    Text(text = fileUiState.date.toString(), style = MaterialTheme.typography.bodySmall)
                }

            }

            IconButton(onClick = { }) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "more")
            }

        }
    }



}

@Preview
@Composable
fun DirectoryPreview() {
    Directory(fileUiState = FileUiState("Directory"))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun File(
    modifier: Modifier = Modifier,
    contentModifier: Modifier =Modifier,
    fileUiState: FileUiState = FileUiState(),
    onClicked: (String) -> Unit={}
) {
    Card(modifier = modifier.clickable { onClicked(fileUiState.path) }) {

        Row(modifier = contentModifier
            .padding(4.dp)
            .fillMaxWidth()) {
            Image(
                painter = painterResource(id = R.drawable.ic_baseline_insert_drive_file_24),
                contentDescription = "file",
                modifier = contentModifier.size(50.dp)
            )
            Column(contentModifier.weight(1f)) {
                Text(
                    text = fileUiState.name,
                    style = MaterialTheme.typography.bodyMedium, modifier = contentModifier,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Row {
                    Text(text = fileUiState.size.formatSize(), style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = contentModifier.width(8.dp))
                    Text(text = fileUiState.date.toString(), style = MaterialTheme.typography.bodySmall)
                }

            }

            IconButton(onClick = { }) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "more")
            }

        }
    }

}

@Preview
@Composable
fun FilePreview() {
    File()
}