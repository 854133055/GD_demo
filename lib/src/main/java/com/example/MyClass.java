package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyClass {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.example.lml.greendao3");
        addArticle(schema);

        new DaoGenerator().generateAll(schema, "/Users/lml/Desktop/GreenDao3/app/src/main/java");
    }

    private static void addArticle(Schema schema) {
        Entity article = schema.addEntity("Article");
        article.addIdProperty().primaryKey().autoincrement();
        article.addStringProperty("title").notNull();
        article.addStringProperty("content");
        article.addStringProperty("date");
        article.addByteArrayProperty("icon");
    }
}
