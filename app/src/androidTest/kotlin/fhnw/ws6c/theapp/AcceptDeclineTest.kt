package fhnw.ws6c.theapp

import fhnw.ws6c.theapp.data.PostStatus
import fhnw.ws6c.theapp.data.Status
import org.json.JSONObject
import org.junit.Assert.assertTrue
import org.junit.Test

internal class AcceptDeclineTest {

    private val personSubscriberdAsString = """
       
        { "uuid": "ae48f9b0-337e-4fda-965a-0328c366c598", 
        "name": "Magdalena",
         "age": "32", 
         "gender": "Female", 
         "image": "068c1dc2-5ed3-499f-9b73-7e492bd0c24f" }
        
    """.trimIndent()

    private val postUUID = "18e1e453-3c11-4084-aa3c-0cf4a02a2e91"

    private val statusAsString = """
        { "status": "DECLINED", "postUUID": "18e1e453-3c11-4084-aa3c-0cf4a02a2e91" }
    """.trimIndent()


    @Test
    fun getStatus(){
        val posStatusAsJSON = JSONObject(statusAsString)
        val postStatus : PostStatus = PostStatus(posStatusAsJSON)

        assertTrue(postStatus.status == Status.DECLINED)
        assertTrue(postUUID == postStatus.postUUID)

    }

}