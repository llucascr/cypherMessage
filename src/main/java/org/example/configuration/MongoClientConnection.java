package org.example.configuration;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoClientConnection {
    public static void main(String[] args) {
        String connectionString = System.getenv("MONGO_URI");
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();
        // Create a new client and connect to the server
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            try {
                // Send a ping to confirm a successful connection
                MongoDatabase database = mongoClient.getDatabase("cypherMessage");
                database.runCommand(new Document("ping", 1));
                System.out.println("Pinged your deployment. You successfully connected to MongoDB!");

                MongoCollection<Document> collection = database.getCollection("message");

                Document doc = new Document("from", "Lucas")
                        .append("to", "luis")
                        .append("message", "fala bb")
                        .append("token", "chicoMoedas");

                collection.insertOne(doc);
                System.out.println("Documento inserido com sucesso!");
            } catch (MongoException e) {
                e.printStackTrace();
            }
        }
    }
}
