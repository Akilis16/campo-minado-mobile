package com.example.campominado;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;

public class BoardView extends View {

    private float cellWidth, cellHeight, offsetX = 0f, offsetY = 0f;

    private CampoMinado campoMinado;
    private Drawable bombDrawable;

    private final Paint
            paintOpen = new Paint(Paint.ANTI_ALIAS_FLAG),
            paintClose = new Paint(Paint.ANTI_ALIAS_FLAG),
            paintMark = new Paint(Paint.ANTI_ALIAS_FLAG),
            paintBomb = new Paint(Paint.ANTI_ALIAS_FLAG),
            paintText = new Paint(Paint.ANTI_ALIAS_FLAG),
            paintMessage = new Paint(Paint.ANTI_ALIAS_FLAG),
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

        this.bombDrawable = AppCompatResources.getDrawable(getContext(), R.drawable.bomba_sem_fundo);

        this.paintClose.setStyle(Paint.Style.FILL);
        this.paintMark.setStyle(Paint.Style.FILL);
        this.paintOpen.setStyle(Paint.Style.FILL);
        this.paintBomb.setStyle(Paint.Style.FILL);
        this.paintText.setStyle(Paint.Style.FILL);
        this.paintGrid.setStyle(Paint.Style.STROKE);

        this.paintClose.setColor(Color.parseColor("#C0B5B4"));
        this.paintMark.setColor(Color.BLUE);
        this.paintOpen.setColor(Color.parseColor("#FFC107"));
        this.paintBomb.setColor(Color.parseColor("#FF0000"));
        this.paintText.setColor(Color.BLACK);

        this.paintText.setTextAlign(Paint.Align.CENTER);
        this.paintGrid.setStrokeWidth(8.4f);
        this.paintGrid.setColor(Color.WHITE);

        paintMessage.setAntiAlias(true);
        paintMessage.setTextAlign(Paint.Align.CENTER);
        paintMessage.setTextSize(64f);                // ajusta depois se quiser maior/menor
        paintMessage.setFakeBoldText(true);
        paintMessage.setShadowLayer(8f, 0f, 0f, Color.BLACK);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float cellSizeByWidth = w / (float) CampoMinado.totalColumn();
        float cellSizeByHeight = h / (float) CampoMinado.totalRow();

        float cellSize = Math.min(cellSizeByWidth, cellSizeByHeight);

        this.cellHeight = cellSize;
        this.cellWidth = cellSize;

        float boardWidth =  cellSize * CampoMinado.totalColumn();
        float boardHeight =  cellSize * CampoMinado.totalRow();

        this.offsetX = (w - boardWidth) / 2f;
        this.offsetY = (h - boardHeight) / 2f;

        this.offsetX = (w - boardWidth) / 2f;
        this.offsetY = (h - boardHeight) / 2f;

//        float height, width;
//
//        if(h > w){
//            height = (float) (h * 0.80);
//            width = (float) (h * 0.95);
//        }else{
//            height = (float) (h * 0.95);
//            width = (float) (h * 0.70);
//        }

//        this.cellWidth =  w / (float)(CampoMinado.totalColumn());
//        this.cellHeight =  h / (float)(CampoMinado.totalRow());

        this.paintText.setTextSize((Math.min(this.cellHeight, this.cellWidth) * 0.6f));
        this.paintMessage.setTextSize(Math.min(w, h) * 0.15f);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.DKGRAY);

        for(int row = 0; row < CampoMinado.totalRow(); row++){
            for(int col = 0; col < CampoMinado.totalColumn(); col++){

                Cell cell = this.campoMinado.getCellByCoords(row, col);

                float left = offsetX + col * this.cellWidth;
                float top = offsetY + row * this.cellHeight;
                float right = left + this.cellWidth;
                float bottom = top + this.cellHeight;

                if(cell.isOpen()){
                    if(cell.isHasBomb()){
                        canvas.drawRect(left, top, right, bottom, this.paintBomb);

                        if(bombDrawable != null){
                            float cellW = this.cellWidth, cellH = this.cellHeight;
                            int bombW = bombDrawable.getIntrinsicWidth(), bombH = bombDrawable.getIntrinsicHeight();
                            int imgL, imgR, imgT, imgB;

                            if(bombH > 0 && bombW > 0) {
                                float scale = 0.9f * Math.min(cellW / bombW, cellH / bombH);

//                                float scaleW = (cellW / bombW) * 0.8F, scaleH = (cellH / bombH) * 0.8F;
                                float drawW = bombW * scale, drawH = bombH * scale;
                                float cx = (left + right) / 2F, cy = (top + bottom) / 2F;

                                imgL = (int) (cx - drawW / 2F);
                                imgR = (int) (cx + drawW / 2F);
                                imgT = (int) (cy - drawH / 2F);
                                imgB = (int) (cy + drawH / 2F);
                            }else{
                                imgL = (int) left;
                                imgT = (int) top;
                                imgR = (int) right;
                                imgB = (int) bottom;
                            }

                            bombDrawable.setBounds(imgL, imgT, imgR, imgB);
                            bombDrawable.draw(canvas);
                        }
                    }else{
                        canvas.drawRect(left, top, right, bottom, this.paintOpen);
                    }
                } else if(cell.isMark()) {
                    canvas.drawRect(left, top, right, bottom, this.paintMark);
                } else {
                    canvas.drawRect(left, top, right, bottom, this.paintClose);
                }

                float centerX = left + this.cellWidth / 2f;
                float centerY = top + this.cellHeight / 2f - ((paintText.descent() + paintText.ascent()) / 2f);

                if(this.campoMinado.getCellByCoords(row, col).getBombsAround() != 0){
                    canvas.drawText(
                            String.valueOf(this.campoMinado.getCellByCoords(row, col).getBombsAround()),
                            centerX,
                            centerY,
                            this.paintText
                    );
                }else{
                    canvas.drawText(
                            "",
                            centerX,
                            centerY,
                            this.paintText
                    );
                }
                canvas.drawRect(left, top, right, bottom, this.paintGrid);
            }
        }


        if(this.campoMinado.isWin() || this.campoMinado.isLose()){

            Paint paintFundo = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintFundo.setColor(0xAA000000);
            canvas.drawRect(0, 0,getWidth(), getHeight(), paintFundo);

            String msg;
            if(this.campoMinado.isWin()){
                paintMessage.setColor(Color.CYAN);    // cor da mensagem
                msg = "YOU WIN!";
            } else {
                paintMessage.setColor(Color.RED);
                msg = "YOU LOSE!";
            }

            float cx = getWidth() / 2f;
            float cy = getHeight() / 2f - ((paintMessage.descent() + paintMessage.ascent()) / 2f);

            canvas.drawText(msg, cx, cy, paintMessage);

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(this.campoMinado.isLose() || this.campoMinado.isWin()){
            invalidate();
            return false;
        }

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                float x = event.getX(), y = event.getY();

                // ignora cliques fora do tabuleiro
                if (x < offsetX || x > offsetX + CampoMinado.totalColumn() * cellWidth
                        || y < offsetY || y > offsetY + CampoMinado.totalRow() * cellHeight) {
                    return true;
                }

                int row = (int) Math.floor((y - offsetY)  / this.cellHeight);
                int col = (int) Math.floor((x - offsetX)  / this.cellWidth);

                if(row >= 0 && col >=0 && row < CampoMinado.totalRow() && col < CampoMinado.totalColumn()) {
                    if(this.campoMinado.isMark()){
                        this.campoMinado.setCellMark(row, col);
                    }else{
                        this.campoMinado.openCell(row, col);
                    }
                    invalidate();
//                }
                }
                return true;
        }

        return super.onTouchEvent(event);
    }

    public void setDifficulty(){
        this.campoMinado.setDifficulty();
        invalidate();
    }
    public void setMark(){
        this.campoMinado.setMark();
        invalidate();
    }

    public void resetGame() {
        this.campoMinado.reset();
        invalidate(); // redesenha o tabuleiro
    }
}
