package fhnw.ws6c.theapp.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fhnw.ws6c.theapp.model.FoodBuddyModel
import fhnw.ws6c.theapp.model.Screen


var expanded by mutableStateOf(false)
var selectedItem by mutableStateOf("Female")
val genderList = listOf("Female", "Male", "Non-Binary", "Other")

var notification by mutableStateOf("")

var tempDate by mutableStateOf("")
var tempName by mutableStateOf("")
var tempGender by mutableStateOf("")

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
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
                style = TextStyle(fontSize = 30.sp, color = Color(55, 107, 0))
            )
            
            //  IMAGE
            if(fotoWasTaken) {
                Image(bitmap = profileImageTakenBitmap, contentDescription = "",
                    Modifier
                        .size(width = 180.dp, height = 180.dp)
                        .padding(10.dp)
                        .clip(
                            RoundedCornerShape(20.dp)
                        ),
                    contentScale = ContentScale.Crop)

            } else {
                Image(
                    bitmap = profileImageTakenBitmap, contentDescription = "",
                    Modifier
                        .size(180.dp)
                        .padding(10.dp)
                        .clip(
                            RoundedCornerShape(20.dp)
                        ),
                    contentScale = ContentScale.Crop
                )

            }
            //  IMAGE UPLOAD BUTTON
            Button(
                onClick = { takeProfilePhotoAndUpdate() },
                colors = buttonColors(backgroundColor = Color(55, 107, 0), contentColor = Color.White),
                contentPadding = PaddingValues(
                    start = 30.dp,
                    top = 4.dp,
                    end = 30.dp,
                    bottom = 4.dp,
                ),
                shape = RoundedCornerShape(30)
                ) {
                Text(text = "Upload")
            }
            Spacer(modifier = Modifier.height(50.dp))
            //  NAME     // AGE
            Row() {
                LabelAndPlaceHolderName(model, "First Name", "Your name")
                Spacer(modifier = Modifier.width(30.dp))
                LabelAndPlaceHolderAge(model, "Date Of Birth", "dd.mm.yyyy")
            }
            Spacer(modifier = Modifier.height(50.dp))
            //  GENDER DROPDOWN
            Row(modifier = Modifier.align(Alignment.Start).padding(start = 40.dp)) {
                DropDownMenuGender(model)
                Spacer(Modifier.weight(0.5f))
            }



            Spacer(modifier = Modifier.height(50.dp))


            //  SAVE Profile Button and go to dashboard

            Button(
                onClick = { checkValidityAndChangeScreen(model) },
                colors = buttonColors(backgroundColor = Color(55, 107, 0), contentColor = Color.White),
                contentPadding = PaddingValues(
                    start = 30.dp,
                    top = 4.dp,
                    end = 30.dp,
                    bottom = 4.dp,
                ),
                shape = RoundedCornerShape(30)
            ) {
                Text(text = "Join other Food Buddies")
            }
            Spacer(modifier = Modifier.height(50.dp))


        }
    }
}



@Composable
fun LabelAndPlaceHolderName(model: FoodBuddyModel, label : String, placeholder: String) {
    with(model) {
        Column() {
            Text(
                text = "$label*",
                style = TextStyle(fontSize = 18.sp, color = Color(55, 107, 0))

            )
            Spacer(modifier = Modifier.height(10.dp))
            TextField(
                textStyle = TextStyle(fontSize = 15.sp),
                modifier = Modifier
                    .width(140.dp)
                    .height(50.dp)
                    .padding(all = 0.dp),
                shape = RoundedCornerShape(15.dp),
                value = tempName,
                onValueChange = {
                    tempName = it
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(237, 237, 237),
                    focusedIndicatorColor =  Color.Transparent, //hide the indicator
                    unfocusedIndicatorColor = Color.Transparent),
                //label = { Text(text = label) },
                placeholder = { Text(
                    text = placeholder,
                    style = TextStyle(fontSize = 15.sp)
                ) },
            )
        }
    }
}

@Composable
fun LabelAndPlaceHolderAge(model: FoodBuddyModel, label : String, placeholder: String) {
    with(model) {
        Column() {
            Text(
                text = "$label*",
                style = TextStyle(fontSize = 18.sp, color = Color(55, 107, 0))

            )
            Spacer(modifier = Modifier.height(10.dp))
            TextField(
                textStyle = TextStyle(fontSize = 15.sp),
                modifier = Modifier
                    .width(140.dp)
                    .height(50.dp)
                    .padding(all = 0.dp),
                shape = RoundedCornerShape(15.dp),
                value = tempDate,
                onValueChange = {
                    tempDate = it
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(237, 237, 237),
                    focusedIndicatorColor =  Color.Transparent, //hide the indicator
                    unfocusedIndicatorColor = Color.Transparent),
                //label = { Text(text = label) },
                placeholder = { Text(
                    text = placeholder,
                    style = TextStyle(fontSize = 15.sp)
                ) },
            )
        }
    }
}



@Composable
fun DropDownMenuGender(model: FoodBuddyModel){

        Box(modifier = Modifier.width(150.dp)) {
            Column() {
                Text(
                    text = "Gender*",
                    style = TextStyle(fontSize = 18.sp, color = Color(55, 107, 0))
                )
                Spacer(modifier = Modifier.height(10.dp))
                TextButton(
                    onClick = { expanded = true },
                    modifier = Modifier.width(100.dp),
                    colors = buttonColors(backgroundColor = Color(237, 237, 237), contentColor = Color.White),
                    shape = RoundedCornerShape(30)
                ) {
                    Row {
                        Text(
                            text = "$selectedItem",
                            style = TextStyle(fontSize = 15.sp, color = Color(55, 107, 0))
                        )
                        Icon(
                            Icons.Default.ArrowDropDown,
                            contentDescription = "",
                            tint = Color(55, 107, 0)
                        )
                    }
                }
            }


            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                genderList.forEach {
                    DropdownMenuItem(onClick = {
                        expanded = false
                        selectedItem = it
                    }) {
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


