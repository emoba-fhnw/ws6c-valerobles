package fhnw.ws6c.theapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            content       = { Body(model) }
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
private fun Body(model: FoodBuddyModel) {
    with(model) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val ( allFlapsPanel, restName, description, publishButton) = createRefs()


            AllMessagesPanel(allPosts, Modifier.constrainAs(allFlapsPanel) {
                width  = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
                top.linkTo(parent.top, 10.dp)
                start.linkTo(parent.start, 10.dp)
                end.linkTo(parent.end, 10.dp)
                bottom.linkTo(restName.top, 15.dp)
            })

            RestaurantInput(model, Modifier.constrainAs(restName){
                width = Dimension.fillToConstraints
                start.linkTo(parent.start, 10.dp)
                end.linkTo(parent.end, 10.dp)
                bottom.linkTo(description.top, 15.dp)
            })

            DescriptionInput(model, Modifier.constrainAs(description){
                width = Dimension.fillToConstraints
                start.linkTo(parent.start, 10.dp)
                end.linkTo(parent.end, 10.dp)
                bottom.linkTo(publishButton.top, 15.dp)
            })



            PublishButton(model, Modifier.constrainAs(publishButton) {
                width = Dimension.fillToConstraints
                start.linkTo(parent.start, 10.dp)
                end.linkTo(parent.end, 10.dp)
                bottom.linkTo(parent.bottom, 15.dp)
            })
        }
    }
}




@Composable
fun AllMessagesPanel(posts: List<Post>, modifier: Modifier){
    Box(){
        if(posts.isEmpty()){
            Text(text     = "No posts yet",
                style    = MaterialTheme.typography.h4,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            AllMessages(posts)
        }
    }
}

@Composable
private fun AllMessages(posts : List<Post>){
    val scrollState = rememberLazyListState()
    LazyColumn(state = scrollState){
        items(posts){ PostCard(it) }
    }

    LaunchedEffect(posts.size){
        scrollState.animateScrollToItem(posts.size)
    }
}

@Composable
fun PostCard(post: Post) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.padding(10.dp)) {
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
    Divider()
}



@Composable
private fun PublishButton(model: FoodBuddyModel, modifier: Modifier){
    Button(onClick  = { model.publishMyPost() },
        shape    = CircleShape,
        modifier = modifier
    ) {
        Text("Post")
    }
}



@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun RestaurantInput(model: FoodBuddyModel, modifier: Modifier){
    with(model){
        val keyboard = LocalSoftwareKeyboardController.current
        OutlinedTextField(value           = restaurantName,
            onValueChange   = {restaurantName = it},
            modifier        = modifier,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { keyboard?.hide() })
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun DescriptionInput(model: FoodBuddyModel, modifier: Modifier){
    with(model){
        val keyboard = LocalSoftwareKeyboardController.current
        OutlinedTextField(value           = description,
            onValueChange   = { description = it},
            modifier        = modifier,
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