package cn.bjsxt.youhuo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 *
 */
public class MyGridView extends GridView {

    public MyGridView(Context context) {
        super(context);
    }

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST));
//        int width = measureWidth(widthMeasureSpec);
//        int height = measureHeight(heightMeasureSpec);
//        setMeasuredDimension(width, height);

    }

    /**
     * 测量高度
     *
     * @param heightMeasureSpec
     * @return
     */
    private int measureHeight(int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int height = 0;
        if (heightMode == MeasureSpec.EXACTLY) {//准确值模式
            height = heightSize;
        } else {
            height = 200;
            if (heightMode == MeasureSpec.AT_MOST) {//最大值模式
                height = Math.min(height, heightSize);
            }
        }
        return height;
    }

    /**
     * 测量宽度
     *
     * @param widthMeasureSpec
     * @return
     */
    private int measureWidth(int widthMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int width = 0;
        if (widthMode == MeasureSpec.EXACTLY) {//准确值 match_parent
            width = widthSize;
        } else {
            width = 400;
            if (widthMode == MeasureSpec.AT_MOST) {//最大值模式 wrap_content
                width = Math.min(width, widthSize);
            }
        }
        return width;
    }
}
