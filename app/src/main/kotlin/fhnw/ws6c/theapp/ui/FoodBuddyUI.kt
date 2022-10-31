package fhnw.ws6c.theapp.ui

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import fhnw.ws6c.theapp.model.FoodBuddyModel
import fhnw.ws6c.theapp.model.Screen


@Composable
fun MqttUI(model: FoodBuddyModel) {

    Crossfade(targetState = model.currentScreen) { screen ->
        when (screen) {
            Screen.DASHBOARD -> {
                DashboardScreen(model = model)
            }
            Screen.LOGINSCREEN -> {

            }
            Screen.NOTIFICATIONS -> {

            }


        }



    }

}
