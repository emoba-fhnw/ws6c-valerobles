package fhnw.ws6c.theapp.model

import org.json.JSONObject

data class Message(val organizor: String,
                   val restaurantName: String,
                    val description: String,) {

    constructor(json : JSONObject): this(json.getString("organizor"),
        json.getString("restaurantName"), json.getString("description"))

    fun asJsonString(): String {
        return """
            {"organizor":  "$organizor", 
             "restaurantName": "$restaurantName",
             "description": "$description"
            }
            """
    }
}