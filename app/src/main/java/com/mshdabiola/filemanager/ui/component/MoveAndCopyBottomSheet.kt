package com.mshdabiola.filemanager.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mshdabiola.filemanager.R
import com.mshdabiola.filemanager.data.model.MoveAndCopyBottomSheetUiState
import kotlin.io.path.pathString

@Composable
fun MoveAndCopyBottomSheet(
    modifier: Modifier = Modifier,
    moveAndCopyBottomSheetUiState: MoveAndCopyBottomSheetUiState = MoveAndCopyBottomSheetUiState(),
    onStorageClick: (String) -> Unit = {}
) {
    BottomSheet(
        modifier,
        moveAndCopyBottomSheetUiState.show,
        moveAndCopyBottomSheetUiState.onDismiss
    ) {
        Column(
            modifier = Modifier

                .fillMaxWidth()

        ) {
            Text(
                text = moveAndCopyBottomSheetUiState.actionName,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 8.dp)
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.base)))

            LazyColumn(Modifier.fillMaxWidth()) {
                items(moveAndCopyBottomSheetUiState.listOfStorageUiState) {
                    Row(modifier = Modifier
                        .clickable {
                            onStorageClick(it.path.pathString)
                            moveAndCopyBottomSheetUiState.onDismiss()
                        }
                        .height(52.dp)
                        .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = it.icon),
                            contentDescription = "storage icon",
                            modifier = Modifier.size(44.dp)
                        )
                        Text(
                            text = it.name,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun MoveAndCopyBottomSheetPreview() {
    MoveAndCopyBottomSheet(moveAndCopyBottomSheetUiState = MoveAndCopyBottomSheetUiState(show = true))
}