importScripts('https://www.gstatic.com/firebasejs/9.9.4/firebase-app-compat.js');
importScripts('https://www.gstatic.com/firebasejs/9.9.4/firebase-messaging-compat.js');

 firebase.initializeApp({
   apiKey: "AIzaSyDdzNXumR8mQDJbh-cRYn8pwq7o5035JqQ",
   authDomain: "balsam-one-fcm-admin-test.firebaseapp.com",
   projectId: "balsam-one-fcm-admin-test",
   storageBucket: "balsam-one-fcm-admin-test.appspot.com",
   messagingSenderId: "462126589691",
   appId: "1:462126589691:web:28e2730e467a69edd5325e",
 });


const messaging = firebase.messaging();

messaging.onBackgroundMessage(function(payload) {
    console.log('[firebase-messaging-sw.js] Received background message ', payload);
    const notificationTitle = payload.notification.title;
    const notificationOptions = {
    body: payload.notification.body,
    icon: "/cloud-messaging-logo.svg"
    };

    var notification = new Notification(notificationTitle, notificationOptions);
    notification.onclick =
        function(){
                window.parent.focus();
                notification.close();
        }

    self.registration.showNotification(notificationTitle,
    notificationOptions);
});

