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


/*

{"organizor": "Maria", "restaurantName": "Les Trois Rois", "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu" }
 */