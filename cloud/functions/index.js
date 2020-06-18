const functions = require('firebase-functions');
const admin = require('firebase-admin');

admin.initializeApp();

exports.sendNotificationToGroup = functions.region('europe-west1').firestore
    .document('messages/{messageId}')
    .onCreate((snap, context) => {
        const newMessage = snap.data();

        var message = {
            notification: {
                title: "Nowa wiadomość",
                body: newMessage.content,
            },
            topic: newMessage.studentGroup
        };

        admin.messaging().send(message)
            .then((response) => {
                console.log('Successfully sent message:', response);
                return response;
            })
            .catch((error) => {
                console.log('Error sending message:', error);
            });

        return 0;
    });
