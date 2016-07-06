package cn.bjsxt.youhuo.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

import cn.bjsxt.youhuo.MainActivity;
import cn.bjsxt.youhuo.R;
import cn.bjsxt.youhuo.util.ToastUtils;

/**
 * 自定义view .
 * 品牌分类右边的字母索引
 */
public class PinPaiIndexView extends View {
    private final Context context;
    /**
     * 要绘制的数据
     */
    private List<String> list;
    /**
     * 自定义view的宽
     */
    private int letterWidth;
    /**
     * 自定义view的高
     */
    private int letterHeight;
    private Paint paint;
    private float itemHeight;
    /**
     * 按下的item
     */
    private int position = -1;
    /**
     * 接口的实例
     */
    private onListViewPositionListener listener;
    /**
     * 判断是否松开
     */
    private boolean isUp = false;
    private Handler handler =  new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            isUp = true;
            paint.setColor(getResources().getColor(R.color.letter_text));
            invalidate();
            return true;
        }
    });
    public PinPaiIndexView(Context context) {
        this(context, null);
    }

    public PinPaiIndexView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setDither(true);//设置抗抖动
        paint.setTextSize(35);//px
    }

    /**
     * 获取 数据源
     *
     * @param list 要绘制的数据源
     */
    public void setLetterList(List<String> list) {
        this.list = list;
        //重绘 。--> 因为刚开始的时候list为空，在调用setLetterList之后list才会有值
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //拿到自定义View的宽高
        letterWidth = getMeasuredWidth();
        letterHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (list == null || list.size() == 0){
            return;
        }
        //绘制 计算每个字母的坐标 以及每个item的高度 一级每个字体的宽高
        itemHeight = letterHeight * 1.0f / list.size();
        for (int i = 0; i < list.size(); i++) {
            //拿到绘制的字
            String text = list.get(i);
            //计算字体的最小宽高
            Rect rect = new Rect();
            paint.getTextBounds(text,0,text.length(), rect);
            //计算绘制坐标
            float x = letterWidth / 2 - rect.width() / 2;
            float y = itemHeight / 2 + rect.height() / 2 + itemHeight *i;
            if (isUp){
                paint.setColor(getResources().getColor(R.color.letter_text));
            }else {
                if (position == i){
                    paint.setColor(getResources().getColor(R.color.pressTrue));
                }else {
                    paint.setColor(getResources().getColor(R.color.letter_text));
                }
            }

            canvas.drawText(text,x,y,paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (listener == null){
            return super.onTouchEvent(event);
        }
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                isUp = false;
                float y = event.getY();
                //计算按下是那个item  用当前按下的高度除以item的高度取整
                position = (int) (y / itemHeight);
                listener.scrollPosition(list.get(position));
                invalidate();
                break;
             case MotionEvent.ACTION_MOVE:
                 float moveY = event.getY();
                 //计算按下是那个item  用当前按下的高度除以item的高度取整
                 position = (int) (moveY/ itemHeight);
                // 判断position是否数组下标越界
                 if (position >= list.size()){
                     position = list.size()-1;
                 }
                 if (position < 0){
                     position = 0;
                 }
                 listener.scrollPosition(list.get(position));
                 invalidate();
                 break;
             case MotionEvent.ACTION_UP:
                 handler.sendEmptyMessageDelayed(0,3*1000);
                 break;
        }
        return true;
    }

    /**
     * 接口回调 传递当前选中的item的值
     */
    public interface  onListViewPositionListener{
        void scrollPosition(String text);
    }
    public void setOnListViewPositionListener(onListViewPositionListener listener){
        this.listener = listener;
    }
}
