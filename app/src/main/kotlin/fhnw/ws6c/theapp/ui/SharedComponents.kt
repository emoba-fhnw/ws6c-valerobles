package fhnw.ws6c.theapp.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import fhnw.ws6c.theapp.model.FoodBuddyModel


@Composable
fun BottomSheetInfo(model: FoodBuddyModel) {
    with(model) {
        Column(Modifier.zIndex(1f)) {

            Box(modifier = Modifier
                .fillMaxHeight(0.2f)
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
                    .clip(RoundedCornerShape(25.dp, 25.dp))
            ) {
                with(currentPost!!){
                    Column(
                        Modifier
                            .clip(RoundedCornerShape(25.dp, 25.dp))
                            .background(Color.White)
                            .verticalScroll(rememberScrollState())) {
                        Image(
                            messageImage, contentDescription = "",
                            Modifier
                                .fillMaxWidth()
                                .size(160.dp)
                                .padding(0.dp)
                                .clip(RoundedCornerShape(25.dp, 25.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Text(text = "THIS IS WHERE THE CURRENT CLICKED POST DETAILS SHOW")
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(restaurantName)
                        Text(description)
                        Text(date)
                        Text(peopleNumber.toString())

                        Text(organizer.name)
                        Image(bitmap = organizer.profileImage, contentDescription = "", modifier = Modifier.size(50.dp))



                        if(organizer.uuid != me.uuid) {
                            Button(onClick = {
                                mySubscribedPosts.add(currentPost!!)
                                mySubscribedPostsUUID.add(currentPost!!.uuid)
                                showBottomSheetInfo = false
                                publishMyProfileToPost(currentPost!!.uuid)
                            } ) {
                                Text(text = "I want to join")
                            }


                        }
                        
                        if (mySubscribedPosts.contains(currentPost))
                            Text(text = "You have already subscribed to this event")
                        if(acceptedPosts.contains(currentPost))
                            Text(text = "You have been accepted to join this event")

                    }

                    }


                }

            }

        }
    }





