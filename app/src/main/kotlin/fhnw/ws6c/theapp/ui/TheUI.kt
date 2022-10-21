package fhnw.ws6c.theapp.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import fhnw.ws6c.theapp.model.Message
import fhnw.ws6c.theapp.model.MqttModel



@Composable
fun MqttUI(model: MqttModel) {
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
private fun Bar(model: MqttModel) {
    with(model){
        TopAppBar(title = { Text(title) })
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
private fun Body(model: MqttModel) {
    with(model) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val ( allFlapsPanel, message, publishButton) = createRefs()

            /*Info(mqttBroker, Modifier.constrainAs(brokerInfo) {
                top.linkTo(parent.top, 10.dp)
                start.linkTo(parent.start, 10.dp)
            })

            Info(mainTopic, Modifier.constrainAs(topicInfo) {
                top.linkTo(brokerInfo.bottom, 10.dp)
                start.linkTo(parent.start, 10.dp)
            })*/

            AllFlapsPanel(allFlaps, Modifier.constrainAs(allFlapsPanel) {
                width  = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
                top.linkTo(parent.top, 10.dp)
                start.linkTo(parent.start, 10.dp)
                end.linkTo(parent.end, 10.dp)
                bottom.linkTo(message.top, 15.dp)
            })

            NewMessage(model, Modifier.constrainAs(message){
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
private fun AllFlapsPanel(flaps: List<Message>, modifier: Modifier){
    Box(modifier.border(width = 1.dp,
        brush = SolidColor(Color.Gray),
        shape = RectangleShape
    )){
        if(flaps.isEmpty()){
            Text(text     = "No posts yet",
                style    = MaterialTheme.typography.h4,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            AllFlaps(flaps)
        }
    }
}

@Composable
private fun AllFlaps(flaps : List<Message>){
    val scrollState = rememberLazyListState()
    LazyColumn(state = scrollState){
        items(flaps){ PostCard(it) }
    }

    LaunchedEffect(flaps.size){
        scrollState.animateScrollToItem(flaps.size)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SingleFlap(flap: Message){
    with(flap){
        ListItem(text  = { Text(restaurantName ) },
                overlineText = { Text(organizor) }
        )
        Divider()
    }
}

@Composable
fun PostCard(message: Message) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column {
            Text(text = message.organizor, fontSize = 10.sp, color = Color.Gray)
            Text(message.restaurantName)
            Text(message.description)

        }
    }
    Divider()
}



@Composable
private fun PublishButton(model: MqttModel, modifier: Modifier){
    Button(onClick  = { model.publish() },
        shape    = CircleShape,
        modifier = modifier
    ) {
        Text("Publish (${model.flapsPublished})")
    }
}

@Composable
private fun Info(text: String, modifier: Modifier){
    Text(text     = text,
        style    = MaterialTheme.typography.h6,
        modifier = modifier
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun NewMessage(model: MqttModel, modifier: Modifier){
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

@Composable
private fun Notification(model: MqttModel, scaffoldState: ScaffoldState) {
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
