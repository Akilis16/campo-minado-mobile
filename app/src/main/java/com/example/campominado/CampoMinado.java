package com.example.campominado;

import android.graphics.Color;
import android.widget.Button;

import java.util.HashMap;
import java.util.Map;

public class CampoMinado {

    private final String COR_SELECT = "#FFC107";
    private final String COR_BOMB = "#ff0000";
    private final String COR_UNSELECT = "#44000000";
    private static final int NUM_LINHAS = 10, NUM_COLUNAS = 10;
    private final Cell[][] listCell = new Cell[NUM_LINHAS][NUM_COLUNAS]; // Tabuleiro do Campo Minado
    private final Map<Integer, Cell> cellMap = new HashMap<>(); //Mapeia as Celular por ID

    public CampoMinado() {
        reset();// inicializa os botões
    }

    private boolean generateBombRandow(){   // Gera aleatoriamente a bomba
        return Math.random() < 0.5;
    }

    public void reset() {   // Reinicia campo
        int id = 0;
        for (int row = 0; row < totalRow(); row++) {
            for (int col = 0; col < totalColumn(); col++) {
                listCell[row][col] = new Cell(id, row, col, generateBombRandow());
                cellMap.put(id, listCell[row][col]);
                id++;
            }
        }
    }

    private Cell getCellById(int id){   //Busca Celula pelo ID
        if(id < 0 || id >= (NUM_LINHAS * NUM_COLUNAS)) return null; //Valida se o ID existe
        return cellMap.get(id);
    }

    public void updateState(int id, Button btn) {   // Atualiza o status da Celula
        if (getCellById(id).isOpen()){
            if(getCellById(id).isHasBomb()){
                btn.setBackgroundColor(Color.parseColor(COR_BOMB));
                btn.setText("1");
            }else{
                btn.setBackgroundColor(Color.parseColor(COR_SELECT));
            }
        } else {
            btn.setBackgroundColor(Color.parseColor(COR_UNSELECT));
        }
    }

    public void click(int id, Button btn) { // Recebe o clique da Celula
        getCellById(id).setOpen();
        updateState(id, btn);
    }

    public static int totalRow(){       // Retorna total de linhas
        return NUM_LINHAS;
    }

    public static int totalColumn(){    // Retorna total de colunas
        return NUM_COLUNAS;
    }
}
