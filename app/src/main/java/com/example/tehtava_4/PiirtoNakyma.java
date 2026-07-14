package com.example.tehtava_4;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

public class PiirtoNakyma extends View {

    public enum ToolType { PEN, CIRCLE, SQUARE }
    public ToolType currentTool = ToolType.PEN;
    public static class Stroke {
        public Path path;
        public Paint paint;
        public Stroke(Path path, Paint paint) {
            this.path = path;
            this.paint = new Paint(paint);
        }
    }

    private List<Stroke> allStrokes = new ArrayList<>();
    private Path currentPath;
    private Paint currentPaint = new Paint();
    private PointF ympyranKeskipiste;

    public PiirtoNakyma(Context context, AttributeSet attrs) {
        super(context, attrs);
        currentPaint.setStyle(Paint.Style.STROKE);
        currentPaint.setStrokeCap(Paint.Cap.ROUND);
        currentPaint.setStrokeWidth(10f);
        currentPaint.setColor(Color.BLACK);
        currentPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (Stroke s : allStrokes) {
            canvas.drawPath(s.path, s.paint);
        }

        if (currentPath != null) {
            canvas.drawPath(currentPath, currentPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        if (currentTool == ToolType.CIRCLE) {
            handleCircleTouch(event, x, y);
        } else if (currentTool == ToolType.SQUARE) {
            handleSquareTouch(event, x, y);
        } else {
            handlePenTouch(event, x, y);
        }
        invalidate();
        return true;
    }

    public void updateBrush(float width, int color) {
        currentPaint.setStrokeWidth(width);
        currentPaint.setColor(color);
    }

    private void handlePenTouch(MotionEvent event, float x, float y) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                currentPath = new Path();
                currentPath.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                currentPath.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                allStrokes.add(new Stroke(currentPath, currentPaint));
                currentPath = null;
                break;
        }
    }

    private void handleCircleTouch(MotionEvent event, float x, float y) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ympyranKeskipiste = new PointF(x, y);
                currentPath = new Path();
                break;
            case MotionEvent.ACTION_MOVE:
                float radius = (float) Math.hypot(x - ympyranKeskipiste.x, y - ympyranKeskipiste.y);
                currentPath.reset();
                currentPath.addCircle(ympyranKeskipiste.x, ympyranKeskipiste.y, radius, Path.Direction.CW);
                break;
            case MotionEvent.ACTION_UP:
                allStrokes.add(new Stroke(currentPath, currentPaint));
                currentPath = null;
                break;
        }
    }
    private void handleSquareTouch(MotionEvent event, float x, float y) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ympyranKeskipiste = new PointF(x, y);
                currentPath = new Path();
                break;
            case MotionEvent.ACTION_MOVE:
                currentPath.reset();
                currentPath.addRect(ympyranKeskipiste.x, ympyranKeskipiste.y, x, y, Path.Direction.CW);
                break;
            case MotionEvent.ACTION_UP:
                allStrokes.add(new Stroke(currentPath, currentPaint));
                currentPath = null;
                break;
        }
    }
    public void setPaintColor(int color) { currentPaint.setColor(color); }
    public void setStrokeWidth(float width) { currentPaint.setStrokeWidth(width); }
    public void reset() {
        allStrokes.clear();
        invalidate();
    }
}