package fhnw.ws6c.theapp.model

import Profile
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import androidx.activity.ComponentActivity
import androidx.annotation.DrawableRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import fhnw.emoba.thatsapp.data.Image
import fhnw.emoba.thatsapp.data.gofileio.GoFileIOConnector
import fhnw.ws6c.R
import fhnw.ws6c.theapp.data.connectors.CameraAppConnector
import fhnw.ws6c.theapp.ui.notification
import fhnw.ws6c.theapp.ui.selectedItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

class ProfileModel( val context: ComponentActivity,
private val cameraAppConnector: CameraAppConnector) {

    var isLoadingProfile by mutableStateOf(false)


    // Profile Screen
    var name by mutableStateOf("Mona")
    var dateOfBirth by mutableStateOf("21.06.1998")
    var gender by mutableStateOf("Female")
    var age by mutableStateOf(24)
    var profileImageTakenURL by mutableStateOf("BAMv51")
    var profileImageTakenBitmap by mutableStateOf(loadImageFromFile(R.drawable.blanc_profile))
    //var personDescription by mutableStateOf("This is the description of my profil. Thank you for looking me up and I hope i can interesst you in my events")
    var personDescription by mutableStateOf("Describe yourself")



    private var me = Profile(
        UUID.randomUUID().toString(),
        //"a49f78c0-8a84-4e08-a9e9-0389f4d703ed",
        name,
        age,
        gender,
        Image(profileImageTakenURL),
        personDescription
    )

    fun getMyDetails(): Profile {
        return me
    }

    // temp vars
    var tempDate by mutableStateOf("")
    var tempName by mutableStateOf("")
    var tempDescription by mutableStateOf("")

    private val goFile: GoFileIOConnector = GoFileIOConnector()

    private fun loadImageFromFile(@DrawableRes id: Int) : ImageBitmap {
        return BitmapFactory.decodeResource(context.resources, id).asImageBitmap()
    }

    private val modelScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun uploadProfileImage(image: Bitmap) {

        println("old: "+profileImageTakenURL)
        modelScope.launch {
            goFile.uploadBitmapToGoFileIO(image,onSuccess =  { profileImageTakenURL=it })
            println("new: "+profileImageTakenURL)
            downloadImg()


        }
    }

    private fun downloadImg(){
        modelScope.launch {
            goFile.downloadBitmapFromGoFileIO(profileImageTakenURL,{ loadMyPic(it) })
        }
    }

    fun getProfileImageBitMapURL(image: Bitmap) {


        println("old: "+profileImageTakenURL)
        modelScope.launch {
            goFile.uploadBitmapToGoFileIO(image,  { profileImageTakenURL = it })
            println("new: "+profileImageTakenURL)
            me.image = Image(profileImageTakenURL)

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

    fun checkChanges() : Boolean{
        var profileHasChanged = false

        if (me.name != tempName) {
            me.name = tempName
            profileHasChanged = true
        }
        if(me.personDescription != tempDescription){
            me.personDescription = tempDescription
            profileHasChanged = true
        }
        if(me.gender != selectedItem){
            me.gender = selectedItem
            profileHasChanged = true
        }


        return profileHasChanged


    }

    fun checkValidity() : Boolean{
        var isValid: Boolean
        if (tempDate != "" && tempName != "" && tempDescription != "") {


            me.name = tempName
            dateOfBirth = tempDate
            getAge(dateOfBirth)

            me.gender = selectedItem
            me.image = Image(profileImageTakenURL)
            me.personDescription = tempDescription
            isValid = true

            if (me.age < 17){
                notification = "You can only join if you are over 16 years old"
                isValid = false

            }


        } else { notification = "Please make sure you fill out everything"
                isValid = false}


        return  isValid


    }






}