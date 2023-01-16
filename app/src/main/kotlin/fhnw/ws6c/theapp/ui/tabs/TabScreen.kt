package fhnw.ws6c.theapp.ui.tabs

import android.annotation.SuppressLint
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fhnw.ws6c.theapp.model.FoodBuddyModel
import fhnw.ws6c.theapp.model.Screen
import fhnw.ws6c.theapp.model.Tab
import fhnw.ws6c.theapp.ui.BottomSheetCreate
import fhnw.ws6c.theapp.ui.BottomSheetInfo
import fhnw.ws6c.theapp.ui.BottomSheetProfileInfo
import fhnw.ws6c.theapp.ui.theme.WorkshopSixAppTheme
import fhnw.ws6c.theapp.ui.theme.typography


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TabsScreen(model: FoodBuddyModel) {
    WorkshopSixAppTheme(model.isDarkMode) {
        Scaffold(
            topBar = { Bar(model) },
            content = { Body(model) },
        )
    }

    if (model.showBottomSheetInfo) {
        BottomSheetInfo(model = model)
    }
    if (model.showBottomSheetCreatePost) {
        BottomSheetCreate(model = model)
    } 
    if (model.showBottomSheetProfile){
        BottomSheetProfileInfo(model = model)
    }
}

@Composable
private fun Bar(model: FoodBuddyModel) {
    with(model) {
        TopAppBar(
            backgroundColor = colors.background,
            modifier = Modifier
                .shadow(elevation = 20.dp, spotColor = colors.onSurface)
                .height(70.dp)
                .clip(RoundedCornerShape(0.dp, 0.dp, 10.dp, 10.dp)),
        ) {
            Text(
                text = "Notifications",
                style = typography.h1,
                color = colors.onSurface,
                modifier = Modifier.padding(10.dp)
            )
            Spacer(Modifier.weight(0.5f))
            IconButton(
                onClick = {
                    isDarkMode = !isDarkMode;
                    if (isDarkMode) {
                        themeSwitchIcon = Icons.Filled.LightMode;
                    } else {
                        themeSwitchIcon = Icons.Filled.DarkMode;
                    }
                },
            ) {
                Icon(
                    themeSwitchIcon,
                    contentDescription = "",
                    tint = colors.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
            IconButton(
                onClick = { model.currentScreen = Screen.DASHBOARD }) {
                Icon(
                    Icons.Filled.Home,
                    contentDescription = "",
                    tint = colors.primary,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}

@Composable
private fun Body(model: FoodBuddyModel) {

    with(model) {
        Column {
            TabRow(
                selectedTabIndex = currentTab.ordinal,
                backgroundColor = colors.background
            ) {
                for (tab in Tab.values()) {
                    Tab(
                        text = { Text(
                            text = tab.title,
                            style = typography.h2
                        ) },
                        selected = tab == currentTab,
                        onClick = { currentTab = tab },
                        selectedContentColor = colors.primary,
                        unselectedContentColor = colors.secondary,
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