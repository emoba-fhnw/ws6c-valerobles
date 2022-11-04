package fhnw.ws6c.theapp.ui.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import fhnw.ws6c.theapp.model.FoodBuddyModel
import fhnw.ws6c.theapp.ui.AllMessagesPanel

@Composable
fun MyRequestsScreen(model: FoodBuddyModel) {

    Scaffold(
        content = { MyRequestsBody(model) })
}



@Composable
private fun MyRequestsBody(model: FoodBuddyModel) {

    Column() {

        AllMessagesPanel(posts = model.mySubscribedPosts,model)

    }
}