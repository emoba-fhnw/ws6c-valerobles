package fhnw.ws6c.theapp.ui

import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import fhnw.ws6c.theapp.model.FoodBuddyModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fhnw.ws6c.R
import fhnw.ws6c.theapp.model.Screen


var expanded by mutableStateOf(false)
var selectedItem by mutableStateOf("Female")
val genderList = listOf("Female", "Male", "Non-Binary", "Other")



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
            
            //  IMAGE
            if(fotoWasTaken) {
                Image(bitmap = me.profileImage, contentDescription = "",
                    Modifier
                        .size(200.dp)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(5.dp)))

            } else {
                Image(
                    bitmap = loadImageFromFile(R.drawable.blanc_profile), contentDescription = "",
                    Modifier
                        .size(200.dp)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(5.dp))
                )

            }
            //  IMAGE UPLOAD BUTTON
            Button(onClick = {
                takeProfilePhotoAndUpdate()
            }) {
                Text(text = "Upload Picture")
            }
            //  NAME     // AGE
            Row() {
                LabelAndPlaceHolderName(model, "First Name", "Your name")
                Spacer(modifier = Modifier.width(30.dp))
                LabelAndPlaceHolderAge(model, "Date Of Birth", "dd/mm/yyyy")
            }

            //  GENDER DROPDOWN
            DropDownMenuGender(model)


            Spacer(modifier = Modifier.height(150.dp))


            // TODO SAVE Profile Button and go to dashboard
            // TODO check if everything has been filled out
            Button(onClick = {
                // TODO Create Profile 
                
                currentScreen = Screen.DASHBOARD
                
            }) {
                Text(text = "Join other Food Buddies")
            }


        }
    }
}

@Composable
fun LabelAndPlaceHolderName(model: FoodBuddyModel, label : String, placeholder: String) {
    with(model) {
        TextField(
            modifier = Modifier.width(170.dp),
            value = name,
            onValueChange = {
                name = it
            },
            label = { Text(text = label) },
            placeholder = { Text(text = placeholder) },
        )
    }
}

@Composable
fun LabelAndPlaceHolderAge(model: FoodBuddyModel, label : String, placeholder: String) {
    with(model) {
        TextField(
            modifier = Modifier.width(160.dp),
            value = dateOfBirth,
            onValueChange = {
                dateOfBirth = it
            },
            label = { Text(text = label) },
            placeholder = { Text(text = placeholder) },
        )
    }
}



@Composable
fun DropDownMenuGender(model: FoodBuddyModel){

    Box {
        TextButton(onClick = { expanded = true}) {
            Row {
                Text(text = "$selectedItem")
                Icon(Icons.Default.ArrowDropDown, contentDescription = "")
            }
        } 
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
           genderList.forEach{
               DropdownMenuItem(onClick = { 
                   expanded = false
                   selectedItem = it }) {
                   Text(text = it)
                   
               }

             model.gender = selectedItem
           } 
        }


    }




}
