package fhnw.ws6c.theapp.model

import Profile
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
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
import fhnw.ws6c.R
import fhnw.ws6c.theapp.data.Post
import fhnw.ws6c.theapp.data.connectors.CameraAppConnector
import fhnw.ws6c.theapp.data.connectors.MqttConnector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

class FoodBuddyModel( val context: ComponentActivity,
                     private val cameraAppConnector: CameraAppConnector) {

    val title      = "Food Buddy"
    val mqttBroker = "broker.hivemq.com"
    val mainTopic  = "fhnw/foodbuddy/"
    val profileTopic = "profiles"
    val postsTopic = "posts/"



    val allPosts = mutableStateListOf<Post>()

    val myCreatedPosts = mutableStateListOf<Post>()

    val mySubscribedPosts = mutableStateListOf<Post>()
    val mySubscribedPostsUUID = mutableStateListOf<String>() // keep in case the posts should be edited




    var notificationMessage by mutableStateOf("")
    var restaurantName      by mutableStateOf("Lorem ipsum")
    var address                by mutableStateOf("Address")
    var description         by mutableStateOf("Hello")
    var postImage           by mutableStateOf("gcDyCD")
    var postImageBitmap     by mutableStateOf(loadImageFromFile(R.drawable.blanc_profile))
    var people              by mutableStateOf("0")
    var maxPeople           by mutableStateOf("5")
    var date                by mutableStateOf("10.11.23")
    var time                by mutableStateOf("18:00")
    var uuidPost            by mutableStateOf("")

    private val mqttConnector by lazy { MqttConnector(mqttBroker) }
    private val goFile: GoFileIOConnector = GoFileIOConnector()
    private val modelScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)


    var currentScreen by mutableStateOf(Screen.LOGINSCREEN)
    var currentTab by mutableStateOf(Tab.MYEVENTS)
    var currentPost: Post? by mutableStateOf(null)


    var isLoading by mutableStateOf(false)

    var photoToUpload by mutableStateOf("")

    var photoWasTaken by mutableStateOf(false)




    // Profile Screen
    var name by mutableStateOf("")
    var dateOfBirth by mutableStateOf("")
    var gender by mutableStateOf("")
    var age by mutableStateOf(0)
    var profileImageTakenURL by mutableStateOf("a9R00e")
    var profileImageTakenBitmap by mutableStateOf(loadImageFromFile(R.drawable.blanc_profile))

    val me         = Profile(
        UUID.randomUUID().toString(),
        name,
        age,
        gender,
        Image(profileImageTakenURL)
    )


    var showBottomSheetInfo by mutableStateOf(false)
    var showBottomSheetCreatePost by mutableStateOf(false)



    fun connectAndSubscribe(){

        mqttConnector.connectAndSubscribe(
            topic        = "$mainTopic$postsTopic+",
            onNewMessage = {
                val p  = Post(it)
                println("$mainTopic$postsTopic+")
                println("incoming post: "+ p)

                allPosts.add(p)
                //if (p.organizer.uuid == me.uuid)
                //    myCreatedPosts.add(p)
                if(mySubscribedPostsUUID.contains(p.uuid))
                    mySubscribedPosts.add(p)

            },
            onError      = {m, p ->
                notificationMessage = m.toString()

            }
        )


    }

    fun publishMyPost(){
        uuidPost = UUID.randomUUID().toString()
        val post = Post(uuidPost,me, restaurantName, address, description, Image(url= postImage), people.toInt(), maxPeople.toInt(), date, time)
        mqttConnector.publish(
            topic       = mainTopic+postsTopic,
            post     = post,
            onPublished = {
                post.downloadImageFromText()
                allPosts.add(post)
                myCreatedPosts.add(post)
                println(post.asJsonString())
            })
        subscribeToGetRegistration(uuidPost)
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
        photoWasTaken = true
        modelScope.launch {
            goFile.uploadBitmapToGoFileIO(image,onSuccess =  { profileImageTakenURL=it })
            downloadImg()
            isLoading = false

        }
    }

    fun downloadImg(){
        modelScope.launch {
            goFile.downloadBitmapFromGoFileIO(profileImageTakenURL,{ loadMyPic(it) })
        }
    }

    fun getEventImageBitMapURL(image: Bitmap) {

        isLoading = true
        modelScope.launch {
            goFile.uploadBitmapToGoFileIO(image, onSuccess = { postImage = it })
        }

    }

    private fun loadMyPic(image: Bitmap){
        profileImageTakenBitmap = image.asImageBitmap()
    }

    fun getAge(date: String) {
        if (date != "") {
            val simpledateformat = SimpleDateFormat("dd.MM.yyyy")
            val dob = simpledateformat.parse(date)
            val temp = simpledateformat.format(Date())
            val currentDate = simpledateformat.parse(temp)
            val diff: Long = currentDate.time - dob.time
            println(dob)
            println(currentDate)
            println(diff)
            me.age = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toInt()/365
        }

    }

    fun publishMyProfile() {
        mqttConnector.publishProfile(
            topic = mainTopic+profileTopic,
            message= me.asJson(),
            onPublished = {println(me.asJson())
                //me.downloadProfilePicture()
                println(me.asJson())})
        photoWasTaken=false
    }

    fun publishMyProfileToPost(uuid : String){
        mqttConnector.publishProfile(
            topic = "$mainTopic$postsTopic$uuid/",
            message= me.asJson(),
            onPublished = {println(me.asJson())
                //me.downloadProfilePicture()
                println(me.asJson())})
    }

    private fun subscribeToGetRegistration(uuidPostToSubscribe: String){
        println("$mainTopic$postsTopic$uuidPostToSubscribe/")
        mqttConnector.subscribe(
            topic = "$mainTopic$postsTopic$uuidPostToSubscribe/",
            onNewMessage = {
                val p = Profile(it)
                val temp = myCreatedPosts.find {post -> post.uuid ==uuidPostToSubscribe }?.profilesWantingToJoin
                if (temp != null) {
                    if(!temp.contains(p)){
                        myCreatedPosts.find {post -> post.uuid ==uuidPostToSubscribe }?.profilesWantingToJoin?.add(p)
                    }
                }
                //myCreatedPosts.find {post -> post.uuid ==uuidPostToSubscribe }?.profilesWantingToJoin?.add(p)
                println("Incoming profile"+ p)


            }

        )



    }




}