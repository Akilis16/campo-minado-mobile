package com.example.campominado;

import java.util.HashMap;
import java.util.Map;

public class CampoMinado {

    private static final int NUM_LINHAS = 10, NUM_COLUNAS = 10;
    private final Cell[][] listCell = new Cell[NUM_LINHAS][NUM_COLUNAS]; // Tabuleiro do Campo Minado
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

    private Cell getCellById(int id){   //Busca Celula pelo ID
        if(id < 0 || id >= (NUM_LINHAS * NUM_COLUNAS)) return null; //Valida se o ID existe
        return cellMap.get(id);
    }

    public Cell getCellByCoords(int row, int col){
        return listCell[row][col];
    }

    private boolean generateBombRandow(){   // Gera aleatoriamente a bomba
        return Math.random() < 0.10;
    }

    public static int totalRow(){       // Retorna total de linhas
        return NUM_LINHAS;
    }

    public static int totalColumn(){    // Retorna total de colunas
        return NUM_COLUNAS;
    }
}
