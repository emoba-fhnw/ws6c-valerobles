package fhnw.ws6c.theapp.ui

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import fhnw.ws6c.theapp.model.FoodBuddyModel

@Composable
fun MyEventsScreen(model: FoodBuddyModel) {

    Scaffold(
        content = { MyEventsBody(model) })
}



@Composable
private fun MyEventsBody(model: FoodBuddyModel) {

}