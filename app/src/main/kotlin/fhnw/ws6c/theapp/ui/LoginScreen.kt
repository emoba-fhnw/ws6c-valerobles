package fhnw.ws6c.theapp.ui

import android.annotation.SuppressLint
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fhnw.ws6c.R
import fhnw.ws6c.theapp.model.FoodBuddyModel
import fhnw.ws6c.theapp.model.Screen
import fhnw.ws6c.theapp.ui.theme.WorkshopSixAppTheme
import fhnw.ws6c.theapp.ui.theme.typography


var expanded by mutableStateOf(false)
var selectedItem by mutableStateOf("Female")
val genderList = listOf("Female", "Male", "Non-Binary", "Other")

var notification by mutableStateOf("")





@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LoginScreen(model: FoodBuddyModel) {
    WorkshopSixAppTheme(model.isDarkMode) {
        Scaffold(
            content                      = {LoginBody(model)} ,
            isFloatingActionButtonDocked = true
        )
    }
}

@Composable
private fun LoginBody(model: FoodBuddyModel) {
    with(model){

        val launcher =
            rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
                var uri = it
                uri.let {
                    if (uri != null) {
                        if (Build.VERSION.SDK_INT < 28) {
                            modelProfile.profileImageTakenBitmap = MediaStore.Images.Media.getBitmap(
                                model.context.contentResolver,
                                uri
                            ).asImageBitmap()
                        photoWasTaken = true
                        } else {
                            val source = ImageDecoder.createSource(model.context.contentResolver, uri)
                            modelProfile.profileImageTakenBitmap = ImageDecoder.decodeBitmap(source).asImageBitmap()
                            getProfileImageBitMapURL(ImageDecoder.decodeBitmap(source))
                            photoWasTaken = true

                        }
                    }
                }
            }






        Column(
            Modifier
                .fillMaxSize()
                .background(colors.background)
                .verticalScroll(rememberScrollState()),
            Arrangement.Center,
            Alignment.CenterHorizontally) {
            Text(
                text = notification,
                style = typography.h4,
                color = Color.Red
            )
            Text(
                text = "Create your Profile",
                textAlign = TextAlign.Center,
                style = typography.h1,
                color = colors.primary
            )
            
            //  IMAGE
            if(photoWasTaken) {
                Image(bitmap = modelProfile.profileImageTakenBitmap, contentDescription = "",
                    Modifier
                        .size(width = 180.dp, height = 180.dp)
                        .padding(10.dp)
                        .clip(
                            RoundedCornerShape(20.dp)
                        ),
                    contentScale = ContentScale.Crop)

            } else {
                Image(
                    bitmap = loadImageFromFile(R.drawable.blanc_profile), contentDescription = "",
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
                onClick = { launcher.launch("image/*")},
                colors = buttonColors(backgroundColor = colors.primary, contentColor = Color.White),
                contentPadding = PaddingValues(
                    start = 30.dp,
                    top = 4.dp,
                    end = 30.dp,
                    bottom = 4.dp,
                ),
                shape = RoundedCornerShape(30)
                ) {
                Text(
                    text = "Upload",
                    style = typography.h4
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            //  NAME     // AGE
            Row() {
                LabelAndPlaceHolderName(model, "First Name", "Your name")
                Spacer(modifier = Modifier.width(30.dp))
                LabelAndPlaceHolderAge(model, "Date Of Birth", "dd.mm.yyyy")
            }
            Spacer(modifier = Modifier.height(10.dp))
            //  GENDER DROPDOWN
            Row(modifier = Modifier.align(Alignment.Start).padding(start = 50.dp)) {
                DropDownMenuGender(model)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.align(Alignment.Start).padding(start = 50.dp)) {
                DescriptonPerson(model)
            }



            Spacer(modifier = Modifier.height(50.dp))


            //  SAVE Profile Button and go to dashboard

            Button(
                onClick = { checkValidityAndChangeScreen(model) },
                colors = buttonColors(colors.primary, contentColor = Color.White),
                contentPadding = PaddingValues(
                    start = 30.dp,
                    top = 4.dp,
                    end = 30.dp,
                    bottom = 4.dp,
                ),
                shape = RoundedCornerShape(30),
                enabled = !isLoading
            ) {
                Text(
                    text = "Join other Food Buddies",
                    style = typography.h4
                )
            }
            Spacer(modifier = Modifier.height(50.dp))


        }
    }
}



@Composable
private fun LabelAndPlaceHolderName(model: FoodBuddyModel, label : String, placeholder: String) {
    with(model) {
        Column() {
            Text(
                text = "$label*",
                style = typography.h2,
                color = colors.secondary

            )
            Spacer(modifier = Modifier.height(10.dp))
            TextField(
                textStyle = typography.h3,
                modifier = Modifier
                    .width(140.dp)
                    .height(50.dp)
                    .padding(all = 0.dp),
                shape = RoundedCornerShape(15.dp),
                value = modelProfile.tempName,
                onValueChange = {
                    modelProfile.tempName = it
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = colors.surface,
                    focusedIndicatorColor =  Color.Transparent, //hide the indicator
                    unfocusedIndicatorColor = Color.Transparent),
                //label = { Text(text = label) },
                placeholder = { Text(
                    text = placeholder,
                    style = typography.h3
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
                style = typography.h2,
                color = colors.secondary

            )
            Spacer(modifier = Modifier.height(10.dp))
            TextField(
                textStyle = typography.h3,
                modifier = Modifier
                    .width(140.dp)
                    .height(50.dp)
                    .padding(all = 0.dp),
                shape = RoundedCornerShape(15.dp),
                value = modelProfile.tempDate,
                onValueChange = {
                    modelProfile.tempDate = it
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = colors.surface,
                    focusedIndicatorColor =  Color.Transparent, //hide the indicator
                    unfocusedIndicatorColor = Color.Transparent),
                //label = { Text(text = label) },
                placeholder = { Text(
                    text = placeholder,
                    style = typography.h3
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
                    style = typography.h2,
                    color = colors.secondary
                )
                Spacer(modifier = Modifier.height(10.dp))
                TextButton(
                    onClick = { expanded = true },
                    modifier = Modifier.width(100.dp),
                    colors = buttonColors(
                        backgroundColor = colors.surface,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(30)
                ) {
                    Row {
                        Text(
                            text = "$selectedItem",
                            style = typography.h3,
                            color = colors.secondary
                        )
                        Icon(
                            Icons.Default.ArrowDropDown,
                            contentDescription = "",
                            tint = colors.secondary
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
                        Text(
                            text = it,
                            color = colors.secondary
                        )

                    }
                }
            }
        }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DescriptonPerson(model: FoodBuddyModel) {
    with(model) {
        Column() {
            Text(
                style = typography.h2,
                text = "Description",
                color = colors.primary

            )
            Spacer(modifier = Modifier.height(10.dp))
            val keyboard = LocalSoftwareKeyboardController.current
            TextField(
                maxLines = 5,
                singleLine = false,
                textStyle = typography.h3,
                modifier = Modifier
                    .width(300.dp)
                    .height(100.dp)
                    .padding(all = 0.dp),
                shape = RoundedCornerShape(15.dp),
                value = modelProfile.tempDescription,
                onValueChange = {
                    modelProfile.tempDescription = it
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = colors.surface,
                    focusedIndicatorColor = Color.Transparent, //hide the indicator
                    unfocusedIndicatorColor = Color.Transparent
                ),
                placeholder = {
                    Text(
                        style = typography.h3,
                        text = modelProfile.personDescription
                    )
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { keyboard?.hide() })
            )
        }
    }
}


fun checkValidityAndChangeScreen(model: FoodBuddyModel) {
    with(model){

        if(modelProfile.checkValidity()){
            publishMyProfile()

            currentScreen = Screen.DASHBOARD

        }


    }

}


