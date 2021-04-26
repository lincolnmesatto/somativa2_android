package com.pucpr.somativaapp.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class ColecaoDatabase extends SQLiteOpenHelper {
    private static final String DB_NAME = "colecoes.sqlite";
    private static final int DB_VERSION = 1;
    private static final String DB_TABLE = "Colecao";
    private static final String COL_ID = "id";
    private static final String COL_TITULO = "titulo";
    private static final String COL_ULTIMO_LIDO = "ultimo_lido";
    private static final String COL_COMPLETO = "completo";
    private static final String COL_PATH = "path_img";
    private static final String COL_RATE = "rate";

    private Context context;

    public ColecaoDatabase(Context context){
        super(context,DB_NAME,null,DB_VERSION);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = String.format("CREATE TABLE IF NOT EXISTS %s("+
                " %s INTEGER PRIMARY KEY AUTOINCREMENT, "+
                " %s TEXT, " +
                " %s INTEGER, " +
                " %s INTEGER, " +
                " %s TEXT, " +
                " %s INTEGER " +
                ")",DB_TABLE,COL_ID,COL_TITULO,COL_ULTIMO_LIDO,COL_COMPLETO,COL_PATH,COL_RATE);

        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public long createColecaoInDB(Colecao colecao){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_TITULO,colecao.getTitulo());
        values.put(COL_COMPLETO,colecao.getCompleto());
        values.put(COL_ULTIMO_LIDO,colecao.getUltimoLido());
        values.put(COL_PATH,colecao.getCaminhoImg());
        values.put(COL_RATE,colecao.getRate());

        long id = database.insert(DB_TABLE,null,values);
        database.close();
        return id;
    }

    public ArrayList<Colecao> retrieveColecaoFromDB(){
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(DB_TABLE,null,null,
                null,null,null,null);
        ArrayList<Colecao> cities = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                long id = cursor.getLong(cursor.getColumnIndex(COL_ID));
                String titulo = cursor.getString(cursor.getColumnIndex(COL_TITULO));
                Integer completo = cursor.getInt(cursor.getColumnIndex(COL_COMPLETO));
                Integer ultimo = cursor.getInt(cursor.getColumnIndex(COL_ULTIMO_LIDO));
                String path = cursor.getString(cursor.getColumnIndex(COL_PATH));
                Integer rate = cursor.getInt(cursor.getColumnIndex(COL_RATE));

                Colecao c = new Colecao(id, titulo, path, completo, ultimo, rate);
                cities.add(c);

            }while (cursor.moveToNext());
        }
        database.close();
        return cities;
    }

    public long updateColecaoInDB(Colecao colecao){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_TITULO,colecao.getTitulo());
        values.put(COL_COMPLETO,colecao.getCompleto());
        values.put(COL_ULTIMO_LIDO,colecao.getUltimoLido());
        values.put(COL_PATH,colecao.getCaminhoImg());
        values.put(COL_RATE,colecao.getRate());

        long i = database.update(DB_TABLE,values, COL_ID+" = ? ", new String[]{String.valueOf(colecao.getId())});
        database.close();
        return i;
    }

    public long deleteColecaoInDB(long id){
        SQLiteDatabase database = getWritableDatabase();
        long i = database.delete(DB_TABLE, COL_ID+" = ? ", new String[]{String.valueOf(id)});
        database.close();
        return  i;
    }
}