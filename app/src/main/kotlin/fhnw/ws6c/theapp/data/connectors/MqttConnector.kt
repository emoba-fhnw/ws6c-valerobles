package fhnw.ws6c.theapp.data.connectors

import com.hivemq.client.mqtt.datatypes.MqttQos
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish
import fhnw.ws6c.theapp.data.Post
import fhnw.ws6c.theapp.data.PostStatus
import org.json.JSONObject
import java.nio.charset.StandardCharsets
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

    private val client = Mqtt5Client.builder()
        .serverHost(mqttBroker)
        .identifier(UUID.randomUUID().toString())
        .buildAsync()

    fun connectAndSubscribe(topic:              String,
                            onNewMessage:       (JSONObject) -> Unit,
                            onError:            (Exception, String) -> Unit = {e, _ -> e.printStackTrace()},
                            onConnectionFailed: () -> Unit = {}) {
        client.connectWith()
            .cleanStart(true)
            .keepAlive(30)
            .send()
            .whenComplete { _, throwable ->
                if (throwable != null) {
                    onConnectionFailed()
                } else { //erst wenn die Connection aufgebaut ist, kann subscribed werden
                    subscribe(topic, onNewMessage, onError)
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
                }
                catch (e: Exception){
                    onError(e, it.payloadAsString())
                }
            }
            .send()
    }

    fun publishPost(topic: String,
                    post: Post,
                    onPublished: () -> Unit = {},
                    onError:     () -> Unit = {}) {
        client.publishWith()
            .topic(topic)
            .payload(post.asPayload())
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
            .retain(false)  //Message soll nicht auf dem Broker gespeichert werden
            .messageExpiryInterval(60) //TODO
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