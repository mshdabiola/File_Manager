package com.mshdabiola.filemanager.ui.component

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.signature.ObjectKey
import com.mshdabiola.filemanager.R
import com.mshdabiola.filemanager.data.model.FileUiState
import com.mshdabiola.filemanager.extention.isMediaFile
import kotlin.io.path.Path
import kotlin.io.path.pathString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun File(
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    fileUiState: FileUiState = FileUiState(),
    onDirectoryClick: (String) -> Unit = {},
    isInSelectedMode: Boolean = false
) {


    Row(
        modifier = modifier

            .fillMaxWidth()
            .heightIn(max = 56.dp)
            .background(if (fileUiState.isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
            .clickable {
                if (fileUiState.isDirectory) onDirectoryClick(fileUiState.path.pathString) else fileUiState.onClicked(
                    fileUiState.id
                )
            },
        verticalAlignment = Alignment.CenterVertically

    ) {

        FileContent(fileUiState = fileUiState, Modifier.weight(1f))

        if (isInSelectedMode) {
            RadioButton(
                selected = fileUiState.isSelected,
                onClick = { fileUiState.onSelectedClick(fileUiState.id) })
        } else {
            IconButton(onClick = { fileUiState.onMoreClicked(fileUiState.id) }) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "more")
            }
        }


    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecentFile(
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    fileUiState: FileUiState = FileUiState()
) {


    FileContent(fileUiState = fileUiState,
        Modifier
            .fillMaxWidth()

            .clickable {
                fileUiState.onClicked(
                    fileUiState.id
                )
            })

}


@Composable
fun FileContent(fileUiState: FileUiState, modifier: Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {

        syncImage(
            fileUiState = fileUiState, modifier = Modifier
                .size(56.dp)
                .clip(
                    RoundedCornerShape(
                        dimensionResource(id = R.dimen.base)
                    )
                )
        )

        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.base)))
        Column(Modifier.weight(1f)) {
            Text(
                text = fileUiState.name,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Row {
                Text(
                    text = if (fileUiState.isDirectory) "${fileUiState.numberOfItems} Items" else fileUiState.size,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.paddingFromBaseline(dimensionResource(id = R.dimen.base))
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.half_base)))

                Text(
                    text = fileUiState.lastModified,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.paddingFromBaseline(dimensionResource(id = R.dimen.base))
                )
            }

        }
    }

}


@Preview
@Composable
fun FilePreview() {
    val path =
        "/Users/user/AndroidStudioProjects/FileManager/app/src/main/java/com/mshdabiola/filemanager/Extention.kt"
    File(fileUiState = FileUiState(path = Path(path), isSelected = true))
}

@Preview
@Composable
fun RecentFilePreview() {
    val path =
        "/Users/user/AndroidStudioProjects/FileManager/app/src/main/java/com/mshdabiola/filemanager/Extention.kt"
    RecentFile(fileUiState = FileUiState(path = Path(path)))
}


@Composable
fun syncImage(modifier: Modifier = Modifier, fileUiState: FileUiState) {

    // val initBitmap=ImageBitmap.imageResource(id = R.drawable.ic_plus_vector)

    val context = LocalContext.current
    var bitmap by remember(key1 = fileUiState) {
        mutableStateOf<ImageBitmap?>(null)
    }
    if (fileUiState.path.isMediaFile()) {
        LaunchedEffect(key1 = fileUiState, block = {
            Glide
                .with(context)
                .asBitmap()
                .fallback(fileUiState.drawableRes)
                .override(100, 100)
                .centerCrop()
                .error(fileUiState.drawableRes)
                .load(fileUiState.path.toFile())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .signature(ObjectKey(System.currentTimeMillis() / (1000 * 60 * 60 * 24)))
                .into(object : CustomTarget<Bitmap>() {

                    override fun onLoadCleared(placeholder: Drawable?) {

                    }

                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        bitmap = resource.asImageBitmap()
                    }

                }
                )

        })
    }

    if (bitmap != null && fileUiState.path.isMediaFile()) {
        Image(bitmap = bitmap!!, contentDescription = "", modifier = modifier)
    } else {
        Image(
            painter = painterResource(id = fileUiState.drawableRes),
            contentDescription = "",
            modifier = modifier
        )
    }


}

