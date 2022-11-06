package com.mshdabiola.filemanager.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.mshdabiola.filemanager.R
import com.mshdabiola.filemanager.data.model.CategoryUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryCard(
    categoryUiState: CategoryUiState = CategoryUiState(),
    onClicked: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.clickable {
            onClicked(categoryUiState.path.replace("/", "*"))
        },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = categoryUiState.icon),
                contentDescription = categoryUiState.name,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.base_x2))
            )
        }
        Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.half_base)))
        Text(text = categoryUiState.name, style = MaterialTheme.typography.labelSmall)
    }

}

@Preview(showBackground = true, backgroundColor = android.graphics.Color.GRAY.toLong())
@Composable
fun CategoryCardPreview() {
    CategoryCard()
}
