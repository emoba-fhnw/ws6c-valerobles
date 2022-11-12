package fhnw.ws6c.theapp.ui.tabs

import Profile
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fhnw.ws6c.R
import fhnw.ws6c.theapp.data.Post
import fhnw.ws6c.theapp.model.FoodBuddyModel
import fhnw.ws6c.theapp.ui.PostCard
import fhnw.ws6c.theapp.ui.theme.WorkshopSixAppTheme
import fhnw.ws6c.theapp.ui.theme.typography

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MyEventsScreen(model: FoodBuddyModel) {
    WorkshopSixAppTheme(model.isDarkMode) {
        Scaffold(
            content = { MyEventsBody(model) })
    }
}


@Composable
private fun MyEventsBody(model: FoodBuddyModel) {

    Column() {

        AllMyEvents(model.myCreatedPosts, model)

    }
}


@Composable
fun AllMyEvents(posts: List<Post>, model: FoodBuddyModel) {
    Box() {
        if (posts.isEmpty()) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    style = typography.h3,
                    text = "No posts yet",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                )
                Image(bitmap = model.loadImageFromFile(R.drawable.boy), contentDescription = "")
            }
        } else {
            AllMessages(posts, model)
        }
    }
}

@Composable
private fun AllMessages(posts: List<Post>, model: FoodBuddyModel) {
    val scrollState = rememberLazyListState()
    LazyColumn(state = scrollState) {
        items(posts) {
            PostCard(it, model, true)
            //ProfilesList(post = it, model = model, showProfiles = true)
        }
    }
    LaunchedEffect(posts.size) {
        scrollState.animateScrollToItem(posts.size)
    }
}


@Composable
public fun ProfilesList(post: Post, model: FoodBuddyModel) {

    Column() {
        post.profilesWantingToJoin.forEach {
            Profiles(it, model, post)
        }
    }

}


@Composable
private fun Profiles(profile: Profile, model: FoodBuddyModel, post: Post) {
    Row(modifier = Modifier.padding(bottom = 10.dp)) {
        Image(
            bitmap = profile.profileImage,
            contentDescription = "",
            modifier = Modifier
                .size(50.dp)
                .padding(end = 5.dp)
        )
        Text(profile.name)
        Button(
            onClick = { model.acceptPerson(profile.uuid, post.uuid) },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colors.primary,
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(
                start = 15.dp,
                top = 2.dp,
                end = 15.dp,
                bottom = 2.dp,
            ),
            shape = RoundedCornerShape(30)
        ) {
            Text(
                text = "Accept",
                style = typography.h3
            )
        }
        Button(
            onClick = { model.declinePerson(profile.uuid, post.uuid) },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colors.error,
                contentColor = colors.onError
            ),
            contentPadding = PaddingValues(
                start = 15.dp,
                top = 2.dp,
                end = 15.dp,
                bottom = 2.dp,
            ),
            shape = RoundedCornerShape(30)
        ) {
            Text(
                text = "Decline",
                style = typography.h3
            )
        }
    }
}




