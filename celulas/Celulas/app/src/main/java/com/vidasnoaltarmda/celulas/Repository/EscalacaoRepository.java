package com.vidasnoaltarmda.celulas.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.vidasnoaltarmda.celulas.Dados.Escalacao;
import com.vidasnoaltarmda.celulas.Utils.Constantes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Barque on 28/01/2017.
 */

public class EscalacaoRepository extends SQLiteOpenHelper{

    public EscalacaoRepository(Context context){
        super(context, Constantes.BD_NOME, null, Constantes.BD_VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE tb_escalacoes ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "id_escala INTEGER,"+
                "membro TEXT(255) NOT NULL,"+
                "tarefa TEXT(255) NOT NULL";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }

    public void salvarEscalacao(Escalacao escalacao){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = getContentValuesEscalacao(escalacao);
        db.insert("tb_escalacoes", null, contentValues);
    }

    @NonNull
    private ContentValues getContentValuesEscalacao(Escalacao escalacao){
        ContentValues contentValues = new ContentValues();
        //contentValues.put("id_escala", escalacao.getIdEscala());
        contentValues.put("membro", escalacao.getMembro());
        contentValues.put("tarefa", escalacao.getTarefa());
        return contentValues;
    }

    public List<Escalacao> listarEscalacaos(){
        List<Escalacao> lista = new ArrayList<Escalacao>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("tb_escalacoes", null, null, null, null, null, "id_escala");

        while(cursor.moveToNext()){
            Escalacao escalacao = new Escalacao();
            setEscalacaoFromCursor(cursor, escalacao);

            lista.add(escalacao);
        }
        return lista;
    }

    public Escalacao consultarEscalacaoPorId(int Id_escalacao){
        Escalacao escalacao = new Escalacao();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query("tb_escalacoes", null, "id = ?", new String[]{String.valueOf(Id_escalacao)}, null, null, "id_escala");

        if(cursor.moveToNext()){
            setEscalacaoFromCursor(cursor, escalacao);
        }
        return escalacao;
    }

 public void setEscalacaoFromCursor(Cursor cursor, Escalacao escalacao){
    escalacao.setIdEscalacao(cursor.getInt(cursor.getColumnIndex("id")));
    //escalacao.setIdEscala(cursor.getInt(cursor.getColumnIndex("id_escala")));
    escalacao.setMembro(cursor.getString(cursor.getColumnIndex("membro")));
    escalacao.setTarefa(cursor.getString(cursor.getColumnIndex("tarefa")));
 }
 public void atualizarEscalacao(Escalacao escalacao){
     SQLiteDatabase db = this.getWritableDatabase();
     ContentValues contentValues = getContentValuesEscalacao(escalacao);
     db.update("tb_escalacoes", contentValues, "id = ?", new String[]{String.valueOf(escalacao.getIdEscalacao())});
 }
 public void removerEscalacaoPorId(int Id_escalacao){
     SQLiteDatabase db = this.getWritableDatabase();

     db.delete("tb_escalacoes", "id = ?", new String[]{String.valueOf(Id_escalacao)});
 }
}
