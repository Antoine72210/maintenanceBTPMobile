package fr.antoine_bouchet.maintenanceBTP;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class  TableControllerTicket extends DatabaseHandler {
    public TableControllerTicket(Context context){
        super(context);
    }

    public boolean create(ObjectTicket objectTicket){
        ContentValues values = new ContentValues();
        values.put("niveau", objectTicket.niveau);
        values.put("zone", objectTicket.zone);
        values.put("salle", objectTicket.salle );
        values.put("titre", objectTicket.titre );
        values.put("materiel", objectTicket.materiel );
        values.put("categorie", objectTicket.categorie );
        values.put("probleme", objectTicket.probleme );

        SQLiteDatabase db = this.getWritableDatabase();
        boolean createSuccessFul = db.insert("ticket", null, values)>0;
        db.close();
        return createSuccessFul;
    }

    public int count(){
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "SELECT * FROM ticket";
        int recordCount = db.rawQuery(sql, null).getCount();
        db.close();

        return recordCount;
    }

    public List<ObjectTicket> read() {
        List<ObjectTicket> recordsList = new ArrayList<ObjectTicket>();
        String sql = "SELECT * FROM ticket ORDER BY id DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                String ticketNiveau = cursor.getString(cursor.getColumnIndex("niveau"));
                String ticketZone = cursor.getString(cursor.getColumnIndex("zone"));
                String ticketSalle = cursor.getString(cursor.getColumnIndex("salle"));
                String ticketTitre = cursor.getString(cursor.getColumnIndex("titre"));
                String ticketMateriel = cursor.getString(cursor.getColumnIndex("materiel"));
                String ticketProbleme = cursor.getString(cursor.getColumnIndex("probleme"));

                ObjectTicket objectTicket = new ObjectTicket();
                objectTicket.id = id;
                objectTicket.niveau = ticketNiveau;
                objectTicket.zone = ticketZone;
                objectTicket.salle = ticketSalle;
                objectTicket.titre = ticketTitre;
                objectTicket.materiel = ticketMateriel;
                objectTicket.probleme = ticketProbleme;

                recordsList.add(objectTicket);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return recordsList;
    }

    public ObjectTicket readSingleRecord(int ticketId) {
        ObjectTicket objectTicket = null;
        String sql = "SELECT * FROM ticket WHERE id = " + ticketId;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
            String niveau = cursor.getString(cursor.getColumnIndex("niveau"));
            String zone = cursor.getString(cursor.getColumnIndex("zone"));
            String salle = cursor.getString(cursor.getColumnIndex("salle"));
            String titre = cursor.getString(cursor.getColumnIndex("titre"));
            String materiel = cursor.getString(cursor.getColumnIndex("materiel"));
            String categorie = cursor.getString(cursor.getColumnIndex("categorie"));
            String probleme = cursor.getString(cursor.getColumnIndex("probleme"));


            objectTicket = new ObjectTicket();
            objectTicket.id = id;
            objectTicket.niveau = niveau;
            objectTicket.zone = zone;
            objectTicket.salle = salle;
            objectTicket.titre = titre;
            objectTicket.materiel = materiel;
            objectTicket.categorie = categorie;
            objectTicket.probleme = probleme;
        }
        cursor.close();
        db.close();
        return objectTicket;

    }

    public boolean update(ObjectTicket objectTicket) {
        ContentValues values = new ContentValues();
        values.put("niveau", objectTicket.niveau);
        values.put("zone", objectTicket.zone);
        values.put("salle", objectTicket.salle );
        values.put("titre", objectTicket.titre );
        values.put("materiel", objectTicket.materiel );
        values.put("categorie", objectTicket.categorie );
        values.put("probleme", objectTicket.probleme );

        String where = "id = ?";
        String[] whereArgs = { Integer.toString(objectTicket.id) };
        SQLiteDatabase db = this.getWritableDatabase();
        boolean updateSuccessful = db.update("ticket", values, where, whereArgs) > 0;
        db.close();
        return updateSuccessful;
    }

    public boolean delete(int id) {
        boolean deleteSuccessful = false;
        SQLiteDatabase db = this.getWritableDatabase();
        deleteSuccessful = db.delete("ticket", "id ='" + id + "'", null) > 0;
        db.close();
        return deleteSuccessful;
    }
}
