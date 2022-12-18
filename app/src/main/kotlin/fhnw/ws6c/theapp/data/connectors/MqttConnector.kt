package fhnw.ws6c.theapp.data.connectors

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.hivemq.client.mqtt.datatypes.MqttQos
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish
import fhnw.ws6c.theapp.data.Post
import fhnw.ws6c.theapp.data.PostStatus
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.text.DateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ofPattern
import java.util.*


/**
 * ACHTUNG: Das ist nur eine erste Konfiguration eines Mqtt-Brokers.
 *
 * Dient vor allem dazu mit den verschiedenen Parametern experimentieren zu können
 *
 * siehe die Doku:
 * https://hivemq.github.io/hivemq-mqtt-client/
 * https://github.com/hivemq/hivemq-mqtt-client
 *
 * Ein generischer Mqtt-Client (gut, um Messages zu kontrollieren)
 * http://www.hivemq.com/demos/websocket-client/
 *
 */
class MqttConnector (mqttBroker: String,
                     private val qos: MqttQos = MqttQos.EXACTLY_ONCE){

    var isConnected by mutableStateOf(false)
    private val client = Mqtt5Client.builder()
        .serverHost(mqttBroker)
        .identifier(UUID.randomUUID().toString())
        .buildAsync()

    fun connectAndSubscribe(topic:              String,
                            onNewMessage:       (JSONObject) -> Unit,
                            onError:            (Exception, String) -> Unit = {e, _ -> e.printStackTrace()},
                            onConnectionFailed: () -> Unit = {},
                            onSuccess:          () -> Unit = {},) {
        client.connectWith()
            .cleanStart(true)
            .keepAlive(30)
            .send()
            .whenComplete { _, throwable ->
                if (throwable != null) {
                    isConnected = false
                    onConnectionFailed()
                } else { //erst wenn die Connection aufgebaut ist, kann subscribed werden
                    isConnected = true
                    subscribe(topic, onNewMessage, onError)
                    onSuccess()
                }
            }
    }

    fun subscribe(topic:        String,
                  onNewMessage: (JSONObject) -> Unit,
                  onError:      (Exception, String) -> Unit = { e, _ -> e.printStackTrace() }){
        client.subscribeWith()
            .topicFilter(topic)
            .qos(qos)
            .noLocal(true)
            .callback {
                try {
                    print(it.payloadAsJSONObject())
                    onNewMessage(it.payloadAsJSONObject())
                    println("VALE Subscribed")
                }
                catch (e: Exception){
                    onError(e, it.payloadAsString())
                    println("VALE Subscribed")
                }
            }
            .send()
    }

    fun publish(topic: String,
                    s: String,
                    onPublished: () -> Unit = {},
                    onError:     () -> Unit = {}) {
        client.publishWith()
            .topic(topic)
            .payload(s.asPayload())
            .qos(qos)
            .retain(false)
            .messageExpiryInterval(100) // 86400 = 24h TODO: Change to stay until day of event
            .send()
            .whenComplete{_, throwable ->
                if(throwable != null){
                    onError()
                }
                else {
                    onPublished()
                }
            }
    }

    fun publishPost(topic: String,
                    post: Post,
                    onPublished: () -> Unit = {},
                    onError:     () -> Unit = {}) {

        val postDate = post.date
        val secondFormatter = ofPattern("dd.MM.yyyy")
        val current = LocalDate.now()
        val localDateTime = LocalDate.parse(postDate, secondFormatter)
        val time = Period.between(current,localDateTime).days
        val messageExpire = time * 86400
        client.publishWith()
            .topic(topic)
            .payload(post.asPayload())
            .qos(qos)
            .retain(true)
            .messageExpiryInterval(messageExpire.toLong()) // 86400 = 24h TODO: Change to stay until day of event
            .send()
            .whenComplete{_, throwable ->
                if(throwable != null){
                    onError()
                }
                else {
                    onPublished()
                }
            }
    }

    fun publishUpdate(topic: String,
                    update: PostStatus,
                    onPublished: () -> Unit = {},
                    onError:     () -> Unit = {}) {
        client.publishWith()
            .topic(topic)
            .payload(update.asPayload())
            .qos(qos)
            .retain(false)
            .messageExpiryInterval(60) // 86400 = 24h TODO: Change to stay until day of event
            .send()
            .whenComplete{_, throwable ->
                if(throwable != null){
                    onError()
                }
                else {
                    onPublished()
                }
            }
    }

    fun publishProfile(
        topic:       String,
        message: String,
        onPublished: () -> Unit = {},
        onError:     () -> Unit = {}) {
        client.publishWith()
            .topic(topic)
            .payload(message.asPayload())
            .qos(qos)
            .retain(true)  //Message soll nicht auf dem Broker gespeichert werden
            .messageExpiryInterval( 86400 * 5) //TODO
            .send()
            .whenComplete {_, throwable ->
                if(throwable != null){
                    onError()
                }
                else {
                    onPublished()
                }
            }
    }

    fun disconnect() {
        client.disconnectWith()
            .sessionExpiryInterval(0)
            .send()
    }
}

// praktische Extension Functions
private fun String.asPayload() : ByteArray = toByteArray(StandardCharsets.UTF_8)
private fun Mqtt5Publish.payloadAsJSONObject() : JSONObject = JSONObject(payloadAsString())
private fun Mqtt5Publish.payloadAsString() : String = String(payloadAsBytes, StandardCharsets.UTF_8)
private fun Post.asPayload() : ByteArray = asJsonString().asPayload()
private fun PostStatus.asPayload() : ByteArray = asJson().asPayload()