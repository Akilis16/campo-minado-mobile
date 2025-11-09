package com.example.campominado;

public class Cell {
    private int linha, coluna, lado;
    private boolean tenhoBomba, aberto;

    public Cell(int linha, int coluna, int lado, boolean tenhoBomba){
        this.linha = linha;
        this.coluna = coluna;
        this.lado = lado;
        this.tenhoBomba = tenhoBomba;
        this.aberto = false;
    }
}
