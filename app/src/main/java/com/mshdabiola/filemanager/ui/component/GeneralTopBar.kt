package com.mshdabiola.filemanager.ui.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    onNavigationButton: () -> Unit = {},
    color: TopAppBarColors = TopAppBarDefaults.smallTopAppBarColors()
) {
    TopAppBar(
        title = {
            Text(text = title)
        },
        navigationIcon = {

            IconButton(onClick = onNavigationButton) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "back arrow"
                )
            }

        },
        colors = color
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TopAppBarPreview() {
    TopAppBar(title = "FileManager")
}