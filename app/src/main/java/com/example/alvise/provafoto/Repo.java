package com.example.alvise.provafoto;

/**
 * Created by alvis on 31/05/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class Repo {
    private DBHelper dbHelper;

    public Repo(Context context) {
        dbHelper = new DBHelper(context);
    }

    //qui si inseriscono le righe nel database
    public int insert(int id, int idImage, String description, String image) {

        //Open connection to write data
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.KEY_ID, id);
        values.put(DBHelper.KEY_Id_image, idImage);
        values.put(DBHelper.KEY_description, description);
        values.put(DBHelper.KEY_image, image);

        // Inserting Row
        long idreturn = db.insert(DBHelper.TABLE, null, values);
        db.close(); // Closing database connection
        return (int) idreturn;
    }

    //elimina una riga dati ScrollView e immagine
    public void delete(int Id, int IdImage) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // It's a good practice to use parameter ?, instead of concatenate string
        db.delete(DBHelper.TABLE, DBHelper.KEY_ID + "= ?" + " AND " + DBHelper.KEY_Id_image + "= ?" , new String[] { String.valueOf(Id), String.valueOf(IdImage) });
        db.close(); // Closing database connection
    }

    //data una ScrollView e un'immagine ci mettiamo la descrizione
    public void update(int Id, int IdImage, String description) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(dbHelper.KEY_description, description);

        // It's a good practice to use parameter ?, instead of concatenate string
        db.update(dbHelper.TABLE, values, dbHelper.KEY_ID + "= ?" + " AND " + DBHelper.KEY_Id_image + "= ?", new String[] { String.valueOf(Id), String.valueOf(IdImage) });
        db.close(); // Closing database connection
    }

    //non l'ho mai usato, e forse sarebbe meglio rimuoverlo
   /* public ArrayList<HashMap<String, String>>  getStudentList() {
        //Open connection to read only
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                DBHelper.KEY_ID + " FROM " + DBHelper.TABLE;

        ArrayList<HashMap<String, String>> studentList = new ArrayList<HashMap<String, String>>();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> student = new HashMap<String, String>();
                student.put("id", cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ID)));
                studentList.add(student);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return studentList;

    }*/

    //anche questo era per il testing, in ogni caso in futuro puo essere utile
    public int getImages(int Id, int idImage){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                dbHelper.KEY_ID + "," +
                dbHelper.KEY_Id_image + "," +
                dbHelper.KEY_image + "," +
                dbHelper.KEY_description +
                " FROM " + dbHelper.TABLE
                + " WHERE " +
                dbHelper.KEY_ID + "=?"
                + " AND " + dbHelper.KEY_Id_image + "=?";/// It's a good practice to use parameter ?, instead of concatenate string

        int student=-1;

      //  Cursor cursor= db.query;
        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(Id),  String.valueOf(idImage) } );

        if (cursor.moveToFirst()) {
            do {
                student =cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_ID));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return student;
    }

    //ora non serve, é per il testing per vedere se tutti i campi sono stati modificati
    public int getAll(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                dbHelper.KEY_ID + "," +
                dbHelper.KEY_Id_image + "," +
                dbHelper.KEY_image + "," +
                dbHelper.KEY_description +
                " FROM " + dbHelper.TABLE;/// It's a good practice to use parameter ?, instead of concatenate string

        int student=-1;
        String prova= "";

        //  Cursor cursor= db.query;
        Cursor cursor = db.rawQuery(selectQuery, null );

        if (cursor.moveToFirst()) {
            do {
                student =cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_ID));
                prova = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_description));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return student;
    }


}
