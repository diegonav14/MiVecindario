package com.example.mivecindario.Modelos;

import com.google.firebase.database.FirebaseDatabase;

public class MyFirabaseApp extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
