package fhnw.ws6c.theapp

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import fhnw.ws6c.EmobaApp
import fhnw.ws6c.theapp.model.MqttModel

import fhnw.ws6c.theapp.ui.MqttUI


object TheApp : EmobaApp {
    private lateinit var model : MqttModel

    override fun initialize(activity: ComponentActivity) {
        model = MqttModel
        model.connectAndSubscribe()
    }

    @Composable
    override fun CreateUI() {
        MqttUI(model)
    }

}

