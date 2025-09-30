package org.example.configuration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MongoHandler {

    private static final String CONNECTION_STRING = System.getenv("MONGO_URI");
    private static final String DATABASE_NAME = "cypherMessage";

    private static MongoClient client;
    private static MongoDatabase database;

    static {
        client = MongoClients.create(CONNECTION_STRING);
        database = client.getDatabase(DATABASE_NAME);
    }

    public static MongoCollection<Document> getCollection(String collectionName) {
        return database.getCollection(collectionName);
    }

    public static void insert(String collectionName, Document document) {
        getCollection(collectionName).insertOne(document);
        System.out.println("Documento inserido com sucesso!");
    }

    public static List<Document> findAll(String collectionName, String token) {
        List<Document> results = new ArrayList<>();

        Document query = new Document("token", token);

        getCollection(collectionName).find(query).into(results);
        return results;
    }

    public static Document findUser(String username) throws Exception {
        Document query = new Document("name", username.toLowerCase());
        Document user = getCollection("user").find(query).first();

        if (user == null) throw new Exception("Usuário não encontrado");

        return user;
    }

    public static void close() {
        client.close();
    }

    public static MongoClient getClient() {
        return client;
    }

    public static void setClient(MongoClient client) {
        MongoHandler.client = client;
    }

    public static MongoDatabase getDatabase() {
        return database;
    }

    public static void setDatabase(MongoDatabase database) {
        MongoHandler.database = database;
    }
}
