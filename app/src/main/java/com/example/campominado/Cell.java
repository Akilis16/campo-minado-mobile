package com.example.campominado;

public class Cell {
    private int id, row, column;
    private boolean hasBomb, open;

    public Cell(int id, int row, int column, boolean hasBomb){
        this.id = id;
        this.row = row;
        this.column = column;
        this.hasBomb = hasBomb;
        this.open = false;
    }

    public boolean isHasBomb(){ // Retorna se tem bomba
        return this.hasBomb;
    }

    public boolean isOpen(){    // Retorna se Celula está aberta
        return this.open;
    }

    public void setOpen(){      // Abre a Celula
        this.open = true;
    }
}
