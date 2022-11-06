package fhnw.ws6c.theapp.data

import Profile
import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.asImageBitmap
import fhnw.emoba.thatsapp.data.Image
import fhnw.emoba.thatsapp.data.gofileio.GoFileIOConnector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.json.JSONObject

data class Post(
    val uuid: String,
    val organizer: Profile,
    val restaurantName: String,
    val city: String,
    val description: String,
    val image: Image,
    val peopleNumber: Int,
    val maxPeopleNumber: Int,
    val date: String,
    val time: String) {

    constructor(json : JSONObject): this(
                json.getString("uuid"),
                Profile(json.getJSONObject("organizer").getString("uuid"),
                    json.getJSONObject("organizer").getString("name"),
                    json.getJSONObject("organizer").getInt("age"),
                    json.getJSONObject("organizer").getString("gender"),
                    Image(url=json.getJSONObject("organizer").getString("image"))),
                json.getString("restaurantName"),
                json.getString("city"),
                json.getString("description"),
                Image(url = json.getString("image")),
                json.getInt("peopleNumber"),
                json.getInt("maxPeopleNumber"),
                json.getString("date"),
                json.getString("time")) {
                downloadImageFromText()

            }

    constructor(p: Post): this(
        uuid = p.uuid,
        organizer = p.organizer,
        restaurantName = p.restaurantName,
        city = p.city,
        description = p.description,
        image = p.image,
        peopleNumber = p.peopleNumber,
        maxPeopleNumber = p.maxPeopleNumber,
        date = p.date,
        time = p.time
    )




    val DEFAULT_ICON: Bitmap = Bitmap.createBitmap(
        120,
        120,
        Bitmap.Config.ALPHA_8
    )

    private val modelScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val goFile: GoFileIOConnector = GoFileIOConnector()

    var messageImage by mutableStateOf(DEFAULT_ICON.asImageBitmap())

    fun downloadImageFromText(){
        modelScope.launch {
            goFile.downloadBitmapFromGoFileIO(image.url,{ loadPic(it) })
        }
        organizer.downloadProfilePicture()
    }
    private fun loadPic(image: Bitmap){
        this.messageImage = image.asImageBitmap()
    }

    fun asJsonString(): String {
        return """
            {
            "uuid":     "$uuid",
            "organizer":  {  "uuid":  "${organizer.uuid}",
                              "name":  "${organizer.name}",
                              "age":  "${organizer.age}",
                              "age":  "${organizer.gender}",
                              "image":  "${organizer.image.url}", 
                            }, 
             "restaurantName": "$restaurantName",
             "city": "$city",
             "description": "$description",
             "image": "${image.url}",
             "peopleNumber": "$peopleNumber",
             "maxPeopleNumber": "$maxPeopleNumber",
             "date": "$date",
             "time": "$time"
            }
            """.trimIndent()
    }
}


/*

{"organizor": "Maria", "restaurantName": "Les Trois Rois", "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu" }
 */