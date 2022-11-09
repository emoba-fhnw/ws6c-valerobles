import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.asImageBitmap
import fhnw.emoba.thatsapp.data.Image
import fhnw.emoba.thatsapp.data.gofileio.GoFileIOConnector
import fhnw.ws6c.theapp.data.Post
import fhnw.ws6c.theapp.data.PostStatus
import fhnw.ws6c.theapp.data.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.json.JSONObject

data class Profile(val uuid: String,
                   var name: String,
                   var age: Int,
                   var gender: String,
                   var image: Image) {

    val DEFAULT_ICON: Bitmap = Bitmap.createBitmap(
        120,
        120,
        Bitmap.Config.ALPHA_8
    )

    var profileImage by mutableStateOf(DEFAULT_ICON.asImageBitmap())
    private val modelScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val goFile: GoFileIOConnector = GoFileIOConnector()

    var postStatus = mutableStateListOf<PostStatus>()

    constructor(jsonObject: JSONObject) : this(
        uuid = jsonObject.getString("uuid"),
        name = jsonObject.getString("name"),
        age = jsonObject.getInt("age"),
        gender = jsonObject.getString("gender"),
        image = Image(url=jsonObject.getString("image"))) {
        downloadProfilePicture()
    }

    fun downloadProfilePicture(){
        modelScope.launch {
            goFile.downloadBitmapFromGoFileIO(image.url,{ loadPic(it) })
        }
    }
    fun loadPic(image: Bitmap){
        this.profileImage = image.asImageBitmap()
    }

    fun asJson(): String{
        return """
        {
        "uuid": "$uuid",
        "name": "$name",
        "age": "$age",
        "gender": "$gender",
        "image": "${image.url}"
     
        }
        """.trimIndent()
    }




}