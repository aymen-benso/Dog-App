package com.example.doggoo;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSession {
    private static UserSession instance;
    private SharedPreferences sharedPreferences;

    private UserSession(Context context) {
        sharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE);
    }

    public static synchronized UserSession getInstance(Context context) {
        if (instance == null) {
            instance = new UserSession(context);
        }
        return instance;
    }

    public String getEmail() {
        return sharedPreferences.getString("email", "");
    }

    public void setEmail(String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.apply();
    }
    public void clearSession() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Clear all data stored in SharedPreferences
        editor.apply(); // Apply changes

        // Nullify the instance to prevent reuse of the old session
        instance = null;
    }
}

