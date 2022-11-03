package fhnw.ws6c.theapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import fhnw.ws6c.theapp.model.FoodBuddyModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fhnw.ws6c.R
import fhnw.ws6c.theapp.model.Screen


var expanded by mutableStateOf(false)
var selectedItem by mutableStateOf("Female")
val genderList = listOf("Female", "Male", "Non-Binary", "Other")

var notification by mutableStateOf("")

var tempDate by mutableStateOf("")
var tempName by mutableStateOf("")
var tempGender by mutableStateOf("")

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
            Text(text = notification, style = TextStyle(Color.Red))
            Text(text = "Create your Profile",
                textAlign = TextAlign.Center,
                style = TextStyle(fontSize = 40.sp)
            )
            
            //  IMAGE
            if(fotoWasTaken) {
                Image(bitmap = profileImageTakenBitmap, contentDescription = "",
                    Modifier
                        .size(200.dp)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(5.dp)))

            } else {
                Image(
                    bitmap = profileImageTakenBitmap, contentDescription = "",
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
                LabelAndPlaceHolderAge(model, "Date Of Birth", "dd.mm.yyyy")
            }

            //  GENDER DROPDOWN
            DropDownMenuGender(model)


            Spacer(modifier = Modifier.height(150.dp))


            // TODO SAVE Profile Button and go to dashboard
            // TODO check if everything has been filled out
            Button(onClick = {
                // TODO Create Profile 
                checkValidityAndChangeScreen(model)

                
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
            value = tempName,
            onValueChange = {
                tempName = it
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
            value = tempDate,
            onValueChange = {
                tempDate = it
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
           } 
        }


    }

}


fun checkValidityAndChangeScreen(model: FoodBuddyModel) {
    with(model){

        if (tempDate != "" && tempName != "") {
            currentScreen = Screen.DASHBOARD

            me.name = tempName
            dateOfBirth = tempDate
            getAge(dateOfBirth)
            me.gender = selectedItem


            publishMyProfile()

        } else {
            notification = "Please make sure you fill out everything"
        }

    }

}


