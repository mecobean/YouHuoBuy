package cn.bjsxt.youhuo.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import cn.bjsxt.youhuo.MyApplication;
import cn.bjsxt.youhuo.R;
import cn.bjsxt.youhuo.bean.AddCartParamesBean;
import cn.bjsxt.youhuo.bean.AddCartResultBean;
import cn.bjsxt.youhuo.bean.CartListInfoBean;
import cn.bjsxt.youhuo.bean.GoodsDetailBean;
import cn.bjsxt.youhuo.util.HttpHandler;
import cn.bjsxt.youhuo.util.HttpModel;
import cn.bjsxt.youhuo.util.HttpThread;
import cn.bjsxt.youhuo.util.ToastUtils;

/**
 * Created by Mecono on 2016/6/17.
 */
public class ChooseGoodsDialog extends BaseDialog implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, HttpThread.OnLoadingDialogDismissListener {

    private  View rootView;
    /**
     * 商品图片
     */
    public ImageView goodsImg;
    /**
     * 商品标题
     */
    public TextView goodsTitle;
    /**
     * 商品现价
     */
    public TextView price;
    /**
     * 商品折扣价
     */
    public TextView discount;
    /**
     * 商品数量减少按钮
     */
    private  TextView subtractNum;
    /**
     * 商品数量
     */
    private  TextView goodsNum;
    /**
     * 商品数量增加按钮
     */
    private  TextView addNum;
    /**
     * 确认添加到购物车
     */
    private  Button sure;
    /**
     * 商品的数量（默认为1）
     */
    private int num = 1;
    /**
     * 商品颜色
     */
    private  RadioGroup goodsColor;
    /**
     * 商品大小
     */
    private  RadioGroup goodsSize;
    /**
     * 已选择的商品颜色
     */
    private String color;
    /**
     * 已选择的商品尺寸
     */
    private String size;
    /**
     * 添加购物车回调接口
     */
    private OnAddCartSureListener listener;
    private String id;

    public ChooseGoodsDialog(Context context, GoodsDetailBean goodsDetailBean) {
        super(context);
        initViews(context,goodsDetailBean.getId(),goodsDetailBean.getImgList().get(0),goodsDetailBean.getTitle(),goodsDetailBean.getPrice(),goodsDetailBean.getDiscount());
        initListener();
        initDialog();
    }
    public ChooseGoodsDialog(Context context, CartListInfoBean.CartBean cartBean) {
        super(context);
        initViews(context,"1",cartBean.getImgpath(),cartBean.getTitle(),cartBean.getPrice(),"0");
        initListener();
        initDialog();
    }
    private void initDialog() {
        setContentView(rootView, window.getWindowManager().getDefaultDisplay().getWidth(), WindowManager.LayoutParams.WRAP_CONTENT);
        //遇到黑边设置window的背景颜色或者设置decorView 的背景为透明
        window.setBackgroundDrawableResource(R.color.f4);
//        decorView.setBackgroundColor(Color.TRANSPARENT);
        setGravity(Gravity.BOTTOM);
        setAnimation(R.style.dialogChooseAnimation);
    }

    private void initListener() {
        goodsColor.setOnCheckedChangeListener(this);
        goodsSize.setOnCheckedChangeListener(this);
        subtractNum.setOnClickListener(this);
        addNum.setOnClickListener(this);
        sure.setOnClickListener(this);
    }

    /**
     * 初始化 控件
     */
    private void initViews(Context context,String id,String imgPath,String title,String prices,String discounts) {
        decorView.setPadding(0, 0, 0, 0);
        rootView = inflater.inflate(R.layout.dialog_choose_goods_info, null);
        this.id = id;
        //加载缩略图
        goodsImg = (ImageView) rootView.findViewById(R.id.dialog_choose_iv);
        Picasso.with(context).load(HttpModel.IMGURL + imgPath).placeholder(R.mipmap.share_loading).into(goodsImg);

        //商品名称
        goodsTitle = (TextView) rootView.findViewById(R.id.dialog_choose_goodsTitle);
        goodsTitle.setText(title);

        //商品价格
        price = (TextView) rootView.findViewById(R.id.dialog_choose_price);
        discount = (TextView) rootView.findViewById(R.id.dialog_choose_discount);
        price.setText("￥" + prices);
        String value = "￥" + (Double.parseDouble(discounts) + Double.parseDouble(prices));
        //设置中划线
        SpannableString spannableString = new SpannableString(value);
        spannableString.setSpan(new StrikethroughSpan(), 0, value.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        discount.setText(spannableString);

        //数量
        subtractNum = (TextView) rootView.findViewById(R.id.dialog_choose_subtract);
        goodsNum = (TextView) rootView.findViewById(R.id.dialog_choose_goodsNum);
        addNum = (TextView) rootView.findViewById(R.id.dialog_choose_add);

        sure = (Button) rootView.findViewById(R.id.dialog_choose_sure);

        goodsColor = (RadioGroup) rootView.findViewById(R.id.dialog_choose_goodsColor);
        goodsSize = (RadioGroup) rootView.findViewById(R.id.dialog_choose_goodsSize);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_choose_subtract://商品数量减
                if (num == 1) {
                    num = 1;
                } else {
                    num--;
                }
                goodsNum.setText(num + "");
                break;
            case R.id.dialog_choose_add://商品数量加
                num++;
                goodsNum.setText(num + "");
                break;
            case R.id.dialog_choose_sure://添加到购物车
                addCart();
                break;
        }
    }

    /**
     * 确定添加到购物车
     */
    private void addCart() {
        //判断是否选择颜色 尺寸
        if (color == null || color.equals("")) {
            sure.setText("请选择颜色");
            sure.setTextColor(Color.parseColor("#000000"));
        } else if (size == null || size.equals("")) {
            sure.setText("请选择尺寸");
            sure.setTextColor(Color.parseColor("#000000"));
        } else {
            //向服务器提交数据
            if (MyApplication.getInstance().userInfoBean != null) {
                AddCartParamesBean paramesBean = new AddCartParamesBean("10", id, "10", MyApplication.getInstance().userInfoBean.getId());
                String parames = new Gson().toJson(paramesBean);
                HttpThread addCartThread = new HttpThread(activity, HttpModel.ADD_CART_URL, "parames=" + parames, new ChooseGoodsHanler(activity), HttpHandler.ADD_CART_SUCCESS);
                addCartThread.setOnLoadingDialogDismissListener(this);
                addCartThread.start();
            }
        }
    }

    @Override
    public void omDismiss() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) goodsImg.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        listener.addCartSuccess(num,bitmap);
        dismiss();
    }

    class ChooseGoodsHanler extends HttpHandler{

        public ChooseGoodsHanler(Context context) {
            super(context);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == HttpHandler.ADD_CART_SUCCESS){
                String s = msg.obj.toString();
                AddCartResultBean successBean = new Gson().fromJson(s, AddCartResultBean.class);
                if (HttpModel.SUCCESSFULLY_OK.equals(successBean.scuess)){
                    ToastUtils.toast("添加成功");
                }else {
                    ToastUtils.toast("添加失败");
                }
            }
        }
    }
    public interface OnAddCartSureListener {
        /**
         * 添加到购物车
         * @param goodsNumber    添加商品的数量
         */
        void addCartSuccess(int goodsNumber, Bitmap bitmap);
    }

    public void setOnAddCartSureListener(OnAddCartSureListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        sure.setText("确认");
        sure.setTextColor(Color.parseColor("#f4f4f4"));
        switch (checkedId) {
            case R.id.dialog_choose_goodsColor_black://黑色
                color = "黑色";
                break;
            case R.id.dialog_choose_goodsColor_blue://蓝色
                color = "蓝色";
                break;
            case R.id.dialog_choose_goodsColor_gray://灰色
                color = "灰色";
                break;
            case R.id.dialog_choose_goodsColor_red://红色
                color = "红色";
                break;
            case R.id.dialog_choose_goodsSize_l:
                size = "l";
                break;
            case R.id.dialog_choose_goodsSize_s:
                size = "s";
                break;
            case R.id.dialog_choose_goodsSize_m:
                size = "m";
                break;
            case R.id.dialog_choose_goodsSize_xl:
                size = "xl";
                break;

        }
    }
}
