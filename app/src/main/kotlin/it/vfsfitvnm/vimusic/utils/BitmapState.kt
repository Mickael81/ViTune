package it.vfsfitvnm.vimusic.utils

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import it.vfsfitvnm.vimusic.service.PlayerService

@Composable
fun PlayerService.Binder?.collectProvidedBitmapAsState(): State<Bitmap?> {
    val state = remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(this) {
        this@collectProvidedBitmapAsState?.setBitmapListener {
            state.value = it
        }
    }

    return state
}
