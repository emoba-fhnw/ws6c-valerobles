package fhnw.ws6c.theapp.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import fhnw.emoba.thatsapp.data.Image
import fhnw.emoba.thatsapp.data.gofileio.GoFileIOConnector
import fhnw.ws6c.theapp.MqttConnector
import fhnw.ws6c.theapp.data.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

object MqttModel {
    val title      = "Food Buddy"
    val mqttBroker = "broker.hivemq.com"
    val mainTopic  = "fhnw/foodbuddy"
    val me         = "Valeria"

    val allPosts = mutableStateListOf<Message>()

    var notificationMessage by mutableStateOf("")
    var restaurantName      by mutableStateOf("Lorem ipsum")
    var description         by mutableStateOf("Hello")
    var image               by mutableStateOf("e77c4f7c-391b-44f3-894f-5ab366fa8d19")

    private val mqttConnector by lazy { MqttConnector(mqttBroker) }


    fun connectAndSubscribe(){
        mqttConnector.connectAndSubscribe(
            topic        = mainTopic,
            onNewMessage = {
                print(it)
                allPosts.add(Message(it))

            },
            onError      = {_, p ->
                notificationMessage = p

            }
        )
    }

    fun publish(){
        val message = Message(me, restaurantName, description, Image(url= image))
        mqttConnector.publish(
            topic       = mainTopic,
            message     = message,
            onPublished = { allPosts.add(message) })
    }


}