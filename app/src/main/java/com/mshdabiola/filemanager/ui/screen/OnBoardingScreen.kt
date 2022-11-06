package com.mshdabiola.filemanager.ui.screen

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.mshdabiola.filemanager.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnBoardingScreen(navController: NavController) {
    val context = LocalContext.current
    val preferences = context.getSharedPreferences("manager", Context.MODE_PRIVATE)
    val title = "show_onboarding"


    var isAccepted by remember {
        mutableStateOf(false)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                isAccepted = Environment.isExternalStorageManager()
            }

        })
    val launcher2 = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {
            isAccepted = it
        })



    OnBoardingContent(
        {
            preferences.edit(true) {
                putBoolean(title, false)
            }
            navController.navigate(it)
        },
        isPermissionAccepted = isAccepted
    ) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            val uri = Uri.fromParts("package", context.packageName, null)

            intent.data = uri
            launcher.launch(intent)
        } else {
            launcher2.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }
}

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalPagerApi::class,
)
@Composable
fun OnBoardingContent(
    navigateTo: (String) -> Unit = {},
    isPermissionAccepted: Boolean = true,
    askPermission: () -> Unit = {}
) {
    val pagerState = rememberPagerState()
    val coroutine = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = R.dimen.base_x2))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            HorizontalPager(
                count = 2,
                state = pagerState,
                modifier = Modifier.weight(1f)

            ) {

                when (currentPage) {
                    0 -> {
                        DisplayCard(R.drawable.ic_wellcome,
                            R.string.onboarding_welcome_title,
                            R.string.onboarding_welcome_content,
                            button = {
                                Button(onClick = {
                                    coroutine.launch {
                                        pagerState.animateScrollToPage(
                                            1
                                        )
                                    }
                                }) {
                                    Text(text = "Next")
                                }
                            }
                        )
                    }
                    1 -> {
                        DisplayCard(R.drawable.ic_permission,
                            R.string.onboarding_permission_title,
                            R.string.onboarding_permission_content,

                            button = {
                                if (!isPermissionAccepted) {
                                    Button(onClick = { askPermission() }) {
                                        Text(text = "Accept Permissions")
                                    }
                                } else {
                                    Button(onClick = { navigateTo("home") }) {
                                        Text(text = "Get Started")
                                    }

                                }

                            }

                        )
                    }

                }


            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                HorizontalPagerIndicator(pagerState = pagerState)

            }


        }

    }
}

@Composable
fun DisplayCard(
    @DrawableRes illustrator: Int,
    @StringRes title: Int,
    @StringRes content: Int,
    button: @Composable () -> Unit = {}
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = illustrator),
            contentDescription = "",
            modifier = Modifier.size(300.dp)
        )
        Text(text = stringResource(id = title), style = MaterialTheme.typography.titleMedium)
        Text(
            text = stringResource(id = content),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.paddingFromBaseline(top = 32.dp),
            textAlign = TextAlign.Center

        )
        Spacer(modifier = Modifier.height(33.dp))
        button()
    }
}
//
//@OptIn(ExperimentalPermissionsApi::class)
//@Composable
//fun SinglePermission(permissionStr: String, onfinished: (PermissionState) -> Unit = {}) {
//    //val permissionRm = rememberLauncherForActivityResult(contract =ActivityResultContracts.GetContent(), onResult ={it} )
//
//    val permission = rememberPermissionState(permission = permissionStr)
//    val lifecycleObserver = LocalLifecycleOwner.current
//    DisposableEffect(key1 = lifecycleObserver, effect = {
//        val eventObserver = LifecycleEventObserver { _, event ->
//            if (event == Lifecycle.Event.ON_START) {
//                permission.launchPermissionRequest()
//            }
//
//        }
//        lifecycleObserver.lifecycle.addObserver(eventObserver)
//        onDispose {
//            lifecycleObserver.lifecycle.removeObserver(eventObserver)
//        }
//    })
//    onfinished(permission)
//
//}
//
//@OptIn(ExperimentalPermissionsApi::class)
//@Composable
//fun MultiplePermission(
//    permissionList: List<String>,
//    onfinished: (MultiplePermissionsState) -> Unit = {}
//) {
//    //val permissionRm = rememberLauncherForActivityResult(contract =ActivityResultContracts.GetContent(), onResult ={it} )
//
//    val permission = rememberMultiplePermissionsState(permissionList)
//    val lifecycleObserver = LocalLifecycleOwner.current
//    DisposableEffect(key1 = lifecycleObserver, effect = {
//        val eventObserver = LifecycleEventObserver { _, event ->
//            if (event == Lifecycle.Event.ON_START) {
//                permission.launchMultiplePermissionRequest()
//            }
//
//
//        }
//
//        lifecycleObserver.lifecycle.addObserver(eventObserver)
//        onDispose {
//            lifecycleObserver.lifecycle.removeObserver(eventObserver)
//        }
//    })
//    LaunchedEffect(key1 = permission, block = {
//        onfinished(permission)
//    })
//
//
//}

@Preview
@Composable
fun SplashScreenPreview() {
    OnBoardingContent()
}