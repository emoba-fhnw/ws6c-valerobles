package fhnw.ws6c.theapp.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import fhnw.ws6c.theapp.MqttConnector

object MqttModel {
    val title      = "Food Buddy"
    val mqttBroker = "broker.hivemq.com"
    val mainTopic  = "fhnw/emoba/flutterapp"
    val me         = "Geissen Peter"

    val allFlaps = mutableStateListOf<Message>()

    var notificationMessage by mutableStateOf("")
    var flapsPublished      by mutableStateOf(0)
    var restaurantName      by mutableStateOf("Lorem ipsum")
    var description         by mutableStateOf("Hello")

    private val mqttConnector by lazy { MqttConnector(mqttBroker) }

    fun connectAndSubscribe(){
        mqttConnector.connectAndSubscribe(
            topic        = mainTopic,
            onNewMessage = { allFlaps.add(Message(it))

            },
            onError      = {_, p ->
                notificationMessage = p

            }
        )
    }

    fun publish(){
        mqttConnector.publish(
            topic       = mainTopic,
            message     = Message(organizor  = me,
                restaurantName = restaurantName,
                description = description),
            onPublished = { flapsPublished++ })
    }

}