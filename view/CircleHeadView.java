package cn.bjsxt.youhuo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import cn.bjsxt.youhuo.R;

/**
 * 自定义View 实现圆角头像
 */
public class CircleHeadView extends View {

    private int resourceId;
    /**
     * 初始图片 不可编辑
     */
    private Bitmap bitmap;
    /**
     * 圆环的颜色
     */
    private int color;
    /**
     * 圆环的宽度
     */
    private float borderWidth;
    private Bitmap optionBitmap;
    private BitmapShader bitmapShader;
    private Paint headPaint;
    private Paint borderPaint;

    public CircleHeadView(Context context) {
        this(context,null);
    }

    public CircleHeadView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //拿到自定义的属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.CircleHeadView);
        resourceId = typedArray.getResourceId(R.styleable.CircleHeadView_resId, 0);
        if (resourceId > 0){
            bitmap = BitmapFactory.decodeResource(getResources(),resourceId);
        }
        color = typedArray.getColor(R.styleable.CircleHeadView_annulusColor, Color.GRAY);
        borderWidth = typedArray.getDimension(R.styleable.CircleHeadView_annulusWidth, 4);
        typedArray.recycle();
    }

    /**
     *   private static final int MODE_SHIFT = 30;
     * 测量模式 AT_MOST = 2 << MODE_SHIFT   高2位（为测量模式） 二进制位为10 右移30位 在右边补30个0 为测量的值
     * 其AT_MOST的最大值为0111111111···正好是integer的最大值右移两位
     * 而 Integer.MAX_VALUE <<2 为（第一个0表示正负，后边补32个1 为最大值 二进制表示）011111111111111111···
     *
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(width,height);
    }

    /**
     * 测量高度
     * @param heightMeasureSpec
     * @return
     */
    private int measureHeight(int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int height = 0;
        if (heightMode == MeasureSpec.EXACTLY){
            height = heightSize;
        }else {
            height = 200;
            if (heightMode == MeasureSpec.AT_MOST){
                height = Math.min(height,heightSize);
            }
        }
        return height;
    }

    /**
     * 测量宽度
     * @param widthMeasureSpec
     * @return
     */
    private int measureWidth(int widthMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int width = 0;
        if (widthMode == MeasureSpec.EXACTLY){//准确模式 match_parent
            width = widthSize;
        }else {
            width = 200;
            if (widthMode == MeasureSpec.AT_MOST){//最大值模式 wrap_content
                width = Math.min(width,widthSize);
            }
        }
        return width;
    }

    /**
     * 当布局的大小发生改变的时候调用 在onMeasure方法之后或者在onLayout
     * 转换bitmap 为可编辑
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
        if (bitmap != null){
            initShader();
        }
        //绘制圆角头像的画笔
        headPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (bitmapShader != null){
            headPaint.setShader(bitmapShader);
        }
        //画圆环的画笔
        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setColor(color);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(borderWidth);
    }

    /**
     * 初始化shader
     */
    private void initShader() {
        //创建一个新的可编辑的bitmap
        optionBitmap = Bitmap.createBitmap(bitmap);
        //bitmapShader 画圆 -----------------------> 模式 clamp 对最后一行像素拉伸
        bitmapShader = new BitmapShader(optionBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        //设置图片的缩放
        Matrix matrix = new Matrix();
        int optionBitmapHeight = (int) (optionBitmap.getHeight() - borderWidth*2);
        int optionBitmapWidth = (int) (optionBitmap.getWidth()-borderWidth*2);

        int measuredHeight = getMeasuredHeight();
        int measuredWidth = getMeasuredWidth();

        float scaleX = measuredHeight * 1.0f / optionBitmapHeight;
        float scaleY = measuredWidth * 1.0f / optionBitmapWidth;

        matrix.setScale(Math.max(scaleX,scaleY),Math.min(scaleX,scaleY));
        bitmapShader.setLocalMatrix(matrix);
    }

    /**
     *绘制
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //计算要绘制圆形头像的最小半径
        int r= (int) (Math.min(getMeasuredHeight(),getMeasuredWidth())/2-borderWidth);
        canvas.drawCircle(getMeasuredWidth()/2,getMeasuredHeight()/2,r,headPaint);
        RectF rectF = new RectF(getMeasuredWidth() / 2 - r-borderWidth/2, getMeasuredHeight() / 2 - r-borderWidth/2, getMeasuredWidth() / 2 +r+borderWidth/2, getMeasuredHeight() / 2 + r+borderWidth/2);
        canvas.drawArc(rectF,0,360,false,borderPaint);
    }

    public void setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        //
        initShader();
        if (headPaint != null){
            headPaint.setShader(bitmapShader);
        }
        invalidate();
    }
}
