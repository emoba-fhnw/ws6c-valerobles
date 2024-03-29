package fhnw.ws6c.theapp.ui

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import fhnw.ws6c.theapp.model.FoodBuddyModel
import fhnw.ws6c.theapp.model.Screen
import fhnw.ws6c.theapp.ui.tabs.TabsScreen


@Composable
fun FoodBuddyUI(model: FoodBuddyModel) {

    Crossfade(targetState = model.currentScreen) { screen ->
        when (screen) {
            Screen.DASHBOARD -> {
                DashboardScreen(model = model)
            }
            Screen.LOGINSCREEN -> {
                LoginScreen(model = model)
            }
            Screen.NOTIFICATIONS -> {

            }
            Screen.TABSCREEN -> {
                TabsScreen(model = model)
            }
            Screen.EDITSCREEN -> {
                EditProfile(model = model)
            }


        }



    }

}
