package cn.bjsxt.youhuo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import cn.bjsxt.youhuo.R;

/**
 * Created by Mecono on 2016/5/29.
 */
public class MyNumProgressBar extends View {

    private int unArriveBarColor;
    private int arriveBarColor;
    private int textColor;
    private float textSize;
    private float pbHeight;
    private Paint arrivePaint;
    private TextPaint textPaint;
    private Paint unArrivePaint;

    private int currentProgress = 50;
    private String currentDrawText;
    private float drawTextWidth;

    public MyNumProgressBar(Context context) {
        this(context, null);
    }

    public MyNumProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyNumProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyNumProgressBar);
        pbHeight = ta.getDimension(R.styleable.MyNumProgressBar_progressBarHeight, getDefValue(4, TypedValue.COMPLEX_UNIT_DIP));
        textSize = ta.getDimension(R.styleable.MyNumProgressBar_textSize, getDefValue(15, TypedValue.COMPLEX_UNIT_SP));
        textColor = ta.getColor(R.styleable.MyNumProgressBar_textColor, Color.parseColor("#f15c6b"));
        arriveBarColor = ta.getColor(R.styleable.MyNumProgressBar_arriveBarColor, Color.parseColor("#FF4081"));
        unArriveBarColor = ta.getColor(R.styleable.MyNumProgressBar_unArriveBarColor, Color.parseColor("#2EAA4C"));
        initPaint();
        ta.recycle();
    }

    private void initPaint() {
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(textSize);
        textPaint.setColor(textColor);

        arrivePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        arrivePaint.setColor(arriveBarColor);
        arrivePaint.setStrokeWidth(pbHeight);

        unArrivePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        unArrivePaint.setColor(unArriveBarColor);
        unArrivePaint.setStrokeWidth(pbHeight);
    }

    private float getDefValue(int defValue, int type) {
        return TypedValue.applyDimension(type, defValue, getResources().getDisplayMetrics());
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        currentDrawText = String.format("%d", currentProgress * 100 / (int) (getMeasuredWidth() - drawTextWidth));
        currentDrawText = currentDrawText + "%";
        //测量文本的宽度
        Rect bounds = new Rect();
        textPaint.getTextBounds(currentDrawText, 0, currentDrawText.length() - 1, bounds);
        drawTextWidth = textPaint.measureText(currentDrawText);

        canvas.drawLine(drawTextWidth + currentProgress, getMeasuredHeight() / 2, getMeasuredWidth(), getMeasuredHeight() / 2, unArrivePaint);
        canvas.drawText(currentDrawText, 0 + currentProgress, getMeasuredHeight() / 2 - (bounds.top + bounds.bottom) / 2, textPaint);
        canvas.drawLine(0, getMeasuredHeight() / 2, currentProgress, getMeasuredHeight() / 2, arrivePaint);
    }

    /**
     * 设置进度
     * @param position
     */
    public void startProgress(final int position) {
        currentProgress = (int) ((getMeasuredWidth() - drawTextWidth) / 100 * position);
        invalidate();

    }
}
