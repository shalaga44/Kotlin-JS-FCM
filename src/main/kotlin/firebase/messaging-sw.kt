@file:JsModule("firebase/messaging/sw")
@file:JsNonModule

package firebase.messaging.sw

import firebase.messaging.FirebaseMessagingService


@JsName("onBackgroundMessage")
external fun onBackgroundMessage(service: FirebaseMessagingService, callback: (Any?) -> Unit): Unit

