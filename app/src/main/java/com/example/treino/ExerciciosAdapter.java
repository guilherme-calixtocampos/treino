package com.example.treino;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ExerciciosAdapter extends RecyclerView.Adapter<ExerciciosAdapter.ViewHolder> {

    private List<Exercicio> exercicios;

    public ExerciciosAdapter(List<Exercicio> exercicios) {
        this.exercicios = exercicios;
    }

    @NonNull
    @Override
    public ExerciciosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercicio, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciciosAdapter.ViewHolder holder, int position) {
        Exercicio exercicio = exercicios.get(position);
        holder.txtNome.setText(exercicio.getNome());
        holder.txtDuracao.setText(exercicio.getDuracaoSegundos() + "s");
    }

    @Override
    public int getItemCount() {
        return exercicios.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNome, txtDuracao;

        public ViewHolder(View itemView) {
            super(itemView);
            txtNome = itemView.findViewById(R.id.txtNome);
            txtDuracao = itemView.findViewById(R.id.txtDuracao);
        }
    }
}

