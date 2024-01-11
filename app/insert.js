//// Initialize Firebase
//var config = {
//  apiKey: "AIzaSyC3x8EgTpoU-2Vn2lTofBHOzTfgX4_EZGc",
//  databaseURL: "https://console.firebase.google.com/project/sound-scape-eaada/database/sound-scape-eaada-default-rtdb/data",
//  projectId: "sound-scape-eaada",
//  messagingSenderId: "your-messaging-sender-id",
//  appId: "1:618380013782:android:52205dbdba9d7e7e121977"
//};
//firebase.initializeApp(config);
//
//// Reference to the "Music" directory in the database
//var musicRef = firebase.database().ref('Music');
//
//// Specify the genre you want to retrieve
//var desiredGenre = "kpop"; // Change this to the desired genre
//
//// Reference to the new genre directory
//var genreRef = firebase.database().ref('Genres').child(desiredGenre);
//
//// Query to retrieve entries where the genre matches the desired value
//musicRef.orderByChild('genre').equalTo(desiredGenre).once('value')
//  .then(function(snapshot) {
//    // Check if the genre directory already exists, if not, create it
//    genreRef.once('value', function(genreSnapshot) {
//      if (!genreSnapshot.exists()) {
//        genreRef.set(true); // You can set any value, or even omit this line if you just want the directory to exist
//      }
//    });
//
//    // Insert fetched entries into the new genre directory
//    snapshot.forEach(function(childSnapshot) {
//      var entry = childSnapshot.val();
//      // Insert the entry into the new genre directory
//      genreRef.push().set(entry);
//    });
//
//    console.log(`Entries with genre "${desiredGenre}" inserted into the "${desiredGenre}" directory.`);
//  })
//  .catch(function(error) {
//    console.error("Error fetching data:", error);
//  });
