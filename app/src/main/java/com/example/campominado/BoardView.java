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

    private final Paint  paintOpen = new Paint(), paintClose = new Paint(), paintBomb = new Paint(), paintText = new Paint();

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
        campoMinado = new CampoMinado();

        paintClose.setStyle(Paint.Style.FILL);
        paintOpen.setStyle(Paint.Style.FILL);
        paintBomb.setStyle(Paint.Style.FILL);
        paintText.setStyle(Paint.Style.FILL);

        paintClose.setColor(Color.parseColor("#44000000"));
        paintOpen.setColor(Color.parseColor("#FFC107"));
        paintBomb.setColor(Color.parseColor("#FF0000"));
        paintText.setColor(Color.BLACK);

        paintText.setTextAlign(Paint.Align.CENTER);
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

        this.cellWidth =  width / (float)(CampoMinado.totalColumn());
        this.cellHeight =  height / (float)(CampoMinado.totalRow());

        paintText.setTextSize((Math.min(cellHeight, cellWidth) * 0.6f));
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        for(int row = 0; row < CampoMinado.totalRow(); row++){
            for(int col = 0; col < CampoMinado.totalColumn(); col++){

                Cell cell = campoMinado.getCellByCoords(row, col);

                float left = col * cellWidth;
                float top = row * cellHeight;
                float right = left + cellWidth;
                float bottom = top + cellHeight;

                if(cell.isOpen()){
                    if(cell.isHasBomb()){
                        canvas.drawRect(left, top, right, bottom, paintBomb);
                    }else{
                        canvas.drawRect(left, top, right, bottom, paintOpen);
                    }
                } else {
                    canvas.drawRect(left, top, right, bottom, paintClose);
                }
            }
        }
    }
}
