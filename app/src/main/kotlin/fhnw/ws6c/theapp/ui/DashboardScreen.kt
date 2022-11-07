package fhnw.ws6c.theapp.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.*
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
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import fhnw.ws6c.theapp.data.Post
import fhnw.ws6c.theapp.model.FoodBuddyModel
import fhnw.ws6c.theapp.model.Screen


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
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
            Text(
                text = title,
                fontSize = 30.sp,
            )
            Spacer(Modifier.weight(0.5f))
            IconButton(
                onClick = { /* TODO: Switch Dark/Light Mode*/ }) {
                Icon(Icons.Filled.DarkMode, "") }
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
    Box(Modifier.border(BorderStroke(0.dp, Color.Transparent))){
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
                    model.showBottomSheetInfo = true })
        {
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
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(0.dp, 0.dp, 25.dp, 25.dp))
                    .background(color = Color(237, 237, 237))
                    .padding(start = 10.dp)
                    .height(50.dp))
            {
                Text(
                    text = post.restaurantName,
                    fontSize = 20.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 15.dp)
                )
                Spacer(Modifier.weight(0.5f))
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = "",
                    tint = Color(55, 107, 0),
                    modifier = Modifier.size(10.dp).padding(end = 2.dp)
                )
                Text(
                    text = post.city,
                    fontSize = 10.sp,
                    color = Color(55,107,0),
                    modifier = Modifier.padding(end = 10.dp)
                )
                Icon(
                    Icons.Default.Schedule,
                    contentDescription = "",
                    tint = Color(55, 107, 0),
                    modifier = Modifier.size(10.dp).padding(end = 2.dp)
                )
                Text(
                    text = post.date,
                    fontSize = 10.sp,
                    color = Color(55,107,0),
                    modifier = Modifier.padding(end = 2.dp)
                )
                Text(
                    text = "|",
                    fontSize = 10.sp,
                    color = Color(55,107,0),
                    modifier = Modifier.padding(end = 2.dp)
                )
                Text(
                    text = post.time,
                    fontSize = 10.sp,
                    color = Color(55,107,0),
                    modifier = Modifier.padding(end = 10.dp)
                )
                Icon(
                    Icons.Default.Groups,
                    contentDescription = "",
                    tint = Color(55, 107, 0),
                    modifier = Modifier.size(10.dp).padding(end = 2.dp)
                )
                Text(
                    text = post.peopleNumber.toString(),
                    fontSize = 10.sp,
                    color = Color(55,107,0),
                    modifier = Modifier.padding(end = 0.dp)
                )
                Text(
                    text = "/",
                    fontSize = 10.sp,
                    color = Color(55,107,0),
                    modifier = Modifier.padding(end = 0.dp)
                )
                Text(
                    text = post.maxPeopleNumber.toString(),
                    fontSize = 10.sp,
                    color = Color(55,107,0),
                    modifier = Modifier.padding(end = 15.dp)
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
fun RestaurantInput(model: FoodBuddyModel) {
    with(model) {
        Column() {
            Text(
                text = "Restaurant Name*",
                style = TextStyle(fontSize = 18.sp, color = Color(55, 107, 0))

            )
            Spacer(modifier = Modifier.height(10.dp))
            val keyboard = LocalSoftwareKeyboardController.current
            TextField(
                textStyle = TextStyle(fontSize = 15.sp),
                modifier = Modifier
                    .width(140.dp)
                    .height(50.dp)
                    .padding(all = 0.dp),
                shape = RoundedCornerShape(15.dp),
                value = restaurantName,
                onValueChange = {
                    restaurantName = it
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(237, 237, 237),
                    focusedIndicatorColor =  Color.Transparent, //hide the indicator
                    unfocusedIndicatorColor = Color.Transparent),
                //label = { Text(text = label) },
                placeholder = { Text(
                    text = "Your Event Location",
                    style = TextStyle(fontSize = 15.sp)
                ) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { keyboard?.hide() })
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CityInput(model: FoodBuddyModel) {
    with(model) {
        Column() {
            Text(
                text = "City*",
                style = TextStyle(fontSize = 18.sp, color = Color(55, 107, 0))

            )
            Spacer(modifier = Modifier.height(10.dp))
            val keyboard = LocalSoftwareKeyboardController.current
            TextField(
                textStyle = TextStyle(fontSize = 15.sp),
                modifier = Modifier
                    .width(140.dp)
                    .height(50.dp)
                    .padding(all = 0.dp),
                shape = RoundedCornerShape(15.dp),
                value = city,
                onValueChange = {
                    city = it
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(237, 237, 237),
                    focusedIndicatorColor =  Color.Transparent, //hide the indicator
                    unfocusedIndicatorColor = Color.Transparent),
                //label = { Text(text = label) },
                placeholder = { Text(
                    text = "City Name",
                    style = TextStyle(fontSize = 15.sp)
                ) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { keyboard?.hide() })
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DateInput(model: FoodBuddyModel) {
    with(model) {
        Column() {
            Text(
                text = "Date*",
                style = TextStyle(fontSize = 18.sp, color = Color(55, 107, 0))

            )
            Spacer(modifier = Modifier.height(10.dp))
            val keyboard = LocalSoftwareKeyboardController.current
            TextField(
                textStyle = TextStyle(fontSize = 15.sp),
                modifier = Modifier
                    .width(140.dp)
                    .height(50.dp)
                    .padding(all = 0.dp),
                shape = RoundedCornerShape(15.dp),
                value = date,
                onValueChange = {
                    date = it
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(237, 237, 237),
                    focusedIndicatorColor =  Color.Transparent, //hide the indicator
                    unfocusedIndicatorColor = Color.Transparent),
                //label = { Text(text = label) },
                placeholder = { Text(
                    text = "dd.mm.yyyy",
                    style = TextStyle(fontSize = 15.sp)
                ) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { keyboard?.hide() })
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TimeInput(model: FoodBuddyModel) {
    with(model) {
        Column() {
            Text(
                text = "Time*",
                style = TextStyle(fontSize = 18.sp, color = Color(55, 107, 0))

            )
            Spacer(modifier = Modifier.height(10.dp))
            val keyboard = LocalSoftwareKeyboardController.current
            TextField(
                textStyle = TextStyle(fontSize = 15.sp),
                modifier = Modifier
                    .width(140.dp)
                    .height(50.dp)
                    .padding(all = 0.dp),
                shape = RoundedCornerShape(15.dp),
                value = time,
                onValueChange = {
                    time = it
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(237, 237, 237),
                    focusedIndicatorColor =  Color.Transparent, //hide the indicator
                    unfocusedIndicatorColor = Color.Transparent),
                //label = { Text(text = label) },
                placeholder = { Text(
                    text = "hh:mm",
                    style = TextStyle(fontSize = 15.sp)
                ) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { keyboard?.hide() })
            )
        }
    }
}

/*@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MaxPeopleInput(model: FoodBuddyModel) {
    with(model) {
        Column() {
            Text(
                text = "Maximal Persons*",
                style = TextStyle(fontSize = 18.sp, color = Color(55, 107, 0))

            )
            Spacer(modifier = Modifier.height(10.dp))
            val keyboard = LocalSoftwareKeyboardController.current
            TextField(
                textStyle = TextStyle(fontSize = 15.sp),
                modifier = Modifier
                    .width(140.dp)
                    .height(50.dp)
                    .padding(all = 0.dp),
                shape = RoundedCornerShape(15.dp),
                value = maxPeople,
                onValueChange = {
                    maxPeople = it
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(237, 237, 237),
                    focusedIndicatorColor =  Color.Transparent, //hide the indicator
                    unfocusedIndicatorColor = Color.Transparent),
                //label = { Text(text = label) },
                placeholder = { Text(
                    text = "0",
                    style = TextStyle(fontSize = 15.sp)
                ) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { keyboard?.hide() })
            )
        }
    }
}*/

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DescriptionInput(model: FoodBuddyModel) {
    with(model) {
        Column() {
            Text(
                text = "Description*",
                style = TextStyle(fontSize = 18.sp, color = Color(55, 107, 0))

            )
            Spacer(modifier = Modifier.height(10.dp))
            val keyboard = LocalSoftwareKeyboardController.current
            TextField(
                textStyle = TextStyle(fontSize = 15.sp),
                modifier = Modifier
                    .width(140.dp)
                    .height(50.dp)
                    .padding(all = 0.dp),
                shape = RoundedCornerShape(15.dp),
                value = description,
                onValueChange = {
                    description = it
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(237, 237, 237),
                    focusedIndicatorColor =  Color.Transparent, //hide the indicator
                    unfocusedIndicatorColor = Color.Transparent),
                //label = { Text(text = label) },
                placeholder = { Text(
                    text = "Describe your Event",
                    style = TextStyle(fontSize = 15.sp)
                ) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { keyboard?.hide() })
            )
        }
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

