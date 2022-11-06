package com.mshdabiola.filemanager.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mshdabiola.filemanager.R
import com.mshdabiola.filemanager.data.model.FileUiState
import com.mshdabiola.filemanager.ui.component.syncImage
import kotlin.io.path.extension
import kotlin.io.path.pathString

@Composable
fun FileInfoScreen(navController: NavController, fileUiState: FileUiState) {

    FileInfoContent(fileUiState = fileUiState, onNavigateBack = { navController.navigateUp() })

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileInfoContent(fileUiState: FileUiState, onNavigateBack: () -> Unit = {}) {

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(400.dp)
        ) {


            syncImage(
                fileUiState = fileUiState,
                modifier = Modifier.fillMaxSize()
            )
            SmallTopAppBar(
                title = {},
                modifier = Modifier.align(Alignment.TopCenter),
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent),
                navigationIcon = {
                    FilledIconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.align(Alignment.TopStart)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "back arrow"
                        )
                    }
                }

            )

        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = fileUiState.name, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (fileUiState.isDirectory) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_folder_24),
                        contentDescription = "",

                        )
                } else {

                    Text(
                        text = fileUiState.path.extension,
                        color = MaterialTheme.colorScheme.background,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.onBackground)
                            .padding(
                                horizontal = dimensionResource(
                                    id = R.dimen.base
                                )
                            )
                    )


                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = fileUiState.path.pathString,
                        style = MaterialTheme.typography.titleMedium,

                        )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(Modifier.fillMaxWidth()) {
                        Text(
                            text = "${fileUiState.size} size",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                        )
                    }
                }


            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_round_today_24),
                    contentDescription = "",

                    )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "modified ${fileUiState.lastModified}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun FileInfoPreview() {
    FileInfoContent(fileUiState = FileUiState())
}