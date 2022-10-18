package fhnw.ws6c.theapp.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import fhnw.ws6c.theapp.MqttConnector

object MqttModel {
    val title      = "MqttApp"
    val mqttBroker = "broker.hivemq.com"
    val topic      = "fhnw/emoba/mqttplayground"

    private val mqttConnector by lazy { MqttConnector(mqttBroker) }

    var messagesReceived  by mutableStateOf(0)
    var messagesPublished by mutableStateOf(0)

    fun connectAndSubscribe(){
        mqttConnector.connectAndSubscribe(topic        = topic,
            onNewMessage = { messagesReceived++ })
    }

    fun publish(){
        mqttConnector.publish(topic       = topic,
            message     = "hello",
            onPublished = { messagesPublished++ })
    }

}