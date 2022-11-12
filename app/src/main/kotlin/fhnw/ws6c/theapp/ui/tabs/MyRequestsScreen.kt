package fhnw.ws6c.theapp.ui.tabs

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import fhnw.ws6c.theapp.ui.AllMessagesPanel
import fhnw.ws6c.theapp.ui.PostCard
import fhnw.ws6c.theapp.ui.theme.WorkshopSixAppTheme
import fhnw.ws6c.theapp.ui.theme.typography

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MyRequestsScreen(model: FoodBuddyModel) {
    WorkshopSixAppTheme(model.isDarkMode) {
        Scaffold(
            content = { MyRequestsBody(model) })
    }
}


@Composable
private fun MyRequestsBody(model: FoodBuddyModel) {

    Column() {

        MyRequests(model)

    }
}


@Composable
fun MyRequests(model: FoodBuddyModel) {
    Box() {
        if (model.acceptedPosts.isEmpty() && model.declinedPosts.isEmpty()) {
            Column(modifier = Modifier.fillMaxSize()) {

                Text(
                    style = typography.h3,
                    text = "No subsriptions yet",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                )
                Image(bitmap = model.loadImageFromFile(R.drawable.boy), contentDescription = "")
            }
        } else {
            Column() {
                AcceptedBody(model.acceptedPosts, model)
                DeclinedBody(model.declinedPosts, model)
            }

        }
    }
}

@Composable
private fun AcceptedBody(posts: List<Post>, model: FoodBuddyModel) {
    val scrollState = rememberLazyListState()
    Text(
        text = "Accepted",
        style = typography.h2,
        color = colors.primary,
        modifier = Modifier.padding(all = 10.dp)
    )
    LazyColumn(state = scrollState) {
        items(posts) {
            PostCard(it, model)
        }
    }
    LaunchedEffect(posts.size) {
        scrollState.animateScrollToItem(posts.size)
    }
}

@Composable
private fun DeclinedBody(posts: List<Post>, model: FoodBuddyModel) {
    val scrollState = rememberLazyListState()
    Text(
        text = "Declined",
        style = typography.h2,
        color = colors.primary,
        modifier = Modifier.padding(all = 10.dp)
    )
    LazyColumn(state = scrollState) {
        items(posts) {
            PostCard(it, model, false) // if declined, person cannot read the details of event
        }
    }
    LaunchedEffect(posts.size) {
        scrollState.animateScrollToItem(posts.size)
    }
}


