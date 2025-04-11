package com.example.pokemontrade.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

@SuppressLint("MissingPermission")
@Composable
fun GoogleMapScreen(
    modifier: Modifier = Modifier,
    onLocationSelected: (LatLng, String) -> Unit
) {
    val context = LocalContext.current
    var markerPosition by remember { mutableStateOf<LatLng?>(null) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(37.1773, -3.5986), 13f)
    }

    LaunchedEffect(markerPosition) {
        markerPosition?.let { coords ->
            val address = resolveAddress(context, coords)
            onLocationSelected(coords, address ?: "Lat: ${coords.latitude}, Lng: ${coords.longitude}")
        }
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapClick = { latLng ->
            markerPosition = latLng
        },
        properties = MapProperties(isMyLocationEnabled = true),
        uiSettings = MapUiSettings(myLocationButtonEnabled = true)
    ) {
        markerPosition?.let { pos ->
            Marker(state = MarkerState(position = pos), title = "Ubicación seleccionada")
        }
    }
}

private suspend fun resolveAddress(context: Context, location: LatLng): String? = withContext(Dispatchers.IO) {
    try {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        return@withContext addresses?.firstOrNull()?.getAddressLine(0)
    } catch (e: Exception) {
        Log.e("GoogleMapScreen", "Error geocoding: ${e.message}")
        null
    }
}