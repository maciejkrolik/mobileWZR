const functions = require('firebase-functions');
const admin = require('firebase-admin');

admin.initializeApp();

exports.sendNotificationToGroup = functions.region('europe-west1').firestore
    .document('messages/{messageId}')
    .onCreate((snap, context) => {
        const newMessage = snap.data();

        const db = admin.firestore();
        const docRef = db.collection("lecturers").where("uid", "==", newMessage.lecturerUID);

        return docRef.get()
            .then(snapshot => {
                var message = {
                    notification: {
                        title: snapshot.docs[0].data().name,
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

                return null;
            })
            .catch(err => {
                console.log('Error:', err);
                throw new functions.https.HttpsError("failed-precondition", err);
            });
    });

exports.registerLecturer = functions.region('europe-west1').https
    .onCall((data, context) => {
        const email = data.email;
        const password = data.password;
        const token = data.token;

        const db = admin.firestore();
        const docRef = db.collection("lecturers").where("token", "==", token);

        return docRef.get()
            .then(snapshot => {
                if (snapshot.empty) {
                    console.log('No such token!');
                    throw new functions.https.HttpsError("failed-precondition", "No such token!");
                } else {
                    return admin.auth().createUser({
                        email: email,
                        password: password
                    })
                        .then(userRecord => {
                            console.log('Successfully created new user:', userRecord.uid);

                            const doc = snapshot.docs[0];
                            db.collection("lecturers").doc(doc.id).update({
                                token: admin.firestore.FieldValue.delete(),
                                uid: userRecord.uid
                            }, { merge: true });

                            return null;
                        })
                }
            })
            .catch(err => {
                console.log('Error:', err);
                throw new functions.https.HttpsError("failed-precondition", err);
            });
    });