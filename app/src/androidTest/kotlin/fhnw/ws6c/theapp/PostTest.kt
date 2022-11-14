package fhnw.ws6c.theapp

import fhnw.ws6c.theapp.data.Post
import org.json.JSONObject
import org.junit.Assert.assertTrue
import org.junit.Test

internal class PostTest {

    private val postAsString = """
        { "uuid": "e6233736-e4ee-4dc2-9aee-d030d8b391e1", 
        "organizer": { "uuid": "d0e08475-99eb-4818-8ece-40c76d133417",
                        "name": "Ana", 
                        "age": 24, 
                        "gender": "Non-Binary",
                         "image": "4d601ee5-71a5-43f5-b791-b5d1fe2e4d22" }, 
                     "restaurantName": "lili's", 
                     "address": "Basel", 
                     "description": "Please join me to a nice evening",
                      "image": "22c48026-46ba-4acb-a41f-3f222e3f00c2", 
                      "peopleNumber": 0, 
                      "maxPeopleNumber": 4,
                       "date": "18.10.2022",
                        "time": "18:00" }
        
    """.trimIndent()



    @Test
    fun convertingJSONTOPost(){
        val postAsJSON = JSONObject(postAsString)
        val post : Post = Post(postAsJSON)
        assertTrue(post.uuid == "e6233736-e4ee-4dc2-9aee-d030d8b391e1")
        assertTrue(post.organizer.name == "Ana")


    }


}