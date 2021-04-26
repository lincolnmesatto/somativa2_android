package com.pucpr.somativaapp.controller;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pucpr.somativaapp.R;
import com.pucpr.somativaapp.model.Colecao;
import com.pucpr.somativaapp.model.DataModel;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ColecaoAdapter extends RecyclerView.Adapter<ColecaoAdapter.ViewHolder> {
    private static ClickListener clickListener;

    public void setClickListener(ClickListener clickListener) {
        ColecaoAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int id, View view);
    }

    @NonNull
    @Override
    public ColecaoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate( R.layout.item_recyclerview,
                        parent, false
                );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColecaoAdapter.ViewHolder holder, int position) {
        Colecao c = DataModel.getInstance().getColecoes().get(position);
        holder.textViewTitulo.setText(c.getTitulo());
        holder.textViewCompleto.setText(c.getCompleto() == 0 ? "Não completo" : "Completo");
        if(c.getCaminhoImg() != null) {
            File file = new File(c.getCaminhoImg());
            if (file.exists()) {
                holder.imageViewList.setImageURI(Uri.fromFile(file));
            }
        }
    }

    @Override
    public int getItemCount() {
        return DataModel.getInstance().getColecoes().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitulo;
        TextView textViewCompleto;
        ImageView imageViewList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitulo = itemView.findViewById(R.id.textViewTitulo);
            textViewCompleto = itemView.findViewById(R.id.textViewCompleto);
            imageViewList = itemView.findViewById(R.id.imageViewList);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener == null)
                        return;
                    clickListener.onItemClick(getAdapterPosition(), view);
                }
            });
        }
    }
}
