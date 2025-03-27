package com.example.doggoo;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    private static final String TAG = "Database";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "dog.db";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE allusers(email TEXT PRIMARY KEY, password TEXT)");
        String createTableQuery = "CREATE TABLE items (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "image BLOB, " +
                "name TEXT, " +
                "description TEXT, " +
                "price TEXT, " +
                "type TEXT" +
                ")";
        db.execSQL(createTableQuery);

        String createTableQuery2 = "CREATE TABLE userData (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "address TEXT, " +
                "contact TEXT, " +
                "email TEXT, " +
                "CONSTRAINT fk_email FOREIGN KEY (email) REFERENCES allusers(email) ON DELETE CASCADE" +
                ")";
        db.execSQL(createTableQuery2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrade here, if needed
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    // Helper methods for password encryption
    private static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.encodeToString(salt, Base64.DEFAULT).trim();
    }

    private static String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] hashedPassword = md.digest(password.getBytes());
            return Base64.encodeToString(hashedPassword, Base64.DEFAULT).trim();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // USER TABLE OPERATIONS
    public boolean insertData(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        String salt = generateSalt();
        String hashedPassword = hashPassword(password, salt);

        contentValues.put("email", email);
        contentValues.put("password", hashedPassword + ":" + salt); // Store the hashed password with the salt

        long result = db.insert("allusers", null, contentValues);
        return result != -1;
    }

    public boolean checkEmail(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM allusers WHERE email = ?", new String[]{email});
        return cursor.getCount() > 0;
    }

    public boolean checkEmailPassword(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT password FROM allusers WHERE email = ?", new String[]{email});

        if (cursor.moveToFirst()) {
            String storedPassword = cursor.getString(0);
            String[] parts = storedPassword.split(":");
            String storedHash = parts[0];
            String salt = parts[1];

            String hashedPassword = hashPassword(password, salt);

            return storedHash.equals(hashedPassword);
        }
        return false;
    }

    // ITEM TABLE OPERATIONS
    public boolean insertDetails(Bitmap image, String name, String description, String price, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            byte[] imageInBytes = outputStream.toByteArray();

            ContentValues values = new ContentValues();
            values.put("image", imageInBytes);
            values.put("name", name);
            values.put("description", description);
            values.put("price", price);
            values.put("type", type);  // Store the type

            long id = db.insert("items", null, values);
            if (id != -1) {
                Log.d(TAG, "Item inserted successfully");
                return true;
            } else {
                Log.e(TAG, "Error inserting item");
                return false;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error inserting item: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<model> getAllMenuItems() {
        ArrayList<model> menuItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM items", null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") model mdl = new model(
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("description")),
                        cursor.getString(cursor.getColumnIndex("price")),
                        cursor.getBlob(cursor.getColumnIndex("image"))
                );
                menuItems.add(mdl);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return menuItems;
    }

    public List<model> getItemsByType(String type) {
        List<model> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM items WHERE type = ?", new String[]{type});
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") model mdl = new model(
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("description")),
                        cursor.getString(cursor.getColumnIndex("price")),
                        cursor.getBlob(cursor.getColumnIndex("image"))
                );
                items.add(mdl);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return items;
    }

    public boolean insertUserdata(String email, String name, String address, String contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            // Check if the email exists in the allusers table
            Cursor cursor = db.rawQuery("SELECT * FROM allusers WHERE email = ?", new String[]{email});
            if (cursor.getCount() == 0) {
                // Email does not exist in allusers table, cannot insert into userData
                Log.e(TAG, "Email does not exist in allusers table");
                return false;
            }

            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("address", address);
            values.put("contact", contact);
            values.put("email", email); // Insert the email value into userData, which is a foreign key

            long result = db.insert("userData", null, values);
            if (result != -1) {
                Log.d(TAG, "User data inserted successfully");
                return true;
            } else {
                Log.e(TAG, "Error inserting user data");
                return false;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error inserting user data: " + e.getMessage());
            return false;
        }
    }
}
