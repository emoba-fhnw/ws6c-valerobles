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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import fhnw.ws6c.theapp.model.FoodBuddyModel
import fhnw.ws6c.theapp.model.Screen
import fhnw.ws6c.theapp.ui.theme.WorkshopSixAppTheme
import fhnw.ws6c.theapp.ui.theme.typography


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun EditProfile(model: FoodBuddyModel) {
    WorkshopSixAppTheme(model.isDarkMode) {
        Scaffold(
            content                      = {Body(model)} ,
            isFloatingActionButtonDocked = true,
            topBar = { Bar(model) }
        )
    }
}

@Composable
private fun Body(model: FoodBuddyModel) {
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
                .padding(horizontal = 60.dp),
            Arrangement.Center,
            Alignment.CenterHorizontally) {
            Text(
                text = notification,
                style = typography.h4,
                color = Color.Red
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
                    bitmap = modelProfile.profileImageTakenBitmap, contentDescription = "",
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
            Spacer(modifier = Modifier.height(20.dp))
            //  NAME     // AGE
            Row() {
                LabelAndPlaceHolderName(model, "First Name", me.name)
                Spacer(modifier = Modifier.weight(1F))
                Text(
                    text = "Age: " + me.age.toString(),
                    style = typography.h2,
                    color = colors.secondary

                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            Row(modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 0.dp)) {

                Text(
                    text = "Gender:",
                    style = typography.h2,
                    color = colors.secondary

                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = me.gender,
                    style = typography.h2,
                    color = colors.primary

                )

            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 0.dp)) {
                DescriptonPerson(model)
            }



            Spacer(modifier = Modifier.height(30.dp))


            //  SAVE Profile Button and go to dashboard

            Button(
                onClick = { model.saveChanges() },
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
                    text = "Update",
                    style = typography.h4
                )
            }
            Spacer(modifier = Modifier.height(50.dp))


        }
    }
}





@Composable
private fun Bar(model: FoodBuddyModel) {
    with(model) {
        TopAppBar(
            backgroundColor = colors.background,
            modifier = Modifier
                .shadow(elevation = 20.dp, spotColor = colors.onSurface)
                .height(70.dp)
                .clip(RoundedCornerShape(0.dp, 0.dp, 10.dp, 10.dp)),
        ) {
            IconButton(
                onClick = { model.currentScreen = Screen.DASHBOARD}) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "",
                    tint = colors.primary,
                    modifier = Modifier.size(30.dp)
                )
            }
            Spacer(Modifier.weight(0.5f))
            Text(
                text = "Edit your profile",
                style = typography.h1,
                color = colors.onSurface,
                modifier = Modifier.padding(10.dp)
            )
            Spacer(Modifier.weight(0.5f))
            IconButton(
                onClick = {
                    isDarkMode = !isDarkMode;
                    if (isDarkMode) {
                        themeSwitchIcon = Icons.Filled.LightMode;
                    } else {
                        themeSwitchIcon = Icons.Filled.DarkMode;

                    }
                },
            ) {
                Icon(
                    themeSwitchIcon,
                    contentDescription = "",
                    tint = colors.primary,
                    modifier = Modifier.size(20.dp)
                )
            }

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




