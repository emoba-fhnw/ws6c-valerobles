package fhnw.ws6c.theapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import fhnw.ws6c.theapp.data.Post
import fhnw.ws6c.theapp.model.FoodBuddyModel
import fhnw.ws6c.theapp.model.Screen



@Composable
fun DashboardScreen(model: FoodBuddyModel) {


    val scaffoldState = rememberScaffoldState()
    MaterialTheme(colors = lightColors(primary = Color(40,80,0))) {
        Scaffold(scaffoldState = scaffoldState,
            topBar        = { Bar(model) },
            snackbarHost  = { NotificationHost(it) },
            content       = { Body(model) },
            floatingActionButton = { CreatePostFAB(model) },
            floatingActionButtonPosition = FabPosition.Center
        )
    }
    Notification(model, scaffoldState)
}

@Composable
private fun Bar(model: FoodBuddyModel) {
    with(model){
        TopAppBar{
            Text(text = title)
            IconButton(
                onClick = { model.currentScreen = Screen.TABSCREEN }) {
                Icon(Icons.Filled.Notifications, "") }
        }
    }
}

@Composable
private fun NotificationHost(state: SnackbarHostState) {
    SnackbarHost(state) { data ->
        Box(modifier         = Modifier.fillMaxHeight(),
            contentAlignment = Alignment.Center) {
            Snackbar(snackbarData = data)
        }
    }
}

@Composable
private fun Body(model: FoodBuddyModel){
    with(model){
        Column() {
            AllMessagesPanel(posts = allPosts, model = model)
        }

        if(showBottomSheetInfo){
            BottomSheetInfo(model = model)
        }
        if(showBottomSheetCreatePost){
            BottomSheetCreate(model = model)
        }
    }
}






@Composable
fun AllMessagesPanel(posts: List<Post>, model: FoodBuddyModel){
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
private fun AllMessages(posts : List<Post>,model: FoodBuddyModel){
    val scrollState = rememberLazyListState()
    LazyColumn(state = scrollState){
        items(posts){ PostCard(it, model) }
    }

    LaunchedEffect(posts.size){
        scrollState.animateScrollToItem(posts.size)
    }
}

@Composable
fun PostCard(post: Post, model: FoodBuddyModel) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(
            Modifier
                .padding(10.dp)
                .clickable {
                    model.currentPost = post
                    model.showBottomSheetInfo = true }) {
            Image(
                bitmap = post.messageImage, contentDescription = "",
                Modifier
                    .fillMaxWidth()
                    .size(120.dp)
                    .padding(0.dp)
                    .clip(RoundedCornerShape(25.dp, 25.dp)),
                contentScale = ContentScale.Crop
            )
            Row(
                Modifier
                    .clip(RoundedCornerShape(0.dp, 0.dp, 25.dp, 25.dp))
                    .background(color = Color(237, 237, 237))
            ) {
                Text(
                    text = post.restaurantName,
                    fontSize = 20.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(5.dp)
                )
                Spacer(Modifier.weight(0.5f))
                Text(
                    text = post.date,
                    fontSize = 10.sp,
                    color = Color(55,107,0),
                    modifier = Modifier.padding(5.dp)
                )
                Text(
                    text = "-",
                    fontSize = 10.sp,
                    color = Color(55,107,0),
                    modifier = Modifier.padding(5.dp)
                )
                Text(
                    text = post.time,
                    fontSize = 10.sp,
                    color = Color(55,107,0),
                    modifier = Modifier.padding(5.dp)
                )
                Text(
                    text = post.peopleNumber.toString(),
                    fontSize = 10.sp,
                    color = Color(55,107,0),
                    modifier = Modifier.padding(5.dp)
                )
            }
        }
    }

}



@Composable
private fun PublishButton(model: FoodBuddyModel){
    Button(onClick  = { model.publishMyPost()
                      model.showBottomSheetCreatePost = false},
        shape    = CircleShape,
    ) {
        Text("Post")
    }
}



@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun RestaurantInput(model: FoodBuddyModel){
    with(model){
        val keyboard = LocalSoftwareKeyboardController.current
        OutlinedTextField(value           = restaurantName,
            onValueChange   = {restaurantName = it},
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { keyboard?.hide() })
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun DescriptionInput(model: FoodBuddyModel){
    with(model){
        val keyboard = LocalSoftwareKeyboardController.current
        OutlinedTextField(value           = description,
            onValueChange   = { description = it},
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { keyboard?.hide() })
        )
    }
}

@Composable
private fun Notification(model: FoodBuddyModel, scaffoldState: ScaffoldState) {
    with(model){
        if (notificationMessage.isNotBlank()) {
            LaunchedEffect(scaffoldState.snackbarHostState){
                scaffoldState.snackbarHostState.showSnackbar(message     = notificationMessage,
                    actionLabel = "OK")
                notificationMessage = ""
            }
        }
    }


}



@Composable
fun BottomSheetCreate(model: FoodBuddyModel) {

    Column(Modifier.zIndex(1f)) {

        Box(modifier = Modifier
            .fillMaxHeight(0.3f)
            .fillMaxWidth()
            .background(Color.Gray.copy(0.5f))
            .blur(50.dp)
            .clickable { model.showBottomSheetCreatePost = false }){

        }
        Box(modifier = Modifier
            .fillMaxHeight(1f)
            .fillMaxWidth()
            .background(Color.White)
        ){

            Column() {

                Text(text = "THIS IS WHERE YOU CREATE A POST")
                RestaurantInput(model = model)
                DescriptionInput(model = model)
                PublishButton(model = model)

            }

        }

    }


}


@Composable
fun CreatePostFAB(model: FoodBuddyModel) {
    FloatingActionButton(backgroundColor = Color(245,245,245),onClick = {
        model.showBottomSheetCreatePost = true })
    { Icon(Icons.Filled.Add, "Create") }


}

