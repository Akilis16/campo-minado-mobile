package com.example.campominado;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private CampoMinado campoMinado;
    private LayoutInflater inflater;
    private TableLayout tableLayout;
    private final HashMap<Integer, Button> btnMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inflater = LayoutInflater.from(this);
        tableLayout = findViewById(R.id.table_layout);

        // criando os botões
        campoMinado = new CampoMinado();  //
        criarCampoMinado();          //
    }

    private void criarCampoMinado() {
        Typeface jerseyFont = ResourcesCompat.getFont(this, R.font.jersey_10);
        int btnId = 0;
        for (int row = 0; row < CampoMinado.totalRow(); row++) {
            TableRow tableRow = new TableRow(this);

            for (int col = 0; col < CampoMinado.totalColumn(); col++) {
                Button btn = (Button) inflater.inflate(R.layout.btn_template, tableRow, false);
                btn.setId(btnId);
                btn.setTypeface(jerseyFont);
                tableRow.addView(btn);
                // definindo um listener para o botão
                btn.setOnClickListener(view -> {
                    onClickBtn(view);
                });
                // armazenando referência para os botões
                btnMap.put(btnId, btn);
                btnId++;
            }
            tableLayout.addView(tableRow);
        }
    }

    /************************************
     INTERAÇÃO COM O USUÁRIO: CLICKS
     ************************************/

    private void onClickBtn(View v) {// Captura clique da Celula
        int id = v.getId();                     // Pega ID do elemento clicado
        campoMinado.click(id, btnMap.get(id));
    }

    // Para o botão INVERTER
    public void onClickInverter(View v) {
//        campoMinado.inverter();
//        sync();
    }

    // Para o botão RESET
    public void onClickReset(View v) {
        campoMinado.reset();
//        sync();
    }
}