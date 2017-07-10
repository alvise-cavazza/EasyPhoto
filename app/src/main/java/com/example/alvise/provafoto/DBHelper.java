package com.example.alvise.provafoto;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper  extends SQLiteOpenHelper {
    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    private static final int DATABASE_VERSION = 4;

    // Labels table name
    public static final String TABLE = "TabellaPrincipale";

    // Labels Table Columns names
    public static final String KEY_ID = "id";
    public static final String KEY_description = "description";
    public static final String KEY_image = "image";
    public static final String KEY_Id_image = "Idimage";

    // Database Name
    private static final String DATABASE_NAME = "database.db";

    public DBHelper(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //All necessary tables you like to create will create here

        String CREATE_TABLE_STUDENT = "CREATE TABLE " + TABLE  + "("
                + KEY_ID  + " INTEGER, "
                + KEY_Id_image + " INTEGER, "
                + KEY_description + " TEXT, "
                + KEY_image + " TEXT, " +
                "PRIMARY KEY ("+ KEY_ID   +
                ", " + KEY_Id_image +
                " ))";

        db.execSQL(CREATE_TABLE_STUDENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed, all data will be gone!!!
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);

        // Create tables again
        onCreate(db);

    }

}