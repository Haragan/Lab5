package com.garkin.laban5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class PersonDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Persons.db";
    private SQLiteDatabase db;

    private static final String TABLE_NAME = "persons";
    private static final String COLUMN_NAME_ID = "_id";
    private static final String COLUMN_NAME_FIRST_NAME= "first_name";
    private static final String COLUMN_NAME_MIDDLE_NAME = "middle_name";
    private static final String COLUMN_NAME_LAST_NAME = "last_name";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_NAME_FIRST_NAME + " TEXT, " +
                    COLUMN_NAME_MIDDLE_NAME + " TEXT, " +
                    COLUMN_NAME_LAST_NAME + " TEXT);";

    public static final String SQL_DELETE_ENTRIES ="DROP TABLE IF EXISTS " + TABLE_NAME;

    public PersonDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public PersonDbHelper(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public long createPerson(Person person) {
        // Gets the data repository in write mode
        db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_FIRST_NAME, person.getFirstName());
        values.put(COLUMN_NAME_MIDDLE_NAME, person.getMiddleName());
        values.put(COLUMN_NAME_LAST_NAME, person.getLastName());

        long result = db.insert(TABLE_NAME, null,values);
        db.close();
        return result;
    }

    public int deletePerson(Long personId) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Define 'where' part of query.
        String selection = COLUMN_NAME_ID + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { String.valueOf(personId) };
        // Issue SQL statement.
        int result = db.delete(TABLE_NAME, selection, selectionArgs);
        db.close();
        return result;
    }

    public int updatePerson(Person person) {
        SQLiteDatabase db = this.getWritableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_FIRST_NAME, person.getFirstName());
        values.put(COLUMN_NAME_MIDDLE_NAME, person.getMiddleName());
        values.put(COLUMN_NAME_LAST_NAME, person.getLastName());

        // Which row to update, based on the ID
        String selection = COLUMN_NAME_ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(person.getId()) };

        int result = db.update(
                TABLE_NAME,
                values,
                selection,
                selectionArgs);
        db.close();
        return result;
    }

    private Person getPerson(Cursor cursor){
        if(cursor != null && cursor.getCount() > 0){
            Long id = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_ID));
            String first_name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_FIRST_NAME));
            String middle_name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_MIDDLE_NAME));
            String last_name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_LAST_NAME));

            return new Person(id, first_name, middle_name, last_name);
        }
        return null;
    }

    public List<Person> getPersonList() {
        List<Person> personList = new ArrayList<>();
        db = this.getReadableDatabase();

        String[] projection = {
                COLUMN_NAME_ID,
                COLUMN_NAME_FIRST_NAME,
                COLUMN_NAME_MIDDLE_NAME,
                COLUMN_NAME_LAST_NAME
        };

//         How you want the results sorted in the resulting Cursor
//        String sortOrder = OrderContract.OrderEntry.COLUMN_NAME_LOGIN + " ASC";

//        Cursor cursor = db.rawQuery("SELECT * FROM "+ OrderContract.OrderEntry.TABLE_NAME, null);

        Cursor cursor = db.query(
                TABLE_NAME,      // The table to query
                projection,                               // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        if(cursor.moveToFirst()){
            do {
                personList.add(getPerson(cursor));
            } while (cursor.moveToNext());
        }
        return personList;
    }

}
