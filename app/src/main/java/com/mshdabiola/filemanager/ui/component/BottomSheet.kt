package com.mshdabiola.filemanager.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mshdabiola.filemanager.R

@Composable
fun BottomSheet(
    modifier: Modifier = Modifier,
    show: Boolean = true,
    onDismissRequest: () -> Unit = {},
    content: @Composable () -> Unit = {

    }
) {

    if (show) {
        Box(modifier = Modifier
            .fillMaxSize()
            .pointerInput(true) {
                detectTapGestures {
                    onDismissRequest()
                }
            }
            .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)))
    }
    AnimatedVisibility(
        visible = show,
        enter = slideInVertically { it },
        exit = slideOutVertically { it },
        modifier = modifier
    ) {
        Surface(

            tonalElevation = 4.dp,
            shadowElevation = 4.dp,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)


        ) {
            Box(Modifier.padding(dimensionResource(id = R.dimen.base_x2))) {
                content()
            }

        }


    }


}

@Preview
@Composable
fun BottomSheetPreview() {

    var show by remember {
        mutableStateOf(true)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        BottomSheet(
            modifier = Modifier.align(Alignment.BottomCenter),
            show = show,
            onDismissRequest = { show = false }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .background(Color.Red)
            )
        }

    }

}