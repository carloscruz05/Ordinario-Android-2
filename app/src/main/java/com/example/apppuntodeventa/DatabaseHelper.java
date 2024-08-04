package com.example.apppuntodeventa;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "OrdinarioBD.db";
    private static final int DATABASE_VERSION = 1;

    // SQL statements to create tables
    private static final String CREATE_USERS_TABLE =
            "CREATE TABLE users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT UNIQUE NOT NULL, " +
                    "password TEXT NOT NULL" +
                    ");";

    private static final String CREATE_PRODUCTS_TABLE =
            "CREATE TABLE products (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT, " +
                    "price REAL, " +
                    "quantity INTEGER, " +
                    "image TEXT, " +
                    "date TEXT" +
                    ");";

    private static final String CREATE_SALES_TABLE =
            "CREATE TABLE sales (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "product_id INTEGER, " +
                    "quantity INTEGER, " +
                    "price REAL, " +
                    "amount REAL, " +
                    "FOREIGN KEY(product_id) REFERENCES products(id)" +
                    ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_PRODUCTS_TABLE);
        db.execSQL(CREATE_SALES_TABLE);

        // Insert initial products
        db.execSQL("INSERT INTO products (name, price, quantity, image, date) VALUES ('Coca-Cola', 10.0, 50, 'refresco_coca', '2024-08-04')");
        db.execSQL("INSERT INTO products (name, price, quantity, image, date) VALUES ('Pepsi', 9.0, 40, 'refresco_pepsi', '2024-08-04')");
        db.execSQL("INSERT INTO products (name, price, quantity, image, date) VALUES ('Sprite', 8.0, 30, 'refresco_sprite', '2024-08-04')");
        db.execSQL("INSERT INTO products (name, price, quantity, image, date) VALUES ('Pizza Margherita', 50.0, 20, 'pizza_margherita', '2024-08-04')");
        db.execSQL("INSERT INTO products (name, price, quantity, image, date) VALUES ('Pizza Pepperoni', 60.0, 15, 'pizza_pepperoni', '2024-08-04')");
        db.execSQL("INSERT INTO products (name, price, quantity, image, date) VALUES ('Pizza Hawaiana', 55.0, 25, 'pizza_hawaiana', '2024-08-04')");
        db.execSQL("INSERT INTO products (name, price, quantity, image, date) VALUES ('Arroz', 20.0, 100, 'abarrotes_arroz', '2024-08-04')");
        db.execSQL("INSERT INTO products (name, price, quantity, image, date) VALUES ('Frijoles', 15.0, 80, 'abarrotes_frijoles', '2024-08-04')");
        db.execSQL("INSERT INTO products (name, price, quantity, image, date) VALUES ('Aceite', 25.0, 60, 'abarrotes_aceite', '2024-08-04')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop old tables if they exist
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS products");
        db.execSQL("DROP TABLE IF EXISTS sales");
        // Create tables again
        onCreate(db);
    }

    // Method to add a new user
    public void addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO users (username, password) VALUES (?, ?)", new Object[]{username, password});
    }
}
