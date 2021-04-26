package com.pucpr.somativaapp.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pucpr.somativaapp.R;
import com.pucpr.somativaapp.model.Colecao;
import com.pucpr.somativaapp.model.DataModel;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ColecaoAdapter adapter;
    FloatingActionButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        btnAdd = findViewById(R.id.btnAdd);

        DataModel.getInstance().setContext(MainActivity.this);
        adapter = new ColecaoAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));


        adapter.setClickListener((position, view) -> {
            Intent intent = new Intent(MainActivity.this, FormActivity.class);
            intent.putExtra("position", position);
            startActivity(intent);
        });

        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FormActivity.class);
            startActivity(intent);
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN,
                        ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        int fromPos = viewHolder.getAdapterPosition();
                        int toPos = target.getAdapterPosition();
                        Colecao from = DataModel.getInstance().getColecoes().get(fromPos);
                        Colecao to = DataModel.getInstance().getColecoes().get(toPos);

                        DataModel.getInstance().getColecoes().set(fromPos, to);
                        DataModel.getInstance().getColecoes().set(toPos, from);
                        adapter.notifyItemMoved(fromPos,toPos);
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();

                        DataModel.getInstance().deleteColecao(DataModel.getInstance().getColecoes().get(position));
                        adapter.notifyItemRemoved(position);
                    }
                }
        );
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}