package fhnw.ws6c.theapp

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import fhnw.ws6c.EmobaApp
import fhnw.ws6c.theapp.model.FoodBuddyModel

import fhnw.ws6c.theapp.ui.MqttUI


object TheApp : EmobaApp {
    private lateinit var model : FoodBuddyModel

    override fun initialize(activity: ComponentActivity) {
        model = FoodBuddyModel
        model.connectAndSubscribe()
    }

    @Composable
    override fun CreateUI() {
        MqttUI(model)
    }

}

