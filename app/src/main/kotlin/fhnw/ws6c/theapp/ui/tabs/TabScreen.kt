package fhnw.ws6c.theapp.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import fhnw.ws6c.theapp.model.FoodBuddyModel
import fhnw.ws6c.theapp.model.Screen
import fhnw.ws6c.theapp.model.Tab
import fhnw.ws6c.theapp.ui.tabs.MyRequestsScreen


@Composable
fun TabsScreen(model: FoodBuddyModel) {
    MaterialTheme {
        Scaffold(
            topBar = { Bar(model) },
            content = { Body(model) },
        )
    }

    if(model.showBottomSheetInfo){
        BottomSheetInfo(model = model)
    }
    if(model.showBottomSheetCreatePost){
        BottomSheetCreate(model = model)
    }
}

@Composable
private fun Bar(model: FoodBuddyModel) {
    TopAppBar {
        Text(text = "Notifications")
        IconButton(
            onClick = { model.currentScreen = Screen.DASHBOARD }) {
            Icon(Icons.Filled.Home, "") }
        IconButton(
            onClick = { /* TODO: Switch Dark/Light Mode*/ }) {
            Icon(Icons.Filled.DarkMode, "") }

    }
}

@Composable
private fun Body(model: FoodBuddyModel){

    with(model) {
        Column {
            TabRow(selectedTabIndex = currentTab.ordinal,backgroundColor = Color.White) {
                for (tab in Tab.values()) {
                    Tab(
                        text = { Text(tab.title) },
                        selected = tab == currentTab,
                        onClick = { currentTab = tab }
                    )

                }

            }

            Crossfade(targetState = model.currentTab) { tab ->
                when (tab) {
                    Tab.MYEVENTS -> {
                        MyEventsScreen(model = model)


                    }
                    Tab.MYREQUESTS -> {
                       MyRequestsScreen(model = model)

                    }
                }

            }

        }


    }

}