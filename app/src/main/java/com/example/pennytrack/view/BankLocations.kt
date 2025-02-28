package com.example.pennytrack.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pennytrack.R
import com.example.pennytrack.ui.theme.md_theme_light_primary
import com.example.pennytrack.ui.theme.md_theme_light_surface
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

// Data class to represent bank locations
data class BankLocation(
    val name: String,
    val address: String,
    val latLng: LatLng,
    val branchType: String = "Main Branch"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BankLocations(navController: NavController) {
    // List of Thailand's major banks
    val thailandBanks = listOf(
        BankLocation("Bangkok Bank", "333 Silom Road, Bangrak", LatLng(13.7222, 100.5260)),
        BankLocation("Kasikorn Bank", "1 Soi Rat Burana 27/1", LatLng(13.7230, 100.5312)),
        BankLocation("Siam Commercial Bank", "9 Ratchadapisek Road", LatLng(13.7964, 100.5572)),
        BankLocation("Krung Thai Bank", "35 Sukhumvit Road", LatLng(13.7651, 100.5622)),
        BankLocation("Bank of Ayudhya", "1222 Rama III Road", LatLng(13.7633, 100.5412)),
        BankLocation("TMB Bank", "3000 Phahonyothin Road", LatLng(13.8308, 100.5594)),
        BankLocation("Siam City Bank", "1101 New Petchburi Road", LatLng(13.7470, 100.5631)),
        BankLocation("Thanachart Bank", "444 MBK Center", LatLng(13.7455, 100.5311)),
        BankLocation("UOB Bank", "191 South Sathorn Road", LatLng(13.7215, 100.5204)),
        BankLocation("CIMB Thai", "44 Langsuan Road", LatLng(13.7413, 100.5421)),
        BankLocation("Kiatnakin Bank", "209 KKP Tower", LatLng(13.7362, 100.5516)),
        BankLocation("Land and Houses Bank", "1 Q.House Lumpini", LatLng(13.7271, 100.5419)),
        BankLocation("ICBC Thai", "622 Emporium Tower", LatLng(13.7331, 100.5698)),
        BankLocation("Tisco Bank", "48 North Sathorn Road", LatLng(13.7241, 100.5281)),
        BankLocation("Standard Chartered", "90 Sathorn Thani 2", LatLng(13.7236, 100.5331)),
        BankLocation("Government Savings Bank", "470 Phaholyothin Road", LatLng(13.8463, 100.5714)),
        BankLocation("Bank for Agriculture", "2346 Phahonyothin Road", LatLng(13.8522, 100.5684)),
        BankLocation("Export-Import Bank", "1193 Exim Building", LatLng(13.7198, 100.5607)),
        BankLocation("Islamic Bank of Thailand", "66 Sukhumvit 43", LatLng(13.7407, 100.5764)),
        BankLocation("Thai Credit Bank", "123 Thai Credit Tower", LatLng(13.7530, 100.5340))
    )

    // Create a CameraPositionState to focus on Thailand
    val cameraPositionState = rememberCameraPositionState {
        // Bangkok coordinates as center point
        position = CameraPosition.fromLatLngZoom(LatLng(13.7563, 100.5018), 10f)
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.bank_locations),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    mapType = MapType.NORMAL
                ),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = true,
                    myLocationButtonEnabled = true
                )
            ) {
                // Add markers for all banks in the list
                thailandBanks.forEach { bank ->
                    Marker(
                        state = MarkerState(position = bank.latLng),
                        title = bank.name,
                        snippet = bank.address
                    )
                }
            }
        }
    }
}