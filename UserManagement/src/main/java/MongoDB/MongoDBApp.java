package MongoDB;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoIterable;
import org.bson.Document;

public class MongoDBApp {

    public static void main(String[] args) {


//        MongoClient mongoClient = MongoClients.create("mongodb://host1");
        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

        //get (if already named) or create new db
        MongoDatabase myDB = mongoClient.getDatabase("UserManagementDB");

//         Creating Credentials
        MongoCredential credential;
        credential = MongoCredential.createCredential("couchpotato", "UserManagementDB",
                "password".toCharArray());
        System.out.println("Connected to the database successfully");


        //create collection
        MongoCollection<Document> Users = myDB.getCollection("Users");

        /* To add documents:
        Document d1 = new Document("_id",1)
        .append("username", "Azeem")
        .append("email", "azeem197@gmail.com");
        Users.insertOne(d1);

        Document d2 = new Document("_id",2)
        .append("username", "Arshad")
        .append("email", "Muhammad.arshad.2@etu.unige.ch");
        Users.insertOne(d2);

        if error code E1100 appears, it is because document already exists
         */



        //read databases
        MongoIterable<String> list = mongoClient.listDatabaseNames();
        for (String name : list) System.out.println(name);

        //read document
        for (Document document : Users.find()) System.out.println(document);





    }
}
