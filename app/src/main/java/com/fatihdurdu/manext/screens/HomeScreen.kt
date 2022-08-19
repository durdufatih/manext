package com.fatihdurdu.manext.screens

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.fatihdurdu.manext.MainActivity
import com.fatihdurdu.manext.R
import com.fatihdurdu.manext.data.ImageResponseList
import com.fatihdurdu.manext.data.ImageResponseListItem
import com.fatihdurdu.manext.viewmodel.ListViewModel
import com.fatihdurdu.manext.viewmodel.state.ListState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


@Composable
@ExperimentalPermissionsApi
fun HomeScreen(navController: NavController) {

    Surface(modifier = Modifier.fillMaxSize()) {
        General()
    }
}

@Composable
@ExperimentalPermissionsApi
fun General(listViewModel: ListViewModel = hiltViewModel()) {

    Column {
        when (val state = listViewModel.allListState.collectAsState().value) {

            is ListState.Empty -> Text(text = "No data available")
            is ListState.Loading -> Text(text = "Loading...")
            is ListState.Success -> {
                ListItems(state.data)
            }
            is ListState.Error -> Text(text = state.message)
            else -> {}
        }
    }
}

@Composable
@ExperimentalPermissionsApi
fun ListItems(list: ImageResponseList) {
    LazyColumn {
        items(items = list, itemContent = {
            ContentCard(it)
        })
    }
}


@ExperimentalPermissionsApi
@Composable
fun ContentCard(imageResponseListItem: ImageResponseListItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(450.dp)
            .padding(10.dp),
        backgroundColor = Color.White,
        shape = RoundedCornerShape(20.dp),
        elevation = (0.5).dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ContentHeader(imageResponseListItem)
            Content(imageResponseListItem)
            BottomActions(imageResponseListItem)
        }

    }
}

@Composable
fun ContentHeader(imageResponseListItem: ImageResponseListItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface {

            Image(
                painter = rememberAsyncImagePainter(
                    imageResponseListItem.user.profile_image?.medium ?: ""
                ),
                modifier = Modifier
                    .clip(CircleShape)
                    .size(46.dp),
                contentScale = ContentScale.Crop,
                contentDescription = "Main picture"
            )


        }
        Surface(modifier = Modifier.padding(1.dp)) {
            Column {
                Text(
                    text = imageResponseListItem.user.name,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = imageResponseListItem.user.instagram_username ?: "",
                    fontWeight = FontWeight.Bold,
                    color = Color.LightGray,
                    fontSize = 13.sp
                )
            }
        }
        Surface {
            Text(
                text = "8 min",
                fontWeight = FontWeight.Bold,
                color = Color.LightGray,
                fontSize = 13.sp
            )
        }
    }

}

@Composable
fun Content(imageResponseListItem: ImageResponseListItem) {
    Card(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth()
            .height(330.dp),
        shape = RoundedCornerShape(30.dp),
        elevation = 0.dp
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                imageResponseListItem.urls.regular
            ),
            contentScale = ContentScale.Crop,
            modifier = Modifier.clip(RoundedCornerShape(10.dp)),
            contentDescription = "Main picture"
        )
    }
}


@Composable
@ExperimentalPermissionsApi
fun BottomActions(imageResponseListItem: ImageResponseListItem) {
    var instance = MainActivity()
    val context = LocalContext.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(start = 10.dp, end = 10.dp)
    ) {

        val permissionWrite =
            rememberPermissionState(permission = Manifest.permission.WRITE_EXTERNAL_STORAGE)

        IconButton(onClick = {
            if (permissionWrite.status.isGranted) {
                saveButton(imageResponseListItem.urls.regular, context)
            } else {
                permissionWrite.launchPermissionRequest()
            }


        }) {
            Icon(
                painterResource(id = R.drawable.baseline_save_24),
                contentDescription = "Save",
                tint = Color.Gray
            )

        }
        IconButton(onClick = {
            openShare(
                imageResponseListItem.urls.regular,
                context
            )
        }) {

            Icon(
                Icons.Default.Share,
                contentDescription = "Share",
                tint = Color.Gray
            )


        }
    }
}

private fun openShare(url: String, context: Context) {

    CoroutineScope(Dispatchers.IO).launch {
        val bitmap = downloadFile(url, context)
        val path = MediaStore.Images.Media.insertImage(
            context.contentResolver,
            bitmap,
            "Title",
            ""
        )
        withContext(Dispatchers.Main) {

            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path))
            intent.putExtra(Intent.EXTRA_TEXT, "Manext app sharing image with you")
            intent.type = "image/*"
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            try {
                context.startActivity(Intent.createChooser(intent, "Share"))
            } catch (ex: ActivityNotFoundException) {
                Log.d("Hello", "Error:${ex.message}")
            }
        }
    }


}


private fun saveButton(url: String, instance: Context) {
    CoroutineScope(Dispatchers.IO).launch {
        val bitmap = downloadFile(url, instance)
        withContext(Dispatchers.Main) {
            saveMediaToStorage(bitmap, instance)
        }
    }


}


private suspend fun downloadFile(url: String, context: Context): Bitmap {
    val loader = ImageLoader(context)
    val request = ImageRequest.Builder(context)
        .data(url)
        .allowHardware(false) // Disable hardware bitmaps.
        .build()

    val result = (loader.execute(request) as SuccessResult).drawable
    return (result as BitmapDrawable).bitmap
}


private fun saveMediaToStorage(bitmap: Bitmap, instance: Context) {
    val filename = "${System.currentTimeMillis()}.jpg"
    val fos: OutputStream?

    val imagesDir =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath.plus(
            "/MANEXT"
        )
    val directory = File(imagesDir)
    if (!directory.exists())
        directory.mkdir()
    val image = File(imagesDir, filename)
    fos = FileOutputStream(image)

    fos.use {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)

        Handler(Looper.getMainLooper()).post {
            Toast.makeText(instance, "Saved $filename", Toast.LENGTH_SHORT).show()
        }
    }
}