package fhnw.ws6c.theapp.ui

import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import fhnw.ws6c.theapp.model.FoodBuddyModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fhnw.ws6c.R
import fhnw.ws6c.theapp.model.Screen

@Composable
fun LoginScreen(model: FoodBuddyModel) {
    MaterialTheme() {
        Scaffold(
            content                      = {LoginBody(model)} ,
            isFloatingActionButtonDocked = true
        )
    }




}

@Composable
fun LoginBody(model: FoodBuddyModel) {
    with(model){
        Column(
            Modifier
                .fillMaxSize(),
            Arrangement.Center,
            Alignment.CenterHorizontally) {
            Text(text = "Create your Profile",
                textAlign = TextAlign.Center,
                style = TextStyle(fontSize = 40.sp)
            )
            
            // TODO: IMAGE
            Image(bitmap = loadImageFromFile(R.drawable.blanc_profile), contentDescription = "",
            Modifier
                .size(200.dp)
                .padding(10.dp)
                .clip(RoundedCornerShape(5.dp)))
            // TODO IMAGE UPLOAD BUTTON
            Button(onClick = {
                //takeProfilePicture
            }) {
                Text(text = "Upload Picture")
            }
            // TODO NAME     // AGE
            
            // TODO GENDER DROPDOWN
            
            // TODO SAVE Profile Button and go to dashboard
            Button(onClick = {
                // TODO Create Profile 
                
                currentScreen = Screen.DASHBOARD
                
            }) {
                Text(text = "Join other Food Buddies")
            }


        }
    }
}


