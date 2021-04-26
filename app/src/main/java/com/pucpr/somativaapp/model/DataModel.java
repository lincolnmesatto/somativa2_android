package com.pucpr.somativaapp.model;

import android.content.Context;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DataModel {
    private static DataModel instance = new DataModel();

    private DataModel(){
        colecoes = new ArrayList<>();
    }
    public static DataModel getInstance(){
        return instance;
    }
    private ColecaoDatabase database;
    private ArrayList<Colecao> colecoes;
    private Context context;

    public void setContext(Context context){
        this.context = context;
        database = new ColecaoDatabase(context);
        colecoes = database.retrieveColecaoFromDB();

        if(colecoes == null || colecoes.size() <= 0)
            colecoes = new ArrayList<>();
    }

    public ArrayList<Colecao> getColecoes(){
        return colecoes;
    }

    public void addColecao(Colecao colecao){
        long id = database.createColecaoInDB(colecao);
        if(id > 0){
            colecao.setId(id);
            colecoes.add(colecao);
        }else{
            Toast.makeText(context,"Ocorreu um erro ao adicionar a coleção.", Toast.LENGTH_LONG).show();
        }
    }

    public void updateColecao(Colecao colecao){
        long i = database.updateColecaoInDB(colecao);
        if(i > 0){
            colecoes.add(colecao);
        }else{
            Toast.makeText(context,"Ocorreu um erro ao atualizar a coleção.", Toast.LENGTH_LONG).show();
        }
    }

    public void deleteColecao(Colecao colecao){
        long i = database.deleteColecaoInDB(colecao.getId());
        if(i > 0){
            colecoes.remove(colecao);
        }else{
            Toast.makeText(context,"Ocorreu um erro ao deletar a coleção", Toast.LENGTH_LONG).show();
        }
    }
}
