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
import java.util.UUID

data class Post(
    val uuid: String,
    val organizor: Profile,
    val restaurantName: String,
    val description: String,
    val image: Image,
    val peopleNumber: Int,
    val date: String,
    val time: String) {

    constructor(json : JSONObject): this(
                json.getString("uuid"),
                Profile(json.getJSONObject("organizor").getString("uuid"),
                    json.getJSONObject("organizor").getString("name"),
                    json.getJSONObject("organizor").getInt("age"),
                    json.getJSONObject("organizor").getString("gender"),
                    Image(url=json.getJSONObject("organizor").getString("image"))),
                json.getString("restaurantName"),
                json.getString("description"),
                Image(url = json.getString("image")),
                json.getInt("peopleNumber"),
                json.getString("date"),
                json.getString("time")) {
                downloadImageFromText()
            }

    constructor(p: Post): this(
        uuid = p.uuid,
        organizor = p.organizor,
        restaurantName = p.restaurantName,
        description = p.description,
        image = p.image,
        peopleNumber = p.peopleNumber,
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
    }
    private fun loadPic(image: Bitmap){
        this.messageImage = image.asImageBitmap()
    }

    fun asJsonString(): String {
        return """
            {
            "uuid":     "$uuid",
            "organizor":  {  "uuid":  "${organizor.uuid}",
                              "name":  "${organizor.name}",
                              "age":  "${organizor.age}",
                              "age":  "${organizor.gender}",
                              "image":  "${organizor.image.url}", 
                            }, 
             "restaurantName": "$restaurantName",
             "description": "$description",
             "image": "${image.url}",
             "peopleNumber": "$peopleNumber",
             "date": "$date",
             "time": "$time"
            }
            """.trimIndent()
    }
}


/*

{"organizor": "Maria", "restaurantName": "Les Trois Rois", "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu" }
 */