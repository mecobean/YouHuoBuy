package cn.bjsxt.youhuo.view;

import android.content.Context;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Mecono on 2016/6/23.
 */
public class MySlidingPaneLayout extends SlidingPaneLayout{
    public MySlidingPaneLayout(Context context) {
        super(context);
    }

    public MySlidingPaneLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySlidingPaneLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isOpen()){
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

}
