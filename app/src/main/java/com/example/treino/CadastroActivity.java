package com.example.treino;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class CadastroActivity extends AppCompatActivity {

    private EditText edtNome, edtDuracao;
    private Button btnSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        edtNome = findViewById(R.id.edtNome);
        edtDuracao = findViewById(R.id.edtDuracao);
        btnSalvar = findViewById(R.id.btnSalvar);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = edtNome.getText().toString();
                String duracaoStr = edtDuracao.getText().toString();

                if (!nome.isEmpty() && !duracaoStr.isEmpty()) {
                    int duracao = Integer.parseInt(duracaoStr);

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("nome", nome);
                    resultIntent.putExtra("duracao", duracao);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
        });
    }
}
