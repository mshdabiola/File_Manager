package com.mshdabiola.filemanager.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mshdabiola.filemanager.R
import com.mshdabiola.filemanager.home.HomeUiState
import com.mshdabiola.filemanager.home.MemoryUiState
import com.mshdabiola.filemanager.ui.theme.FileManagerTheme

@Composable
fun HomeScreen(homeUiState: HomeUiState, navController: NavController) {



    HomeContent(homeUiState = homeUiState, onMemoryClicked = {navController.navigate("second/$it")})

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(homeUiState: HomeUiState,onMemoryClicked : (String)->Unit={}) {
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
                        .clickable { onMemoryClicked(memoryUiState.path.replace("/","*")) }
                        .weight(1f),
                        memoryUiState = memoryUiState)
                   if (index==0) {
                       Spacer(modifier = Modifier.width(8.dp))
                   }}

            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Categories", style = MaterialTheme.typography.titleMedium,color=MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
           for (i in 1..3){
               Row (Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically){
                   for (j in 1..3){
                       CategoryCard()
                   }
               }

           }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Recent Files", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
          (1..10).toList().forEach {
              ClickableFileCard(modifier = Modifier.fillMaxWidth())
              Spacer(modifier = Modifier.height(4.dp))
          }


        }

    }
}
@Preview(showSystemUi = true)
@Composable
fun HomeContentPreview(){
    FileManagerTheme {
        HomeContent(homeUiState = HomeUiState())
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoryCard(memoryUiState: MemoryUiState= MemoryUiState(), modifier: Modifier=Modifier,contentModifier: Modifier=Modifier) {
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
fun CategoryCard() {
    Column (horizontalAlignment = Alignment.CenterHorizontally){
        Card(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Icon(imageVector = Icons.Default.Info, contentDescription = "", modifier = Modifier.padding(16.dp))
        }
        Text(text = "Document", style = MaterialTheme.typography.labelSmall)
    }

}

@Preview(showBackground = true, backgroundColor = android.graphics.Color.GRAY.toLong())
@Composable
fun CategoryCardPreview() {
    CategoryCard()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClickableFileCard(modifier : Modifier=Modifier,contentModifier: Modifier=Modifier) {
    Card (modifier = modifier){
        Row(contentModifier.padding(4.dp)) {
            Icon(imageVector = Icons.Default.Menu, contentDescription = "",
                modifier = contentModifier.size(50.dp))
            Column(modifier = contentModifier) {
                Text(text = "Shoot Tuesday", style = MaterialTheme.typography.titleSmall)
                Text(text = "473Kb", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Preview
@Composable
fun ClickableFileCardPreview() {
    ClickableFileCard()
}