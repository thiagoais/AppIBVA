package com.vidasnoaltarmda.celulas.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.vidasnoaltarmda.celulas.Dados.Aviso;
import com.vidasnoaltarmda.celulas.Dados.Escala;
import com.vidasnoaltarmda.celulas.Utils.Constantes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Barque on 28/01/2017.
 */

public class EscalaRepository extends SQLiteOpenHelper{

    public EscalaRepository(Context context){
        super(context, Constantes.BD_NOME, null, Constantes.BD_VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE tb_escalas ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "id_celula INTEGER,"+
                "data DATE NOT NULL,"+
                "horario TEXT(50) NOT NULL,"+
                "local TEXT NOT NULL";


        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }

    public void salvarEscala(Escala escala){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = getContentValuesEscala(escala);
        db.insert("tb_escalas", null, contentValues);
    }

    @NonNull
    private ContentValues getContentValuesEscala(Escala escala){
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_celula", escala.getId_celula());
        contentValues.put("data", escala.getData_celula());
        contentValues.put("horario", escala.getHora_celula());
        contentValues.put("local", escala.getLocal_celula());
        return contentValues;
    }

    public List<Escala> listarEscalas(){
        List<Escala> lista = new ArrayList<Escala>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("tb_escalas", null, null, null, null, null, "data");

        while(cursor.moveToNext()){
            Escala escala = new Escala();
            setEscalaFromCursor(cursor, escala);

            lista.add(escala);
        }
        return lista;
    }

    public Escala consultarEscalaPorId(int Id_escala){
        Escala escala = new Escala();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query("tb_escalas", null, "id = ?", new String[]{String.valueOf(Id_escala)}, null, null, "data");

        if(cursor.moveToNext()){
            setEscalaFromCursor(cursor, escala);
        }
        return escala;
    }

 public void setEscalaFromCursor(Cursor cursor, Escala escala){
    escala.setId_escala(cursor.getInt(cursor.getColumnIndex("id")));
    escala.setId_celula(cursor.getInt(cursor.getColumnIndex("id_celula")));
    escala.setData_celula(cursor.getString(cursor.getColumnIndex("data")));
    escala.setHora_celula(cursor.getString(cursor.getColumnIndex("horario")));
    escala.setLocal_celula(cursor.getString(cursor.getColumnIndex("local")));
 }
 public void atualizarEscala(Escala escala){
     SQLiteDatabase db = this.getWritableDatabase();
     ContentValues contentValues = getContentValuesEscala(escala);
     db.update("tb_escalas", contentValues, "id = ?", new String[]{String.valueOf(escala.getId_escala())});
 }
 public removerEscalaPorId(int Id_escala){
     SQLiteDatabase db = this.getWritableDatabase();

     db.delete("tb_escalas", "id = ?", new String[]{String.valueOf(Id_escala)});
 }
}
