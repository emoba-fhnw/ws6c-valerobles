package fhnw.ws6c.theapp.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import fhnw.ws6c.theapp.MqttConnector

object MqttModel {
    val title      = "Food Buddy"
    val mqttBroker = "broker.hivemq.com"
    val mainTopic  = "fhnw/foodbuddy"
    val me         = "Valeria"

    val allPosts = mutableStateListOf<Message>()

    var notificationMessage by mutableStateOf("")
    var restaurantName      by mutableStateOf("Lorem ipsum")
    var description         by mutableStateOf("Hello")

    private val mqttConnector by lazy { MqttConnector(mqttBroker) }

    fun connectAndSubscribe(){
        mqttConnector.connectAndSubscribe(
            topic        = mainTopic,
            onNewMessage = { allPosts.add(Message(it))

            },
            onError      = {_, p ->
                notificationMessage = p

            }
        )
    }

    fun publish(){
        val message = Message(me, restaurantName, description)
        mqttConnector.publish(
            topic       = mainTopic,
            message     = message,
            onPublished = { allPosts.add(message) })
    }

}