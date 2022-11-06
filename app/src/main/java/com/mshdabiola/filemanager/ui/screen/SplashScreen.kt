package com.mshdabiola.filemanager.ui.screen

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.mshdabiola.filemanager.R
import com.mshdabiola.filemanager.ui.theme.FileManagerTheme
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(navController: NavController) {

    val context = LocalContext.current
    val preferences = context.getSharedPreferences("manager", Context.MODE_PRIVATE)

    val title = "show_onboarding"
    val showOnBoarding = preferences.getBoolean(title, true)

    if (showOnBoarding) {
        navController.popBackStack()
        navController.navigate("onboarding")
    } else {
        LaunchedEffect(key1 = Unit, block = {
            delay(1000)
            navController.popBackStack()
            navController.navigate("home")

        }
        )
    }
    SplashContent()
}

@Composable
fun SplashContent(navigateTo: () -> Unit = {}) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
        )
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.primary
        )

    }
}

@Preview(showBackground = true)
@Composable
fun SplashPreview() {
    FileManagerTheme {
        SplashContent()
    }

}
