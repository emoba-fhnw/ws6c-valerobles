package fhnw.ws6c.theapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fhnw.ws6c.theapp.model.FoodBuddyModel

@Composable
fun MyEventsScreen(model: FoodBuddyModel) {

    Scaffold(
        content = { MyEventsBody(model) })
}



@Composable
private fun MyEventsBody(model: FoodBuddyModel) {

    Column() {

        AllMessagesPanel(posts = model.myCreatedPosts, modifier = Modifier.height(300.dp))

    }
}