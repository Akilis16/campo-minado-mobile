package com.example.campominado;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.Button;

import androidx.core.content.res.ResourcesCompat;

import java.util.HashMap;
import java.util.Map;

public class CampoMinado {

    private final String COR_SELECT = "#FFC107", COR_BOMB = "#ff0000", COR_UNSELECT = "#44000000";
    private static final int NUM_LINHAS = 10, NUM_COLUNAS = 10;
    private static final Cell[][] listCell = new Cell[NUM_LINHAS][NUM_COLUNAS]; // Tabuleiro do Campo Minado
    private final Map<Integer, Cell> cellMap = new HashMap<>(); //Mapeia as Celular por ID

    public CampoMinado() {
        reset();// inicializa os botões
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

    public void updateState(int id, Button btn) {   // Atualiza o status da Celula
        if (getCellById(id).isOpen()){                                  // Se Celula aberta
            if(getCellById(id).isHasBomb()){                            // Se Celula tem Bomba
//                btn.setText("1");
//                btn.setTextSize(3);
//                btn.setAllCaps(false);
//                btn.setGravity(Gravity.CENTER);
//                btn.setTextColor(Color.BLACK);
                btn.setBackgroundColor(Color.parseColor(COR_BOMB));     // Define Cor da Bomba
            }else{                                                      // Se Celula não tem Bomba
                btn.setBackgroundColor(Color.parseColor(COR_SELECT));
            }
        } else {                                                        // Se Celula fechada
            btn.setBackgroundColor(Color.parseColor(COR_UNSELECT));
        }
    }

    public void click(int id, Button btn) { // Recebe o clique da Celula
        getCellById(id).setOpen();
        updateState(id, btn);
    }

    private Cell getCellById(int id){   //Busca Celula pelo ID
        if(id < 0 || id >= (NUM_LINHAS * NUM_COLUNAS)) return null; //Valida se o ID existe
        return cellMap.get(id);
    }

    public static Cell getCellByCoords(int row, int col){
        return listCell[row][col];
    }

    private boolean generateBombRandow(){   // Gera aleatoriamente a bomba
        return Math.random() < 0.5;
    }

    public static int totalRow(){       // Retorna total de linhas
        return NUM_LINHAS;
    }

    public static int totalColumn(){    // Retorna total de colunas
        return NUM_COLUNAS;
    }
}
