package com.mshdabiola.filemanager.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mshdabiola.filemanager.R
import com.mshdabiola.filemanager.data.model.StorageUiState
import com.mshdabiola.filemanager.ui.theme.FileManagerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StorageCard(
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    memoryUiState: StorageUiState = StorageUiState()
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)


    ) {
        Column(
            contentModifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.base))
        ) {
            Row(
                modifier = contentModifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = memoryUiState.icon),
                    contentDescription = "menu"
                )
                Spacer(modifier = contentModifier.width(dimensionResource(id = R.dimen.base)))
                Column(contentModifier.fillMaxWidth()) {
                    Text(text = memoryUiState.name, style = MaterialTheme.typography.titleMedium)

                    Text(
                        text = "${memoryUiState.usedSize} / ${memoryUiState.totalSize}",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = contentModifier.paddingFromBaseline(top = dimensionResource(id = R.dimen.base))
                    )
                }
            }


            Spacer(modifier = contentModifier.height(dimensionResource(id = R.dimen.base)))

            LinearProgressIndicator(
                progress = memoryUiState.fractionUsed,
                modifier = contentModifier.fillMaxWidth(),
                trackColor = MaterialTheme.colorScheme.surface

            )

            Text(
                text = "Free space ${memoryUiState.freeSize} ",
                style = MaterialTheme.typography.bodySmall,
                color = TextStyle.Default.color.copy(alpha = 0.6f),
                modifier = contentModifier
                    .align(Alignment.CenterHorizontally)
                    .paddingFromBaseline(
                        top = dimensionResource(
                            id = R.dimen.base
                        )
                    )
            )
        }
    }
}

@Preview
@Composable
fun StorageCardPreview() {
    FileManagerTheme {
        StorageCard(modifier = Modifier.width(200.dp))
    }

}