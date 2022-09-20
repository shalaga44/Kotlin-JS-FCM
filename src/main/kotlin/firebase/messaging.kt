@file:JsModule("firebase/messaging")
@file:JsNonModule

package firebase.messaging

import firebase.app.App
import kotlin.js.Promise

@JsName("getMessaging")
external fun getMessaging(app: App): FirebaseMessagingService

@JsName("onMessage")
external fun onMessage(service: FirebaseMessagingService, callback: (payload: MessagePayload) -> Unit): Unit

@JsName("getToken")
external fun getToken(messages: FirebaseMessagingService, opts: GetTokenOpts): Promise<String?>


@JsName("deleteToken")
external fun deleteToken(messages: FirebaseMessagingService, scope: String): Promise<String>

external interface FirebaseMessagingService {


}

external interface GetTokenOpts {
    var vapidKey: String
}

external interface MessagePayload {
    @JsName("notification")
    var notification: Notification

    interface Notification {
        var body: String
        var title: String
    }

    @JsName("data")
    var _data: dynamic
}
