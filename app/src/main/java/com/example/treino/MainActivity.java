package com.example.treino;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Exercicio> listaExercicios = new ArrayList<>();
    private RecyclerView recyclerView;
    private ExerciciosAdapter adapter;
    private Button btnCadastro, btnIniciar;
    private TextView txtExercicioAtual, txtContador;

    private int currentIndex = 0;
    private CountDownTimer timer;

    private static final String CHANNEL_ID = "treino_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pedirPermissaoNotificacao();

        recyclerView = findViewById(R.id.recyclerView);
        btnCadastro = findViewById(R.id.btnCadastro);
        btnIniciar = findViewById(R.id.btnIniciarTreino);
        txtExercicioAtual = findViewById(R.id.txtExercicioAtual);
        txtContador = findViewById(R.id.txtContador);

        adapter = new ExerciciosAdapter(listaExercicios);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnCadastro.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CadastroActivity.class);
            startActivityForResult(intent, 1);
        });

        btnIniciar.setOnClickListener(v -> {
            if (!listaExercicios.isEmpty()) {
                iniciarTreino();
            }
        });
    }

    private void pedirPermissaoNotificacao() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1001);
            }
        }
    }

    private void iniciarTreino() {
        if (currentIndex >= listaExercicios.size()) {
            mostrarNotificacao("Treino concluído! Parabéns!");
            return;
        }

        Exercicio exercicioAtual = listaExercicios.get(currentIndex);
        txtExercicioAtual.setText(exercicioAtual.getNome());

        mostrarNotificacao("Exercício: " + exercicioAtual.getNome());

        if (timer != null) {
            timer.cancel();
        }

        timer = new CountDownTimer(exercicioAtual.getDuracaoSegundos() * 1000L, 1000L) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                txtContador.setText((millisUntilFinished / 1000) + "s");
            }

            @Override
            public void onFinish() {
                currentIndex++;
                iniciarTreino();
            }
        }.start();
    }

    private void mostrarNotificacao(String texto) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Canal de Treino",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Notificações do circuito de treino");
            channel.enableLights(true);
            channel.enableVibration(true);
            manager.createNotificationChannel(channel);
        }

        androidx.core.app.NotificationCompat.Builder builder =
                new androidx.core.app.NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentTitle("Circuito de Treino")
                        .setContentText(texto)
                        .setAutoCancel(true)
                        .setPriority(androidx.core.app.NotificationCompat.PRIORITY_HIGH);

        manager.notify((int) System.currentTimeMillis(), builder.build());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String nome = data.getStringExtra("nome");
            int duracao = data.getIntExtra("duracao", 0);

            if (nome != null && duracao > 0) {
                listaExercicios.add(new Exercicio(nome, duracao));
                adapter.notifyItemInserted(listaExercicios.size() - 1);
            }
        }
    }
}
