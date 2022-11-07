package fhnw.ws6c.theapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fhnw.ws6c.theapp.model.FoodBuddyModel


@Composable
fun BottomSheetInfo(model: FoodBuddyModel) {
    with(model) {
        Column() {

            Box(modifier = Modifier
                .fillMaxHeight(0.3f)
                .fillMaxWidth()
                .background(Color.Gray.copy(0.5f))
                .blur(50.dp)
                .clickable { model.showBottomSheetInfo = false }) {

            }
            Box(
                modifier = Modifier
                    .fillMaxHeight(1f)
                    .fillMaxWidth()
                    .background(Color.White)
            ) {

                Column() {

                    Text(text = "THIS IS WHERE THE CURRENT CLICKED POST DETAILS SHOW")
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(currentPost!!.restaurantName)
                    Text(currentPost!!.description)
                    Text(currentPost!!.date)
                    Text(currentPost!!.peopleNumber.toString())
                    Image(currentPost!!.messageImage, contentDescription = "")


                    if((!mySubscribedPostsUUID.contains(currentPost!!.uuid)) and (currentPost!!.organizer.uuid != me.uuid)) {
                        Button(onClick = {
                            mySubscribedPosts.add(currentPost!!)
                            mySubscribedPostsUUID.add(currentPost!!.uuid)
                            showBottomSheetInfo = false
                            publishMyProfileToPost(currentPost!!.uuid)
                        } ) {
                            Text(text = "I want to join")
                        }

                    }

                    }


                }

            }

        }
    }





