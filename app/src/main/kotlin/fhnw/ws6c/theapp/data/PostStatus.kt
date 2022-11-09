package fhnw.ws6c.theapp.data


import org.json.JSONObject

data class PostStatus(val status: Status,
                      val postUUID: String
) {

    constructor(jsonObject: JSONObject) : this(
        Status.valueOf(jsonObject.getString("status")),
        jsonObject.getString("postUUID")
    )

    fun asJson(): String{
        return """
        {
        "status": "$status",
        "postUUID": "$postUUID"
        }
        """.trimIndent()
    }

}