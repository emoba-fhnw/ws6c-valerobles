package fhnw.ws6c.theapp.model

import Profile
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import androidx.activity.ComponentActivity
import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
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
import fhnw.ws6c.theapp.data.PostStatus
import fhnw.ws6c.theapp.data.Status
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
    val mainTopic  = "test/foodbuddy/"
    val profileTopic = "profiles"
    val postsTopic = "posts/"



    val allPosts = mutableStateListOf<Post>()

    val myCreatedPosts = mutableStateListOf<Post>()

    val mySubscribedPosts = mutableStateListOf<Post>()
    val mySubscribedPostsUUID = mutableStateListOf<String>() // keep in case the posts should be edited




    var notificationMessage by mutableStateOf("")
    var restaurantName      by mutableStateOf("Lorem ipsum")
    var address             by mutableStateOf("Address")
    var description         by mutableStateOf("Hello")
    var postImageURL           by mutableStateOf("ppC1ux")
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

    var isDarkMode by mutableStateOf(false)

    var themeSwitchIcon by mutableStateOf(Icons.Filled.DarkMode)


    // Profile Screen
    var name by mutableStateOf("Mona")
    var dateOfBirth by mutableStateOf("21.06.1998")
    var gender by mutableStateOf("Female")
    var age by mutableStateOf(24)
    var profileImageTakenURL by mutableStateOf("IXB28E")
    var profileImageTakenBitmap by mutableStateOf(loadImageFromFile(R.drawable.blanc_profile))

    var me         = Profile(
        UUID.randomUUID().toString(),
        //"a49f78c0-8a84-4e08-a9e9-0389f4d703ed",
        name,
        age,
        gender,
        Image(profileImageTakenURL)
    )



    var showBottomSheetInfo by mutableStateOf(false)
    var showBottomSheetCreatePost by mutableStateOf(false)

    var acceptedPosts = mutableStateListOf<Post>()
    var declinedPosts = mutableStateListOf<Post>()

    fun connectAndSubscribe(){
        mqttConnector.connectAndSubscribe(
            topic        = "$mainTopic$postsTopic+",
            onNewMessage = {
                print("incoming post")
                val p  = Post(it)
                p.organizer.downloadProfilePicture()
                allPosts.add(p)
                if(mySubscribedPostsUUID.contains(p.uuid)) // for edit event edit feature
                    mySubscribedPosts.add(p)

            },
            onError      = {m, p ->
                notificationMessage = m.toString()

            }
        )


    }

    fun publishMyPost(){
        uuidPost = UUID.randomUUID().toString()
        val post = Post(uuidPost,me, restaurantName, address, description, Image(url= postImageURL), people.toInt(), maxPeople.toInt(), date, time)
        mqttConnector.publishPost(
            topic       = mainTopic+postsTopic,
            post     = post,
            onPublished = {
                post.downloadImageFromText()
                allPosts.add(post)
                myCreatedPosts.add(post)
                println(post.asJsonString())
            })
        subscribeToGetRegistrations(uuidPost)
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

    private fun uploadProfileImage(image:Bitmap) {
        isLoading = true
        photoWasTaken = true
        println("old: "+profileImageTakenURL)
        modelScope.launch {
            goFile.uploadBitmapToGoFileIO(image,onSuccess =  { profileImageTakenURL=it })
            println("new: "+profileImageTakenURL)
            downloadImg()
            isLoading = false

        }
    }

    private fun downloadImg(){
        modelScope.launch {
            goFile.downloadBitmapFromGoFileIO(profileImageTakenURL,{ loadMyPic(it) })
        }
    }

    fun getEventImageBitMapURL(image: Bitmap) {

        isLoading = true
        modelScope.launch {
            goFile.uploadBitmapToGoFileIO(image,  { postImageURL = it })
        }

    }

    fun getProfileImageBitMapURL(image: Bitmap) {

        isLoading = true
        modelScope.launch {
            goFile.uploadBitmapToGoFileIO(image,  { profileImageTakenURL = it })
        }

    }

    private fun loadMyPic(image: Bitmap){
        me.profileImage = image.asImageBitmap()
        profileImageTakenBitmap = image.asImageBitmap()
    }

    fun getAge(date: String) {
        if (date != "") {
            val simpledateformat = SimpleDateFormat("dd.MM.yyyy")
            val dob = simpledateformat.parse(date)
            val temp = simpledateformat.format(Date())
            val currentDate = simpledateformat.parse(temp)
            val diff: Long = currentDate.time - dob.time
            me.age = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toInt()/365
        }

    }

    fun publishMyProfile() {
        mqttConnector.publishProfile(
            topic = mainTopic+profileTopic,
            message= me.asJson(),
            onPublished = {
                println(me.asJson())})
        photoWasTaken=false
    }

    fun publishMyProfileToPost(uuid : String){
        mqttConnector.publishProfile(
            topic = "$mainTopic$postsTopic$uuid/",
            message= me.asJson(),
            onPublished = {
                println(me.asJson())})
        subscribeToGetUpdate(uuid)
    }

    private fun subscribeToGetRegistrations(uuidPostToSubscribe: String){
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

    private fun subscribeToGetUpdate(uuidPostToSubscribe: String){
        mqttConnector.subscribe(
            topic = mainTopic+postsTopic+uuidPostToSubscribe+"/"+me.uuid+"/",
            onNewMessage = {

            val ps = PostStatus(it)


            if (ps.status == Status.ACCEPTED)
                me.acceptedStatus.add(ps)
                println(me.acceptedStatus)
                getAcceptedPost()
            if (ps.status == Status.DECLINED)
                me.declinedStatus.add(ps)
                getDeclinedPosts()

            }
        )
    }

    private fun publishPostUpdate(uuidPerson : String, postStatus: PostStatus){
        mqttConnector.publishUpdate(
            topic = "$mainTopic$postsTopic$uuidPost/$uuidPerson/",
            update = postStatus,
            onPublished = {

                val prof = myCreatedPosts.find {post -> post.uuid ==uuidPost }?.profilesWantingToJoin?.find { profile -> profile.uuid == uuidPerson }
                myCreatedPosts.find {post -> post.uuid ==uuidPost }?.profilesWantingToJoin?.remove(prof)
            }

        )
    }

    fun acceptPerson(uuidPerson: String,uuidPost: String) {
        val ps = PostStatus(Status.ACCEPTED,uuidPost)
        publishPostUpdate(uuidPerson,ps)
        myCreatedPosts.find { p -> p.uuid ==uuidPost }?.addPerson()

    }
    fun declinePerson(uuidPerson: String,uuidPost: String) {
        val ps = PostStatus(Status.DECLINED,uuidPost)
        publishPostUpdate(uuidPerson,ps)
    }



    fun getAcceptedPost(){

        me.acceptedStatus.forEach { ap ->
            mySubscribedPosts.forEach { p ->
                if (p.uuid == ap.postUUID)
                acceptedPosts.add(p)

            }
        }

    }

    fun getDeclinedPosts(){

        me.declinedStatus.forEach { ap ->
            mySubscribedPosts.forEach { p ->
                if (p.uuid == ap.postUUID){
                    declinedPosts.add(p)
                    allPosts.remove(p)
                }



            }
        }
    }






}