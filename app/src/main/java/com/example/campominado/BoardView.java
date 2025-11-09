package com.example.campominado;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

public class BoardView extends View {

    private float cellWidth, cellHeight;

    private CampoMinado campoMinado;

    private final Paint
            paintOpen = new Paint(Paint.ANTI_ALIAS_FLAG),
            paintClose = new Paint(Paint.ANTI_ALIAS_FLAG),
            paintBomb = new Paint(Paint.ANTI_ALIAS_FLAG),
            paintText = new Paint(Paint.ANTI_ALIAS_FLAG),
            paintGrid = new Paint(Paint.ANTI_ALIAS_FLAG);

    public BoardView(Context context) {
        super(context);
        inicializador();
    }

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inicializador();
    }

    public BoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inicializador();
    }
    private void inicializador(){
        this.campoMinado = new CampoMinado();

        this.paintClose.setStyle(Paint.Style.FILL);
        this.paintOpen.setStyle(Paint.Style.FILL);
        this.paintBomb.setStyle(Paint.Style.FILL);
        this.paintText.setStyle(Paint.Style.FILL);
        this.paintGrid.setStyle(Paint.Style.STROKE);

        this.paintClose.setColor(Color.parseColor("#666666"));
        this.paintOpen.setColor(Color.parseColor("#FFC107"));
        this.paintBomb.setColor(Color.parseColor("#FF0000"));
        this.paintText.setColor(Color.BLACK);

        this.paintText.setTextAlign(Paint.Align.CENTER);
        this.paintGrid.setStrokeWidth(5f);
        this.paintGrid.setColor(Color.WHITE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float height, width;

        if(h > w){
            height = (float) (h * 0.80);
            width = (float) (h * 0.95);
        }else{
            height = (float) (h * 0.95);
            width = (float) (h * 0.70);
        }

        this.cellWidth =  w / (float)(this.campoMinado.totalColumn());
        this.cellHeight =  h / (float)(this.campoMinado.totalRow());

        this.paintText.setTextSize((Math.min(this.cellHeight, this.cellWidth) * 0.6f));
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.DKGRAY);

        for(int row = 0; row < CampoMinado.totalRow(); row++){
            for(int col = 0; col < CampoMinado.totalColumn(); col++){

                Cell cell = this.campoMinado.getCellByCoords(row, col);

                float left = col * this.cellWidth;
                float top = row * this.cellHeight;
                float right = left + this.cellWidth;
                float bottom = top + this.cellHeight;

                if(cell.isOpen()){
                    if(cell.isHasBomb()){
                        canvas.drawRect(left, top, right, bottom, this.paintBomb);
                    }else{
                        canvas.drawRect(left, top, right, bottom, this.paintOpen);
                    }
                } else {
                    canvas.drawRect(left, top, right, bottom, this.paintClose);
                }

                canvas.drawRect(left, top, right, bottom, this.paintGrid);

            }
        }
    }

    public void resetGame() {
        this.campoMinado.reset();
        invalidate(); // redesenha o tabuleiro
    }
}
