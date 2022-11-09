package fhnw.ws6c.theapp.ui

import Profile
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fhnw.ws6c.theapp.data.Post
import fhnw.ws6c.theapp.model.FoodBuddyModel

@Composable
fun MyEventsScreen(model: FoodBuddyModel) {

    Scaffold(
        content = { MyEventsBody(model) })
}



@Composable
private fun MyEventsBody(model: FoodBuddyModel) {

    Column() {

        AllMyEvents(model.myCreatedPosts,model)

    }
}




@Composable
fun AllMyEvents(posts: List<Post>, model: FoodBuddyModel){
    Box(){
        if(posts.isEmpty()){
            Text(text     = "No posts yet",
                style    = MaterialTheme.typography.h4,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxSize()
            )
        } else {
            AllMessages(posts,model)
        }
    }
}

@Composable
private fun AllMessages(posts : List<Post>, model: FoodBuddyModel){
    val scrollState = rememberLazyListState()
    LazyColumn(state = scrollState){
        items(posts){
            PostCard(it, model)
            ProfilesList(post = it, model = model)
        }
    }
    LaunchedEffect(posts.size){
        scrollState.animateScrollToItem(posts.size)
    }
}


@Composable
private fun ProfilesList(post: Post,model: FoodBuddyModel){

    Column() {
        post.profilesWantingToJoin.forEach {
            Profiles(it,model,post)
        }
    }

}


@Composable
private fun Profiles(profile: Profile,model: FoodBuddyModel,post: Post){
    Row (modifier = Modifier.padding(bottom = 10.dp)){
        Image(bitmap = profile.profileImage, contentDescription = "", modifier = Modifier.size(50.dp).padding(end = 5.dp))
        Text(profile.name)
        Button(onClick = { model.acceptPerson(profile.uuid,post.uuid) }, colors = ButtonDefaults.buttonColors(backgroundColor =Color.Green)) {
            Text(text = "Accept")
        }
        Button(onClick = { model.declinePerson(profile.uuid,post.uuid) },colors = ButtonDefaults.buttonColors(backgroundColor =Color.Red)) {
            Text(text = "Decline")
        }
    }
}




