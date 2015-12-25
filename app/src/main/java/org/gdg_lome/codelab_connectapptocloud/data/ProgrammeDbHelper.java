
package org.gdg_lome.codelab_connectapptocloud.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.gdg_lome.codelab_connectapptocloud.data.ProgrammeContract.MatiereEntry;

/**
 * Created by setico on 10/10/15.
 */
public class ProgrammeDbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "programme.db";
    public static final int DB_VERSION = 1;
    public static final String[] COLUMNS = {
            MatiereEntry._ID,
            MatiereEntry.COLUMN_MATIERE_LOGO,
            MatiereEntry.COLUMN_MATIERE_NOM,
            MatiereEntry.COLUMN_MATIERE_DESCRIPTION
    };

    public ProgrammeDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    // matiere table
        final String SQL_CREATE_MATIERE_TABLE = "CREATE TABLE "+
                MatiereEntry.TABLE_NAME + " ("+
                MatiereEntry._ID+" INTEGER PRIMARY KEY,"+
                MatiereEntry.COLUMN_MATIERE_LOGO +" TEXT NOT NULL, "+
                MatiereEntry.COLUMN_MATIERE_NOM+" TEXT UNIQUE NOT NULL, "+
                MatiereEntry.COLUMN_MATIERE_DESCRIPTION+" TEXT NOT NULL, "+
                "UNIQUE ("+MatiereEntry.COLUMN_MATIERE_NOM+") ON CONFLICT IGNORE"+
                " );";


        db.execSQL(SQL_CREATE_MATIERE_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+MatiereEntry.TABLE_NAME);
        onCreate(db);
    }

    public long insert(ContentValues values){
        SQLiteDatabase db = getWritableDatabase();
        return db.insert(MatiereEntry.TABLE_NAME,null,values);
    }

    public long insert(ContentValues [] values){
        SQLiteDatabase db = getWritableDatabase();
        long retcount=0;

        try {
            db.beginTransaction();
            for (ContentValues value : values) {
                long id = db.insert(MatiereEntry.TABLE_NAME, null, value);
                if (id != -1)
                    retcount++;
            }

            db.setTransactionSuccessful();

        }finally {
            db.endTransaction();
        }

        return retcount;
    }

    public Cursor get(long id){
        SQLiteDatabase db = getReadableDatabase();

        return db.query(MatiereEntry.TABLE_NAME,
                COLUMNS,
                MatiereEntry._ID+"= ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null,
                null);
    }

    public Cursor get(){
        SQLiteDatabase db = getReadableDatabase();
        return db.query(MatiereEntry.TABLE_NAME,
                COLUMNS,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    public int delete(long id){
        SQLiteDatabase db = getReadableDatabase();
        return db.delete(MatiereEntry.TABLE_NAME,
                MatiereEntry._ID+"=?",
                new String[]{String.valueOf(id)});
    }

}
