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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private BoardView boardView;
    private AppCompatImageButton btnMarcador, btnDificuldade;

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

        btnMarcador = findViewById(R.id.btn_marcador);
        btnDificuldade = findViewById(R.id.btn_dificuldade);
        boardView = findViewById(R.id.board_view);

        if (savedInstanceState != null) {
            CampoMinado salvo = (CampoMinado) savedInstanceState.getSerializable("game_state");
            if (salvo != null) {
                boardView.setGame(salvo);
            }
        }

        btnMarcador.setSelected(boardView.isMark());
        btnDificuldade.setImageLevel(boardView.getDifficulty());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("game_state", boardView.getGame());
    }

    //  Botão para Marcar Celula
    public void onClickMark(View v) {
        boardView.setMark();
        btnMarcador.setSelected(boardView.isMark());
    }

    public void onClickDifficulty(View v){
        boardView.setDifficulty();
        btnDificuldade.setImageLevel(boardView.getDifficulty());
    }

    // Botão para Resetar Game
    public void onClickReset(View v) {
        boardView.resetGame();
    }
}