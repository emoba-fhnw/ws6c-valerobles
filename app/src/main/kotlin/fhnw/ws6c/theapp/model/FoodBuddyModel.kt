package fhnw.ws6c.theapp.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import fhnw.emoba.thatsapp.data.Image
import fhnw.ws6c.theapp.MqttConnector
import fhnw.ws6c.theapp.data.Post

object FoodBuddyModel {
    val title      = "Food Buddy"
    val mqttBroker = "broker.hivemq.com"
    val mainTopic  = "fhnw/foodbuddy"
    val me         = "Valeria"

    val allPosts = mutableStateListOf<Post>()

    var notificationMessage by mutableStateOf("")
    var restaurantName      by mutableStateOf("Lorem ipsum")
    var description         by mutableStateOf("Hello")
    var image               by mutableStateOf("gcDyCD")
    var people              by mutableStateOf(0)
    var date                by mutableStateOf("10.11.2023")
    var time                by mutableStateOf("18:00")

    private val mqttConnector by lazy { MqttConnector(mqttBroker) }

    var currentScreen by mutableStateOf(Screen.DASHBOARD)



    fun connectAndSubscribe(){
        mqttConnector.connectAndSubscribe(
            topic        = mainTopic,
            onNewMessage = {
                allPosts.add(Post(it))

            },
            onError      = {_, p ->
                notificationMessage = p

            }
        )
    }

    fun publish(){
        val post = Post(me, restaurantName, description, Image(url= image), people, date, time)
        mqttConnector.publish(
            topic       = mainTopic,
            post     = post,
            onPublished = {
                post.downloadImageFromText()
                allPosts.add(post)
            })
    }


}