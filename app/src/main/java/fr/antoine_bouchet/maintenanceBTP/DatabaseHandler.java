package fr.antoine_bouchet.maintenanceBTP;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static int DATABASE_VERSION = 1;
    protected static final String DATABASE_NAME = "TicketDatabase";
    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE ticket " +
                "( id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "niveau TEXT, "+
                "zone TEXT, "+
                "salle TEXT, "+
                "titre TEXT, "+
                "materiel TEXT, "+
                "categorie TEXT, "+
                "probleme TEXT ) ";
        db.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS ticket";
        db.execSQL(sql);
        onCreate(db);
    }
}