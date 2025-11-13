package com.example.campominado;

public class CampoMinado {

    private static final int NUM_LINHAS = 10, NUM_COLUNAS = 10;
    private final Cell[][] listCell = new Cell[NUM_LINHAS][NUM_COLUNAS]; // Tabuleiro do Campo Minado
//    private final Map<Integer, Cell> cellMap = new HashMap<>(); //Mapeia as Celular por ID
    private boolean mark, win, lose;
    private int difficulty;

    public CampoMinado() {
        this.difficulty = 1;
        reset();// inicializa tabuleiro
    }

    public void reset() {   // Reinicia campo
        this.mark = false;
        this.win = false;
        this.lose = false;
        int id = 0,  countCellNotBomb = 0;
        for (int row = 0; row < totalRow(); row++) {
            for (int col = 0; col < totalColumn(); col++) {
                listCell[row][col] = new Cell(id, row, col, generateBombRandow());
                countCellNotBomb = listCell[row][col].isHasBomb() ? (countCellNotBomb + 1) : (countCellNotBomb);
                id++;
            }
        }
        accountBombs(countCellNotBomb);
    }

    private void accountBombs(int countCellNotBomb){
        int sizeBoard = NUM_COLUNAS * NUM_LINHAS;

        int sizeMin = 0;
        int sizeMax = 0;

        switch (this.difficulty){
            case 1:
                sizeMin = (int)(sizeBoard * 0.05);
                sizeMax = (int)(sizeBoard * 0.10);
                break;
            case 2:
                sizeMin = (int)(sizeBoard * 0.20);
                sizeMax = (int)(sizeBoard * 0.30);
                break;
            case 3:
                sizeMin = (int)(sizeBoard * 0.30);
                sizeMax = (int)(sizeBoard * 0.50);
                break;
        }

        if(countCellNotBomb < sizeMin || countCellNotBomb > sizeMax){
            reset();
        }

    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty() {
        this.difficulty++;

        if(this.difficulty > 3){
            this.difficulty = 1;
        }
    }

    public boolean isLose() {
        return this.lose;
    }

    public boolean isWin() {
        return this.win;
    }

    private void endGameWin() {
        for (int row = 0; row < totalRow(); row++) {
            for (int col = 0; col < totalColumn(); col++) {
                if (!getCellByCoords(row, col).isOpen() && !getCellByCoords(row, col).isHasBomb())
                    return;
            }
        }
        this.win = true; // vitória
    }


    private void endGameLose(){
        for(int row = 0; row < totalRow(); row++){
            for(int col = 0; col <totalColumn(); col++){
                if(getCellByCoords(row, col).isHasBomb()) getCellByCoords(row, col).setOpen();
            }
        }
        this.lose = true; //DERROTA
    }

    public void openCell(int row, int col){
        if(row < 0 || col < 0 || row >= totalRow() || col >= totalColumn()) return;

        Cell cell = this.getCellByCoords(row, col);

        if(cell.isOpen()) return;

        this.getCellByCoords(row, col).setOpen();

//        this.countCellNoBomb--;
//        if(this.countCellNoBomb <= 0) endGameWin();
        endGameWin();

        if(cell.isHasBomb()) {
            endGameLose();
            return;
        }

        int bombsAround = countBombsAround(row, col);
        this.getCellByCoords(row, col).setBombsAround(bombsAround);

        if(bombsAround == 0){
            for(int r = -1; r < 2; r++){
                for(int c = -1; c < 2; c++){
                    if(r == 0 && c == 0) continue;

                    int rowCheck = row + r;
                    int colCheck = col + c;

                    if(rowCheck < 0 || colCheck < 0 || rowCheck >= totalRow() || colCheck >= totalColumn())
                        continue;;

                    openCell(rowCheck, colCheck);
                }
            }
        }
    }

    private int countBombsAround(int row, int col){
        int bombsAround = 0;
        for(int r = -1; r < 2; r++){
            for(int c = -1; c < 2; c++){
                if(r == 0 && c == 0) continue;

                int rowCheck = row + r;
                int colCheck = col + c;

                if(rowCheck < 0 || colCheck < 0 || rowCheck >= CampoMinado.totalRow() || colCheck >= CampoMinado.totalColumn())
                    continue;;

                if(this.getCellByCoords(rowCheck, colCheck).isHasBomb()){
                    bombsAround++;
                }
            }
        }
        return bombsAround;
    }

    public void setCellMark(int row, int col){
        getCellByCoords(row, col).markBomb();
    }

    public boolean isMark() {
        return mark;
    }

    public void setMark(){
        this.mark = !this.mark;
    }

    public Cell getCellByCoords(int row, int col){
        return listCell[row][col];
    }

    private boolean generateBombRandow(){   // Gera aleatoriamente a bomba
        float lucky = (float) (this.difficulty == 1 ? 0.10 : this.difficulty == 2 ? 0.30 : 0.50);
        return Math.random() < lucky;
    }

    public static int totalRow(){       // Retorna total de linhas
        return NUM_LINHAS;
    }

    public static int totalColumn(){    // Retorna total de colunas
        return NUM_COLUNAS;
    }
}
