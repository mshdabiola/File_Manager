package com.mshdabiola.filemanager.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mshdabiola.filemanager.R
import com.mshdabiola.filemanager.ui.screen.home.CategoryUiState
import com.mshdabiola.filemanager.ui.screen.home.HomeRecentFile
import com.mshdabiola.filemanager.ui.screen.home.HomeUiState
import com.mshdabiola.filemanager.ui.screen.home.MemoryUiState
import com.mshdabiola.filemanager.ui.theme.FileManagerTheme

@Composable
fun HomeScreen(homeUiState: HomeUiState, navController: NavController) {



    HomeContent(homeUiState = homeUiState, onMemoryClicked = {navController.navigate("second/$it")})

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(homeUiState: HomeUiState, onMemoryClicked : (String)->Unit={}) {
    val scrollState= rememberScrollState()

    Scaffold(topBar = {
        SmallTopAppBar (
            title = { Text(text = stringResource(id = R.string.app_name))}
                )
    }) { paddingValues->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 8.dp)
                .verticalScroll(scrollState)

        ) {
            Row(
            modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                homeUiState.memoryUiStates.forEachIndexed { index, memoryUiState ->
                    MemoryCard(modifier = Modifier
                        .clickable { onMemoryClicked(memoryUiState.path.replace("/", "*")) }
                        .weight(1f),
                        memoryUiState = memoryUiState)
                   if (index==0) {
                       Spacer(modifier = Modifier.width(8.dp))
                   }}

            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Categories", style = MaterialTheme.typography.titleMedium,color=MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            val category= homeUiState.categoryUiStates
            val columnSize = 3
            var row =category.size/columnSize
            if (category.size%columnSize != 0){
                row+=1
            }

           for (i in 0 until row){
               Row (Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically){
                   for (j in 0 until columnSize){
                       val index = i*3+j

                       if (index<category.size){
                           CategoryCard(category[index], onClicked =  onMemoryClicked)
                       }

                   }
               }

           }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Recent Files", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
          homeUiState.homeRecentFiles.forEach {
              ClickableFileCard(modifier = Modifier.fillMaxWidth(), recentFile = it)
              Spacer(modifier = Modifier.height(4.dp))
          }


        }

    }
}
@Preview(showSystemUi = true)
@Composable
fun HomeContentPreview(){
    FileManagerTheme {
        HomeContent(homeUiState = HomeUiState(memoryUiStates = arrayListOf(
            MemoryUiState(),
            MemoryUiState()
        ),
        categoryUiStates = (1..8).map { CategoryUiState() }
            ))
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoryCard(memoryUiState: MemoryUiState = MemoryUiState(), modifier: Modifier=Modifier, contentModifier: Modifier=Modifier) {
    Card(modifier = modifier,


    ) {
        Column (
            contentModifier
                .fillMaxWidth()
                .padding(16.dp)){
            Icon(imageVector = ImageVector.vectorResource(id = memoryUiState.icon), contentDescription = "menu")
            Spacer(modifier = contentModifier.height(8.dp))
            Text(text = memoryUiState.name, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = contentModifier.height(8.dp))
            Text(text = "${memoryUiState.usedSize} / ${memoryUiState.totalSize}", style = MaterialTheme.typography.bodySmall)
            LinearProgressIndicator(progress = memoryUiState.fractionUsed,
                modifier = contentModifier.fillMaxWidth(),
                trackColor = MaterialTheme.colorScheme.surface

            )
            Spacer(modifier = contentModifier.height(4.dp))
            Text(text ="Free space ${memoryUiState.freeSize} ", style = MaterialTheme.typography.bodySmall,
                color = TextStyle.Default.color.copy(alpha = 0.4f),
                modifier = contentModifier.align(Alignment.CenterHorizontally)
                )
        }
    }
}

@Preview
@Composable
fun MemoryCardPreview() {
    FileManagerTheme {
        MemoryCard(modifier = Modifier.width(200.dp))
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryCard(categoryUiState: CategoryUiState = CategoryUiState(), onClicked: (String) -> Unit={}) {
    Column (
        modifier = Modifier.clickable {
            onClicked(categoryUiState.path.replace("/","*")) },
        horizontalAlignment = Alignment.CenterHorizontally){
        Card(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Icon(imageVector = ImageVector.vectorResource(id = categoryUiState.icon),
                contentDescription = categoryUiState.name,
                modifier = Modifier.padding(16.dp))
        }
        Text(text = categoryUiState.name, style = MaterialTheme.typography.labelSmall)
    }

}

@Preview(showBackground = true, backgroundColor = android.graphics.Color.GRAY.toLong())
@Composable
fun CategoryCardPreview() {
    CategoryCard()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClickableFileCard(modifier : Modifier=Modifier,
                      contentModifier: Modifier=Modifier,
                      recentFile: HomeRecentFile
) {
    Card (modifier = modifier){
        Row(contentModifier.padding(4.dp)) {
            Icon(imageVector = Icons.Default.Menu, contentDescription = "",
                modifier = contentModifier.size(50.dp))
            Column(modifier = contentModifier) {
                Text(text = recentFile.name,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row {
                    Text(text = recentFile.sizeStr, style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = recentFile.date.toString(), style = MaterialTheme.typography.bodySmall)
                }

            }
        }
    }
}

@Preview
@Composable
fun ClickableFileCardPreview() {
    ClickableFileCard(recentFile = HomeRecentFile())
}