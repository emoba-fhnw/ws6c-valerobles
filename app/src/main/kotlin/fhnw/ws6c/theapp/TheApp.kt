package fhnw.ws6c.theapp

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import fhnw.ws6c.WorkshopSixApp
import fhnw.ws6c.theapp.data.connectors.CameraAppConnector
import fhnw.ws6c.theapp.model.FoodBuddyModel
import fhnw.ws6c.theapp.model.ProfileModel
import fhnw.ws6c.theapp.ui.FoodBuddyUI



object TheApp : WorkshopSixApp {
    private lateinit var model : FoodBuddyModel
    private lateinit var modelProfile : ProfileModel


    override fun initialize(activity: ComponentActivity) {
        val cameraAppConnector = CameraAppConnector(activity)
        modelProfile = ProfileModel(activity,cameraAppConnector)
        model = FoodBuddyModel(activity,cameraAppConnector, modelProfile)
        model.connectAndSubscribe()
    }

    @Composable
    override fun CreateUI() {
        FoodBuddyUI(model)
    }

}

