package com.example.campominado;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;

public class BoardView extends View {

    private float cellSize, offsetX = 0f, offsetY = 0f;//cellWidth, cellHeight

    private CampoMinado campoMinado;
    private Drawable bombDrawable;
    private SoundPool soundPool;
    private int bombSoundId;

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

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(4)
                .setAudioAttributes(audioAttributes)
                .build();

        bombSoundId = soundPool.load(getContext(), R.raw.audio_bomba, 1);

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
        this.paintGrid.setStrokeWidth(20f);
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

        this.cellSize = Math.min(cellSizeByWidth, cellSizeByHeight);

        float boardWidth =  this.cellSize * CampoMinado.totalColumn();
        float boardHeight =  this.cellSize * CampoMinado.totalRow();

        this.offsetX = (w - boardWidth) / 2f;
        this.offsetY = (h - boardHeight) / 2f;

        this.offsetX = (w - boardWidth) / 2f;
        this.offsetY = (h - boardHeight) / 2f;

        this.paintText.setTextSize(((this.cellSize) * 0.6f));
        this.paintMessage.setTextSize(Math.min(w, h) * 0.15f);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.DKGRAY);
        for(int row = 0; row < CampoMinado.totalRow(); row++){
            for(int col = 0; col < CampoMinado.totalColumn(); col++){
                Cell cell = this.campoMinado.getCellByCoords(row, col);

                float left = offsetX + col * this.cellSize;
                float top = offsetY + row * this.cellSize;
                float right = left + this.cellSize;
                float bottom = top + this.cellSize;

                if(cell.isOpen()){
                    if(cell.isHasBomb()){
                        canvas.drawRect(left, top, right, bottom, this.paintBomb);
                        designBomb(canvas, left, top, right, bottom);

                        if (soundPool != null && bombSoundId != 0) {
                            soundPool.play(
                                    bombSoundId,
                                    1.0f,
                                    1.0f,
                                    1,
                                    0,
                                    1.0f
                            );
                        }
                    }else{
                        canvas.drawRect(left, top, right, bottom, this.paintOpen);
                    }
                } else if(cell.isMark()) {
                    canvas.drawRect(left, top, right, bottom, this.paintMark);
                } else {
                    canvas.drawRect(left, top, right, bottom, this.paintClose);
                }
                designText(canvas, left, top, row, col);
                canvas.drawRect(left, top, right, bottom, this.paintGrid);
            }
        }
        designEndGame(canvas);
    }

    private void designBomb(Canvas canvas, float left, float top, float right, float bottom){
        if(bombDrawable != null){
            float cellW = this.cellSize, cellH = this.cellSize;
            int bombW = bombDrawable.getIntrinsicWidth(), bombH = bombDrawable.getIntrinsicHeight();
            int imgL, imgR, imgT, imgB;

            if(bombH > 0 && bombW > 0) {
                float scale = 0.9f * Math.min(cellW / bombW, cellH / bombH);

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
    }

    private void designText(Canvas canvas, float left, float top, int row, int col){
        if(this.campoMinado.getCellByCoords(row, col).getBombsAround() != 0) {
            float centerX = left + this.cellSize / 2f;
            float centerY = top + this.cellSize / 2f - ((paintText.descent() + paintText.ascent()) / 2f);

            canvas.drawText(
                    String.valueOf(this.campoMinado.getCellByCoords(row, col).getBombsAround()),
                    centerX,
                    centerY,
                    this.paintText
            );
        }
    }

    private void designEndGame(Canvas canvas){
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
                if (x < offsetX || x > offsetX + CampoMinado.totalColumn() * this.cellSize
                        || y < offsetY || y > offsetY + CampoMinado.totalRow() * this.cellSize) {
                    return true;
                }

                int row = (int) Math.floor((y - offsetY)  / this.cellSize);
                int col = (int) Math.floor((x - offsetX)  / this.cellSize);

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
