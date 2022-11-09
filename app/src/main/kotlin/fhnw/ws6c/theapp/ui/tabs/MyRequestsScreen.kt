package fhnw.ws6c.theapp.ui.tabs

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fhnw.ws6c.theapp.data.Post
import fhnw.ws6c.theapp.model.FoodBuddyModel
import fhnw.ws6c.theapp.ui.AllMessagesPanel
import fhnw.ws6c.theapp.ui.PostCard

@Composable
fun MyRequestsScreen(model: FoodBuddyModel) {

    Scaffold(
        content = { MyRequestsBody(model) })
}



@Composable
private fun MyRequestsBody(model: FoodBuddyModel) {

    Column() {

        MyRequests(model)

    }
}




@Composable
fun MyRequests(model: FoodBuddyModel){
    Box(){
        if(model.mySubscribedPosts.isEmpty()){
            Text(text     = "No subsriptions yet",
                style    = MaterialTheme.typography.h4,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxSize()
            )
        } else {
            Column() {
                AcceptedBody(model.acceptedPosts,model)
                DeclinedBody(model.declinedPosts,model)
            }

        }
    }
}

@Composable
private fun AcceptedBody(posts : List<Post>, model: FoodBuddyModel){
    val scrollState = rememberLazyListState()
    Text(text = "Accepted")
    LazyColumn(state = scrollState){
        items(posts){
            PostCard(it, model)
        }
    }
    LaunchedEffect(posts.size){
        scrollState.animateScrollToItem(posts.size)
    }
}

@Composable
private fun DeclinedBody(posts : List<Post>, model: FoodBuddyModel){
    val scrollState = rememberLazyListState()
    Text(text = "Declined")
    LazyColumn(state = scrollState){
        items(posts){
            PostCard(it, model,false) // if declined, person cannot read the details of event
        }
    }
    LaunchedEffect(posts.size){
        scrollState.animateScrollToItem(posts.size)
    }
}


