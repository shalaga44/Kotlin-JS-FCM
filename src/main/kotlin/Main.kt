import com.narbase.kunafa.core.components.*
import com.narbase.kunafa.core.css.*
import com.narbase.kunafa.core.dimensions.dependent.matchParent
import com.narbase.kunafa.core.dimensions.dependent.weightOf
import com.narbase.kunafa.core.dimensions.percent
import com.narbase.kunafa.core.dimensions.px
import com.narbase.kunafa.core.lifecycle.LifecycleOwner
import firebase.app.App
import firebase.data
import firebase.getToken
import firebase.messaging.*
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.w3c.notifications.GRANTED
import org.w3c.notifications.Notification
import org.w3c.notifications.NotificationOptions
import org.w3c.notifications.NotificationPermission

val mainScope = MainScope()
fun main() {
    page {
        style {
            width = 100.percent
            height = 100.percent
            position = "fixed"
        }
        mount(FcmApp())
    }
}

class FcmApp : Component() {
    var firebaseApp: App? = null
    var firebaseMessagingService: FirebaseMessagingService? = null
    var tokenTextView: TextView? = null
    var messagesView: View? = null
    var messagesRootView: View? = null
    var permissionView: View? = null
    override fun onViewMounted(lifecycleOwner: LifecycleOwner) {
        super.onViewMounted(lifecycleOwner)

        mainScope.launch {
            // also edit in "firebase-messaging-sw.js"
            val app = firebase.initializeApp {
                apiKey = "AIzaSyDdzNXumR8mQDJbh-cRYn8pwq7o5035JqQ"
                authDomain = "balsam-one-fcm-admin-test.firebaseapp.com"
                projectId = "balsam-one-fcm-admin-test"
                storageBucket = "balsam-one-fcm-admin-test.appspot.com"
                messagingSenderId = "462126589691"
                appId = "1:462126589691:web:28e2730e467a69edd5325e"
            }.also { firebaseApp = it }

            val messaging = getMessaging(app).also { firebaseMessagingService = it }

            onMessage(messaging) { payload ->
                appendMessage(payload)

                Notification(
                    title = payload.notification.title,
                    options = NotificationOptions(
                        body = payload.notification.body,
                        icon = "cloud-messaging-logo.svg"
                    )
                ).apply {
                    onclick = {
                        window.parent.focus()
                        close()
                    }
                }


            }
            resetUI()
        }
    }

    private fun appendMessage(payload: MessagePayload) {
        messagesRootView?.isVisible = true
        messagesView?.verticalLayout {
            style {
                justifyContent = JustifyContent.Center
                alignItems = Alignment.Center
            }
            textView { text = "Received message:" }
            textView { text = JSON.stringify(payload, replacer = null, space = 2) }
        }
    }

    override fun View?.getView(): View {
        return verticalLayout {
            style {
                width = matchParent
                height = matchParent
                justifyContent = JustifyContent.Center
                alignItems = Alignment.Center
            }
            textView { text = "Firebase Cloud Messaging" }
            verticalLayout {
                style {
                    width = matchParent
                    justifyContent = JustifyContent.Center
                    alignItems = Alignment.Center
                }
                textView { text = "Registration Token" }
                tokenTextView = textView {
                    style { fontSize = 8.px;textAlign = TextAlign.Center }
                }
                horizontalLayout {
                    style {
                        width = matchParent
                        justifyContent = JustifyContent.Center
                        alignItems = Alignment.Center
                    }
                    button {
                        text = "Delete Token"
                        onClick = {
                            deleteToken()
                        }
                    }
                    button {
                        text = "Copy to clipboard"
                        onClick = {
                            try {
                                tokenTextView?.text?.let {
                                    window.navigator.clipboard.writeText(it)
                                }
                            } catch (_: Exception) {
                            }
                        }
                    }
                }

            }
            permissionView = verticalLayout {
                style {
                    width = matchParent
                    justifyContent = JustifyContent.Center
                    alignItems = Alignment.Center
                }
                isVisible = false
                textView { text = "Needs Permission" }
                button {
                    text = "Request Permission"
                    onClick = {
                        requestPermission()
                    }
                }
            }
            messagesRootView = verticalScrollLayout {
                style {
                    width = matchParent
                    height = weightOf(1)
                    alignItems = Alignment.Center
                }
                textView { text = "Messages:" }
                messagesView = verticalLayout {
                    style {
                        width = matchParent
                        justifyContent = JustifyContent.Center
                        alignItems = Alignment.Center
                    }
                }

            }
        }

    }

    private fun requestPermission() {
        Notification.requestPermission().then {
            if (it == NotificationPermission.GRANTED) {
                resetUI()
            }
        }
    }

    private fun deleteToken() {
        val messaging = firebaseMessagingService ?: return
        messaging.getToken().then { currentToken ->
            deleteToken(messaging, currentToken ?: "null").then {
                resetUI()
            }.catch {
                it.printStackTrace()
            }
        }
    }

    private fun resetUI() {
        messagesView?.clearAllChildren()
        tokenTextView?.text = "loading..."
        messagesRootView?.isVisible = false
        firebaseMessagingService?.getToken {
            vapidKey = "BPwXEy3ZBr6gB95g0iuCzW9EQGE2b5iRkqkZNscSY7a3Lg7csNjROIK9cjdPzbOIl2FhOZ5MixA9lJgA6c0Cbfo"
        }?.then {
            if (it != null) {
                tokenTextView?.text = it
                permissionView?.isVisible = false

            } else {
                tokenTextView?.text = "No registration token available. Request permission to generate one."
                permissionView?.isVisible = true
            }

        }?.catch {
            it.printStackTrace()
            permissionView?.isVisible = true
            tokenTextView?.text = "Error retrieving registration token. ${it.message}"
        }


    }


}



