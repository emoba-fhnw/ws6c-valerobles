package fhnw.ws6c.theapp.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.ImageDecoder
import android.icu.util.Calendar
import android.os.Build
import android.provider.MediaStore
import android.widget.DatePicker
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import fhnw.ws6c.theapp.data.Post
import fhnw.ws6c.theapp.model.FoodBuddyModel
import fhnw.ws6c.theapp.model.Screen
import java.util.*


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
fun PostCard(post: Post, model: FoodBuddyModel, clickable : Boolean = true) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(
            Modifier
                .padding(10.dp)
                .clickable {
                    if (clickable) {
                        model.currentPost = post
                        model.showBottomSheetInfo = true
                    }

                })
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
                    fontSize = 18.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .width(150.dp)
                    ,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.weight(0.4f))
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = "",
                    tint = Color(55, 107, 0),
                    modifier = Modifier
                        .size(10.dp)
                        .padding(end = 2.dp)
                )
                Text(
                    text = post.address,
                    fontSize = 10.sp,
                    color = Color(55,107,0),
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .width(50.dp)
                    ,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Icon(
                    Icons.Default.Schedule,
                    contentDescription = "",
                    tint = Color(55, 107, 0),
                    modifier = Modifier
                        .size(10.dp)
                        .padding(end = 2.dp)
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
                    modifier = Modifier
                        .size(10.dp)
                        .padding(end = 2.dp)
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
private fun PublishButton(model: FoodBuddyModel) {
    Button(
        onClick = {
            model.publishMyPost()
            model.showBottomSheetCreatePost = false
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
        Text(text = "Create")
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RestaurantInput(model: FoodBuddyModel) {
    with(model) {
        Column() {
            Text(
                text = "Restaurant Name",
                style = TextStyle(fontSize = 18.sp, color = Color(55, 107, 0))

            )
            Spacer(modifier = Modifier.height(10.dp))
            val keyboard = LocalSoftwareKeyboardController.current
            TextField(
                textStyle = TextStyle(fontSize = 15.sp),
                modifier = Modifier
                    .width(340.dp)
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
fun AddressInput(model: FoodBuddyModel) {
    with(model) {
        Column() {
            Text(
                text = "Address",
                style = TextStyle(fontSize = 18.sp, color = Color(55, 107, 0))

            )
            Spacer(modifier = Modifier.height(10.dp))
            val keyboard = LocalSoftwareKeyboardController.current
            TextField(
                textStyle = TextStyle(fontSize = 15.sp),
                modifier = Modifier
                    .width(340.dp)
                    .height(50.dp)
                    .padding(all = 0.dp),
                shape = RoundedCornerShape(15.dp),
                value = address,
                onValueChange = {
                    address = it
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(237, 237, 237),
                    focusedIndicatorColor =  Color.Transparent, //hide the indicator
                    unfocusedIndicatorColor = Color.Transparent),
                //label = { Text(text = label) },
                placeholder = { Text(
                    text = "Address",
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
            Icon(
                Icons.Default.CalendarMonth,
                contentDescription = "",
                tint = Color(55, 107, 0),
                modifier = Modifier
                    .size(25.dp)
                    .padding(end = 2.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = date)

            val context = LocalContext.current
            val year: Int
            val month: Int
            val day: Int

            val calendar = Calendar.getInstance()
            year = calendar.get(Calendar.YEAR)
            month = calendar.get(Calendar.MONTH)
            day = calendar.get(Calendar.DAY_OF_MONTH)
            calendar.time = Date()


            val datePickerDialog = DatePickerDialog(
                context,
                { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                    date = "$dayOfMonth.$month.$year"
                }, year, month, day
            )

            OutlinedButton(onClick = { datePickerDialog.show() }) {
                Text(text = "Pick Date")
            }
        }
    }
}








//            val keyboard = LocalSoftwareKeyboardController.current
//            TextField(
//                textStyle = TextStyle(fontSize = 15.sp),
//                modifier = Modifier
//                    .width(130.dp)
//                    .height(50.dp)
//                    .padding(all = 0.dp),
//                shape = RoundedCornerShape(15.dp),
//                value = date,
//                onValueChange = {
//                    date = it
//                },
//                colors = TextFieldDefaults.textFieldColors(
//                    backgroundColor = Color(237, 237, 237),
//                    focusedIndicatorColor =  Color.Transparent, //hide the indicator
//                    unfocusedIndicatorColor = Color.Transparent),
//                //label = { Text(text = label) },
//                placeholder = { Text(
//                    text = "dd.mm.yyyy",
//                    style = TextStyle(fontSize = 15.sp)
//                ) },
//                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
//                keyboardActions = KeyboardActions(onDone = { keyboard?.hide() })
//            )
//        }
//    }











@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TimeInput(model: FoodBuddyModel) {
    with(model) {
        Column() {
            Icon(
                Icons.Default.Schedule,
                contentDescription = "",
                tint = Color(55, 107, 0),
                modifier = Modifier
                    .size(25.dp)
                    .padding(end = 2.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = time)

            val mContext = LocalContext.current

            // Declaring and initializing a calendar
            val mCalendar = Calendar.getInstance()
            val mHour = mCalendar[Calendar.HOUR_OF_DAY]
            val mMinute = mCalendar[Calendar.MINUTE]


            // Creating a TimePicker dialod
            val mTimePickerDialog = TimePickerDialog(
                mContext,
                {_, mHour : Int, mMinute: Int ->
                    time = "$mHour:$mMinute"
                }, mHour, mMinute, false
            )
            OutlinedButton(onClick = { mTimePickerDialog.show() }) {
                Text(text = "Pick Time")
            }


        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MaxPeopleInput(model: FoodBuddyModel) {
    with(model) {
        Column() {
            Icon(
                Icons.Default.Groups,
                contentDescription = "",
                tint = Color(55, 107, 0),
                modifier = Modifier
                    .size(25.dp)
                    .padding(end = 2.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            val keyboard = LocalSoftwareKeyboardController.current
            TextField(
                textStyle = TextStyle(fontSize = 15.sp),
                modifier = Modifier
                    .width(60.dp)
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
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DescriptionInput(model: FoodBuddyModel) {
    with(model) {
        Column() {
            Text(
                text = "Description",
                style = TextStyle(fontSize = 18.sp, color = Color(55, 107, 0))

            )
            Spacer(modifier = Modifier.height(10.dp))
            val keyboard = LocalSoftwareKeyboardController.current
            TextField(
                maxLines = 5,
                singleLine = false,
                textStyle = TextStyle(fontSize = 15.sp),
                modifier = Modifier
                    .width(340.dp)
                    .height(100.dp)
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
fun EventImageUpload(model: FoodBuddyModel){

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
            var uri = it
            uri.let {
                if (uri != null) {
                    if (Build.VERSION.SDK_INT < 28) {
                        model.postImageBitmap = MediaStore.Images.Media.getBitmap(
                            model.context.contentResolver,
                            uri
                        ).asImageBitmap()
                        model.getEventImageBitMapURL(MediaStore.Images.Media.getBitmap(
                            model.context.contentResolver,
                            uri))
                    } else {
                        val source = ImageDecoder.createSource(model.context.contentResolver, uri)
                        model.postImageBitmap = ImageDecoder.decodeBitmap(source).asImageBitmap()
                        model.getEventImageBitMapURL(ImageDecoder.decodeBitmap(source))
                    }
                }
            }
        }

    Column() {
        Text("Event picture")
        Image(bitmap = model.postImageBitmap, contentDescription = "", Modifier.size(200.dp))
        Button(onClick = { launcher.launch("image/*") },modifier = Modifier
            .wrapContentSize()
            .padding(10.dp)
        ) {
            Text(text = "Pick Image From Gallery")
        }
    }




}



@Composable
fun BottomSheetCreate(model: FoodBuddyModel) {





    Column(Modifier.zIndex(1f)) {

        Box(modifier = Modifier
            .fillMaxHeight(0.2f)
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

            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                Arrangement.Center,
                Alignment.CenterHorizontally)
            {
                Text(
                    text = "Create Event",
                    fontSize = 30.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                RestaurantInput(model = model)
                Spacer(modifier = Modifier.height(10.dp))
                AddressInput(model = model)
                Spacer(modifier = Modifier.height(10.dp))
                Row() {
                    DateInput(model = model)
                    Spacer(modifier = Modifier.width(30.dp))
                    TimeInput(model = model)
                    Spacer(modifier = Modifier.width(30.dp))
                    MaxPeopleInput(model = model)
                }
                Spacer(modifier = Modifier.height(10.dp))
                DescriptionInput(model = model)
                Spacer(modifier = Modifier.height(10.dp))
                EventImageUpload(model= model)
                Spacer(modifier = Modifier.height(10.dp))
                PublishButton(model = model)

            }

        }

    }


}


/*@Composable
fun CreatePostFAB(model: FoodBuddyModel) {
    FloatingActionButton(backgroundColor = Color(245,245,245),onClick = {
        model.showBottomSheetCreatePost = true })
    { Icon(Icons.Filled.Add, "Create") }
}*/

@Composable
fun CreatePostFAB(model: FoodBuddyModel) {
    Button(
        onClick = { model.showBottomSheetCreatePost = true },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(55, 107, 0),
            contentColor = Color.White
        ),
        contentPadding = PaddingValues(5.dp),
        shape = RoundedCornerShape(30)
    )
    {
        Icon(Icons.Filled.Add,
            contentDescription = "Create",
            modifier = Modifier.size(50.dp))
    }
}
