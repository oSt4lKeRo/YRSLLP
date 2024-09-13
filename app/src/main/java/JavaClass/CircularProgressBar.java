package JavaClass;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CircularProgressBar extends View {

    private Paint paint;
    private Paint textPaint;
    private int progress;

    public CircularProgressBar(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        init();
    }

    private void init(){
        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(20);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setStrokeWidth(50);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);

        progress = 0;
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 2 - 20;

        canvas.drawCircle(width / 2, height / 2, radius, paint);

        float angle = 360 * progress / 100;
        canvas.drawArc(0, 20, width - 20, height - 20, -90, angle, false, paint);

        String progressText = progress + "%";
        canvas.drawText(progressText, width / 2, height / 2 + 20, textPaint);
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

}
