package fhnw.emoba.thatsapp.data.gofileio

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import org.json.JSONObject
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import javax.net.ssl.HttpsURLConnection


class GoFileIOConnector() {
    val token = "cuE3ne4VyaGNN24Uk717IEZB5LjfXZvF"
    private val UPLOAD_URL1 = "https://store"
    private val UPLOAD_URL2 = ".gofile.io/uploadFile"
    private var serverCounter = 2

    fun uploadBitmapToGoFileIO(
        bitmap: Bitmap,
        onSuccess: (url: String) -> Unit,
        onError: (responseCode: Int, json: String) -> Unit = { _, _ -> }
    ) {

        val file = "photo.jpg"
        val crlf = "\r\n"
        val twoHyphens = "--"
        val boundary = "*****Boundary*****"

        val uploadUrl = UPLOAD_URL1+serverCounter+UPLOAD_URL2

        with(URL(uploadUrl).openConnection() as HttpsURLConnection) {

            //Request
            requestMethod = "POST"
            setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary)

            val requestStringStart = crlf + crlf +
                    twoHyphens + boundary + crlf +
                    "Content-Disposition: form-data; name=\"file\"; filename=\"$file\"" + crlf + crlf
            val requestStringEnd = crlf + twoHyphens + boundary + twoHyphens + crlf + crlf

            val request = DataOutputStream(getOutputStream())

            request.writeBytes(requestStringStart)
            request.write(bitmap.asByteArray())
            request.writeBytes(requestStringEnd)

            request.flush()
            request.close()

            //Response



//            val response = message()
//            val parentFolder =
//                JSONObject(response).getJSONObject("data").getString("parentFolder")
//            //val fileName = JSONObject(response).getJSONObject("data").getString("fileName")
//            onSuccess(parentFolder)
            if (responseCode == 200) {
                val response = message()
                val parentFolder =
                    JSONObject(response).getJSONObject("data").getString("parentFolder")
                //val fileName = JSONObject(response).getJSONObject("data").getString("fileName")
                //println("parentFolder: "+parentFolder)
                onSuccess(parentFolder)

            } else {
                if (serverCounter < 15) {
                    serverCounter++
                    uploadBitmapToGoFileIO(bitmap, onSuccess, onError)
                }
                return onError(responseCode, "")
            }
        }
    }

    //       "https://api.gofile.io/getContent?contentId=$676096a7-3aa9-42cd-9f2c-238898801602&token=cuE3ne4VyaGNN24Uk717IEZB5LjfXZvF&websiteToken=12345"

    fun downloadJSONFromGoFileIO(parentFolder: String): String {
        // https://api.gofile.io/getContent?contentId=f9199ed1-bbc2-4b75-a37e-078eb79eda2d&token=HstUK9Ye5sLPhsKsI1uQ12GiTAfqra5P&websiteToken=12345
        val url = "https://api.gofile.io/getContent?contentId=$parentFolder&token=$token&websiteToken=12345"
        var targetLink = ""
        print("downloadJSONFromGoFileIO " + url)
        with(URL(url).openConnection() as HttpsURLConnection) {
            addRequestProperty(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:100.0) Gecko/20100101 Firefox/100.0"
            )
            instanceFollowRedirects = false
            try {
                connect()
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val response = JSONObject(message())
                    val childName = response.getJSONObject("data").getJSONArray("childs").getString(0)
                    targetLink = response.getJSONObject("data").getJSONObject("contents").getJSONObject(childName).getString("link")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return targetLink
    }

    fun downloadBitmapFromGoFileIO(
        parentFolder: String,
        onSuccess: (bitmap: Bitmap) -> Unit,
        onDeleted: () -> Unit = {},
        onError: (exception: Exception) -> Unit = { e -> e.printStackTrace() }
    ) {
        val url = downloadJSONFromGoFileIO(parentFolder)
        print(url)
        with(URL(url).openConnection() as HttpsURLConnection) {

            addRequestProperty(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:100.0) Gecko/20100101 Firefox/100.0"
            )
            addRequestProperty("Accept", "Accept: image/avif,image/webp,*/*")
            addRequestProperty("Accept-Encoding", "Accept-Encoding: gzip, deflate, br")
            addRequestProperty("Host", "${url.subSequence(8, url.indexOf("."))}.gofile.io")
            addRequestProperty("Sec-Fethc-Dest", "image")
            addRequestProperty("Sec-Fetch-Mode", "no-cors")
            addRequestProperty("Sec-Fetch-Site", "same-site")
            addRequestProperty("Cookie", "accountToken=$token")

            instanceFollowRedirects = false

            var redirect = false
            try {
                connect()
                val status = responseCode

                if (status != HttpURLConnection.HTTP_OK) {
                    if (status == HttpURLConnection.HTTP_MOVED_TEMP
                        || status == HttpURLConnection.HTTP_MOVED_PERM
                        || status == HttpURLConnection.HTTP_SEE_OTHER
                    )
                        redirect = true;
                }
                if (redirect) {
                    println("Redirected")
                    downloadBitmapFromGoFileIO(
                        headerFields.get("Location")!!.first(),
                        onSuccess,
                        onDeleted,
                        onError
                    )
                    return
                }

            } catch (e: Exception) {
                onError(e)
            }
            try {
                if (!redirect) onSuccess(bitmap())

            } catch (e: Exception) {  //das ist nur eine Heuristik: Wenn die Response nicht in ein Bitmap umgewandelt werden kann,
                // dann muss der File wohl inzwischen geloescht worden sein
                onDeleted()
            }
        }
    }

    // ein paar hilfreiche Extension Functions
    private fun HttpsURLConnection.message(): String {
        Thread.sleep(2_000)
        val reader = BufferedReader(InputStreamReader(this.inputStream, StandardCharsets.UTF_8))
        val message = reader.readText()
        reader.close()

        return message
    }

    private fun HttpsURLConnection.bitmap(): Bitmap {
        val bitmapAsBytes = inputStream.readBytes()
        inputStream.close()
        return BitmapFactory.decodeByteArray(bitmapAsBytes, 0, bitmapAsBytes.size)
    }

    private fun Bitmap.asByteArray(): ByteArray {
        val baos = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val byteArray = baos.toByteArray()
        baos.close()
        return byteArray
    }
}