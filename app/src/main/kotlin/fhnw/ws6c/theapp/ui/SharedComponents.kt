package fhnw.ws6c.theapp.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import fhnw.ws6c.R
import fhnw.ws6c.theapp.model.FoodBuddyModel
import fhnw.ws6c.theapp.ui.theme.WorkshopSixAppTheme
import fhnw.ws6c.theapp.ui.theme.typography


@Composable
fun BottomSheetInfo(model: FoodBuddyModel) {
    with(model) {
        WorkshopSixAppTheme(model.isDarkMode) {
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
                        .background(colors.surface)
                        //.clip(RoundedCornerShape(25.dp, 25.dp))
                ) {
                    with(currentPost!!) {
                        Column(
                            Modifier
                                //.clip(RoundedCornerShape(25.dp, 25.dp))
                                .background(colors.surface)
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
                                Spacer(modifier = Modifier.height(30.dp))
                                if (acceptedPosts.contains(currentPost)) {
                                    Text(
                                        text = "You're in!",
                                        style = typography.h1,
                                        color = colors.primary
                                    )
                                    Spacer(modifier = Modifier.height(40.dp))
                                }
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = restaurantName,
                                        style = typography.h1,
                                        color = colors.primary
                                    )
                                    Spacer(modifier = Modifier.weight(0.5f))
                                    Column() {
                                        Text(
                                            text = date,
                                            style = typography.h2,
                                            color = colors.onSurface
                                        )
                                        Text(
                                            text = "@ $time",
                                            style = typography.h2,
                                            color = colors.onSurface
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(30.dp))
                                Row() {
                                    Text(
                                        text = address,
                                        style = typography.h2,
                                        color = colors.onSurface
                                    )
                                }
                                Spacer(modifier = Modifier.height(30.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Image(
                                        bitmap = organizer.profileImage,
                                        contentDescription = "",
                                        modifier = Modifier
                                            .size(70.dp)
                                            .clip(RoundedCornerShape(15.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                    Spacer(modifier = Modifier.width(20.dp))
                                    Column() {
                                        Text(
                                            text = "Organized by",
                                            style = typography.h3,
                                            color = colors.onSurface
                                        )
                                        Text(
                                            text = organizer.name + ", " + organizer.age + ", " + organizer.gender,
                                            style = typography.h2,
                                            color = colors.primary
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(20.dp))
                                Text(
                                    text = description,
                                    style = typography.h4,
                                    color = colors.onSurface
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                Text(
                                    text = (maxPeopleNumber.minus(peopleNumber)).toString() + " places open",
                                    style = typography.h3,
                                    color = colors.primary
                                )
                                Spacer(modifier = Modifier.height(20.dp))

                                if (organizer.uuid != me.uuid && !mySubscribedPosts.contains(currentPost)) {
                                    Button(
                                        onClick = {
                                            mySubscribedPosts.add(currentPost!!)
                                            mySubscribedPostsUUID.add(currentPost!!.uuid)
                                            showBottomSheetInfo = false
                                            publishMyProfileToPost(currentPost!!.uuid)
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = colors.primary,
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
                                        Text(
                                            text = "I want to join",
                                            style = typography.h3,
                                        )
                                    }
                                    Spacer(Modifier.height(30.dp))
                                }

                                if (mySubscribedPosts.contains(currentPost))
                                    Text(
                                        text = "You have already subscribed to this event",
                                        style = typography.h3,
                                        color = colors.primary,
                                        modifier = Modifier.padding(bottom = 30.dp)
                                    )
                            }

                        }

                    }


                }

            }

        }
    }
}





