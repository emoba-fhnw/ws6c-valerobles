package fhnw.ws6c.theapp.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                with(currentPost!!) {
                    Column(
                        Modifier
                            .clip(RoundedCornerShape(25.dp, 25.dp))
                            .background(Color.White)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Image(
                            messageImage, contentDescription = "",
                            Modifier
                                .fillMaxWidth()
                                .size(160.dp)
                                .padding(0.dp)
                                .clip(RoundedCornerShape(25.dp, 25.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Column(Modifier.padding(horizontal = 30.dp)) {
                            Spacer(modifier = Modifier.height(20.dp))
                            if (acceptedPosts.contains(currentPost)) {
                                Text(
                                    text = "You're in!",
                                    fontSize = 30.sp,
                                    color = Color(55, 107, 0)
                                )
                                Spacer(modifier = Modifier.height(40.dp))
                            }
                            Row() {
                                Text(
                                    text = restaurantName,
                                    fontSize = 30.sp,
                                    color = Color(55, 107, 0)
                                )
                                Spacer(modifier = Modifier.width(20.dp))
                                Text(
                                    text = "$date @ $time",
                                    fontSize = 20.sp,
                                    color = Color(63, 74, 52)
                                )
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            Row() {
                                Text(
                                    text = address,
                                    fontSize = 20.sp,
                                    color = Color(63, 74, 52)
                                )
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            Row() {
                                Image(
                                    bitmap = organizer.profileImage,
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(70.dp)
                                        .clip(RoundedCornerShape(25.dp))
                                )
                                Column() {
                                    Text(
                                        text = "Organized by",
                                        fontSize = 16.sp,
                                        color = Color(63, 74, 52)
                                    )
                                    Text(
                                        text = restaurantName,
                                        fontSize = 25.sp,
                                        color = Color(55, 107, 0)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(description)
                            Spacer(modifier = Modifier.height(20.dp))
                            Text("$peopleNumber places open")
                            Spacer(modifier = Modifier.height(20.dp))

                            if (organizer.uuid != me.uuid) {
                                Button(
                                    onClick = {
                                        mySubscribedPosts.add(currentPost!!)
                                        mySubscribedPostsUUID.add(currentPost!!.uuid)
                                        showBottomSheetInfo = false
                                        publishMyProfileToPost(currentPost!!.uuid)
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Color(55, 107, 0),
                                        contentColor = Color.White
                                    ),
                                    contentPadding = PaddingValues(
                                        start = 30.dp,
                                        top = 4.dp,
                                        end = 30.dp,
                                        bottom = 4.dp,
                                    ),
                                    shape = RoundedCornerShape(30)
                                ) {
                                    Text(text = "I want to join")
                                }

                            }

                            if (mySubscribedPosts.contains(currentPost))
                                Text(text = "You have already subscribed to this event")
                        }

                    }

                }


            }

        }

    }
}





