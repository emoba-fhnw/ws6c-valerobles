package fhnw.ws6c.theapp.ui

import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.R
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetTest() {

    BoxWithConstraints() {

        BottomSheetScaffold(
            sheetPeekHeight = 96.dp,
            topBar = {
                TopAppBar {
                    Text(text = "Wassertemperaturen", fontSize = 20.sp)
                }
            },
            sheetContent = {
                Column(
                    modifier = Modifier
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "OST Badewiese", fontSize = 20.sp)
                    Text("Der beliebte Badeplatz direkt hinter der Hochschule")
                }
            }) {
            Box(Modifier.background(Color.LightGray)) {

            }
        }
    }
}