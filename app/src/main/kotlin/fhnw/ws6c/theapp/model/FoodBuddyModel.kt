package fhnw.ws6c.theapp.model

import Profile
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.ComponentActivity
import androidx.annotation.DrawableRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import fhnw.emoba.thatsapp.data.Image
import fhnw.emoba.thatsapp.data.gofileio.GoFileIOConnector
import fhnw.ws6c.theapp.MqttConnector
import fhnw.ws6c.theapp.data.Post
import fhnw.ws6c.theapp.data.connectors.CameraAppConnector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.*

class FoodBuddyModel(private val context: ComponentActivity,
                     private val cameraAppConnector: CameraAppConnector) {
    val title      = "Food Buddy"
    val mqttBroker = "broker.hivemq.com"
    val mainTopic  = "fhnw/foodbuddy"
    val me         = Profile(
        UUID.randomUUID().toString(),
        "Valeria",
        23,
        Image("PnnNqF")
    )

    val allPosts = mutableStateListOf<Post>()

    var notificationMessage by mutableStateOf("")
    var restaurantName      by mutableStateOf("Lorem ipsum")
    var description         by mutableStateOf("Hello")
    var postImage               by mutableStateOf("gcDyCD")
    var people              by mutableStateOf(0)
    var date                by mutableStateOf("10.11.2023")
    var time                by mutableStateOf("18:00")

    private val mqttConnector by lazy { MqttConnector(mqttBroker) }
    private val goFile: GoFileIOConnector = GoFileIOConnector()
    private val modelScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)


    var currentScreen by mutableStateOf(Screen.LOGINSCREEN)

    var isLoading by mutableStateOf(false)

    var photoToUpload by mutableStateOf("")

    var fotoWasTaken by mutableStateOf(false)


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
        val post = Post(me, restaurantName, description, Image(url= postImage), people, date, time)
        mqttConnector.publish(
            topic       = mainTopic,
            post     = post,
            onPublished = {
                post.downloadImageFromText()
                allPosts.add(post)
            })
    }

    fun takePhoto() {

        cameraAppConnector.getBitmap(onSuccess  = { uploadImage(it) },
            onCanceled = { notificationMessage = "Kein neues Bild" })


    }

    fun uploadImage(imageTaken: Bitmap) {
        isLoading = true
        modelScope.launch {
            goFile.uploadBitmapToGoFileIO(imageTaken, onSuccess = {photoToUpload = it })
            isLoading = false
            // TODO: do something with photoToUpload
        }
    }


    fun loadImageFromFile(@DrawableRes id: Int) : ImageBitmap {
        return BitmapFactory.decodeResource(context.resources, id).asImageBitmap()
    }

    fun takeProfilePhotoAndUpdate() {

        cameraAppConnector.getBitmap(onSuccess  = { uploadProfileImage(it) },
            onCanceled = { notificationMessage = "Kein neues Bild" })


    }

    fun uploadProfileImage(image:Bitmap) {
        isLoading = true
        fotoWasTaken = true
        modelScope.launch {
            goFile.uploadBitmapToGoFileIO(image,onSuccess =  { me.image.url=it })
            downloadImg()
            isLoading = false

        }
    }

    fun downloadImg(){
        modelScope.launch {
            goFile.downloadBitmapFromGoFileIO(me.image.url,{ loadMyPic(it) })
        }
    }

    private fun loadMyPic(image: Bitmap){
        me.profileImage = image.asImageBitmap()
    }





}