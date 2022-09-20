@file:JsModule("firebase/app")
@file:JsNonModule

package firebase.app

@JsName("initializeApp")
external fun initializeApp(firebaseConfig: FirebaseConfig): App


external interface App {

}

external interface FirebaseConfig {
    var apiKey: String
    var authDomain: String
    var databaseURL: String
    var projectId: String
    var storageBucket: String
    var messagingSenderId: String
    var appId: String
    var measurementId: String

}