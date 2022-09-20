package firebase

import firebase.app.App
import firebase.app.FirebaseConfig
import firebase.messaging.GetTokenOpts
import firebase.messaging.FirebaseMessagingService
import firebase.messaging.MessagePayload
import kotlin.js.Promise

fun initializeApp(firebaseConfig: FirebaseConfig.() -> Unit): App {
    val result = js("new Object()").unsafeCast<FirebaseConfig>()
    firebaseConfig(result)
    return firebase.app.initializeApp(result)
}

fun FirebaseMessagingService.getToken(opts: (GetTokenOpts.() -> Unit) = {}): Promise<String?> {
    val result = js("new Object()").unsafeCast<GetTokenOpts>()
    opts(result)
    return firebase.messaging.getToken(this, result)
}

val MessagePayload.data
    get():  Map<String, Any?> {
        return mapOf(this._data)
    }

inline fun <I> objectOf(init: I.() -> Unit): I {
    val result = js("new Object()").unsafeCast<I>()
    init(result)
    return result
}

fun entriesOf(jsObject: dynamic): List<Pair<String, Any?>> =
    (js("Object.entries") as (dynamic) -> Array<Array<Any?>>)
        .invoke(jsObject)
        .map { entry -> entry[0] as String to entry[1] }

fun mapOf(jsObject: dynamic): Map<String, Any?> =
    entriesOf(jsObject).toMap()
