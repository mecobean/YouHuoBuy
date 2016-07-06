package cn.bjsxt.youhuo.view;

import android.content.Context;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bjsxt.youhuo.R;
import cn.bjsxt.youhuo.adapter.CategoryFirstMenuAdapter;
import cn.bjsxt.youhuo.adapter.CategorySecondMenuAdapter;
import cn.bjsxt.youhuo.bean.CategoryFirstMenuBean;
import cn.bjsxt.youhuo.bean.CategorySecondMenuBean;
import cn.bjsxt.youhuo.util.FileUtils;
import cn.bjsxt.youhuo.util.HttpHandler;
import cn.bjsxt.youhuo.util.HttpModel;
import cn.bjsxt.youhuo.util.HttpThread;
import cn.bjsxt.youhuo.util.JsonUtils;
import cn.bjsxt.youhuo.util.LogUtil;

/**
 * categoryFragment 分类的子View --> 品类
 * 实现切换不同的选项卡显示不同的界面
 * 数据从服务器请求
 */
public class CategoryPinLeiView implements View.OnClickListener {
    private Context context;
    /**
     * categoryChild 组合控件的根布局
     */
    private LinearLayout mLinear;
    private TextView boyTv;
    private TextView girlTv;
    private TextView lifeTv;
    private FileUtils fileUtils;
    private MyHandler handler;
    private JsonUtils jsonUtils;
    /**
     * 一级菜单的数据集合
     */
    private List<CategoryFirstMenuBean> firstMenuList = new ArrayList<>();
    /**
     * 二级菜单的数据集合
     */
    private List<CategorySecondMenuBean> secondMenuList = new ArrayList<>();
    private ListView firstLv;
    private ListView secondMenu;
    private CategoryFirstMenuAdapter firstMenuAdapter;
    private CategorySecondMenuAdapter secondMenuAdapter;
    private RelativeLayout secondMenuGroup;
    /**
     * 点击一级菜单保存的position
     * 判断一级菜单两次点击时候是同一个
     */
    private int lastPosition = -1;
    /**
     * 控制二级菜单的显示隐藏标记
     */
    private boolean isSecondMenuShow = false;
    private Animation hideAnimation;
    private Animation showAnimation;
    /**
     * 动画时候在执行的标记
     * 防止多次连续点击造成的动画的反复执行
     */
    private boolean isStartAnimaton = false;

    public CategoryPinLeiView(Context context) {
        this.context = context;
        initViews();
    }

    /**
     * 初始化组合控件
     */
    private void initViews() {
        mLinear = (LinearLayout) View.inflate(context, R.layout.view_category_pinlei, null);
        firstLv = (ListView) mLinear.findViewById(R.id.categoryChild_firstMenu);
        secondMenu = (ListView) mLinear.findViewById(R.id.categoryChild_secondMenu);
        secondMenuGroup = (RelativeLayout) mLinear.findViewById(R.id.categoryChild_secondMenuGroup);
        secondMenuGroup.setVisibility(View.GONE);
        boyTv = (TextView) mLinear.findViewById(R.id.category_pinlei_boy);
        girlTv = (TextView) mLinear.findViewById(R.id.category_pinlei_girls);
        lifeTv = (TextView) mLinear.findViewById(R.id.category_pinlei_life);
        boyTv.setSelected(true);

        initUtils();
        initSecondMenuAnimation();
        initListener();
        /**
         * 默认加载boy的选项卡
         */
        requestDatas(HttpHandler.CATEGORY_BOY_SUCCESS, HttpModel.URL + HttpModel.CATEGORY_BOY, HttpModel.KEY_BOY);

    }

    /**
     * 初始化二级菜单的动画
     */
    private void initSecondMenuAnimation() {
        hideAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1, 0, 0, 0, 0);
//        hideAnimation.setFillAfter(true);
        hideAnimation.setDuration(350);
        showAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0, 0, 0, 0, 0);
//        showAnimation.setFillAfter(true);
        showAnimation.setDuration(350);

    }

    /**
     * 初始化一些工具类
     */
    private void initUtils() {
        fileUtils = new FileUtils(context);
        handler = new MyHandler(context);
        jsonUtils = new JsonUtils();
    }


    /**
     * 处理消息
     */
    class MyHandler extends HttpHandler {

        public MyHandler(Context context) {
            super(context);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HttpHandler.CATEGORY_BOY_SUCCESS://boy选项卡的数据正常返回
                    //拿到数据解析 渲染显示
                    String result = msg.obj.toString();
                    handlerResult(result, HttpModel.KEY_BOY);
                    LogUtil.logE(LogUtil.DEBUG_TAG, result);
                    break;
                case HttpHandler.CATEGORY_GIRLS_SUCCESS://girls选项卡的数据正常返回
                    //拿到数据解析 渲染显示
                    handlerResult(msg.obj.toString(), HttpModel.KEY_GIRLS);
                    break;
                case HttpHandler.CATEGORY_LIFE_SUCCESS://life选项卡的数据正常返回
                    //拿到数据解析 渲染显示
                    handlerResult(msg.obj.toString(), HttpModel.KEY_LIFESTYLE);
                    break;
                case HttpHandler.CATEGORY_VALUE_SUCCESS:
                    //拿到数据解析 渲染显示
//                    LogUtil.logE(LogUtil.DEBUG_TAG,msg.obj.toString());
                    handlerResultSecond(msg.obj.toString());
                    break;
            }
        }
    }

    /**
     * 处理二级菜单返回的数据
     * 渲染数据
     *
     * @param json
     */
    private void handlerResultSecond(String json) {
        secondMenuList.clear();
        List<CategorySecondMenuBean> secondMenuParseJson = jsonUtils.getSecondMenuParseJson(json);
        secondMenuList.addAll(secondMenuParseJson);
        if (secondMenuAdapter == null) {
            secondMenuAdapter = new CategorySecondMenuAdapter(context, secondMenuList);
            secondMenu.setAdapter(secondMenuAdapter);
        } else {
            secondMenuAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 处理handler接受的数据
     * 根据不同的key 做不同的解析
     *
     * @param json 要解析的数据
     * @param key  每个选型卡的key
     */
    private void handlerResult(String json, String key) {
        firstMenuList.clear();
        List<CategoryFirstMenuBean> firstMenuBeen = jsonUtils.getFirstMenuParseJson(json, key);
        firstMenuList.addAll(firstMenuBeen);
        //渲染数据
        if (firstMenuAdapter == null) {
            firstMenuAdapter = new CategoryFirstMenuAdapter(context, firstMenuList);
            firstLv.setAdapter(firstMenuAdapter);
        } else {
            firstMenuAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 初始化listView的数据
     * 1.先从本地拿，如果本地没用从服务器请求
     *
     * @param what 区分文件
     * @param url  请求url
     * @param key  区分是那个选项卡的数据，boy/girls/lifeStyle
     */
    private void requestDatas(int what, String url, String key) {
        //判断本地存储
        if (fileUtils.isSaveFile("data" + what + ".txt")) {//存在
            //读取
            String result = fileUtils.readCacheJson("data" + what + ".txt");
            //解析数据 渲染数据
            handlerResult(result, key);
        } else {//不存在 网络请求
            new HttpThread(context, url, "", handler, what).start();
        }
    }

    /**
     * 对外提供方法拿到该组合控件
     *
     * @return 返回该组合控件
     */
    public LinearLayout getCategoryChild() {
        return mLinear;
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        boyTv.setOnClickListener(this);
        girlTv.setOnClickListener(this);
        lifeTv.setOnClickListener(this);
        firstLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isStartAnimaton) {
                    return;
                } else {
                    //3.控制二级菜单的显示隐藏
                    if (lastPosition == position) {
                        CategoryFirstMenuBean item = firstMenuAdapter.getItem(position);
                        if (isSecondMenuShow) {
                            item.setSelect(false);
                            secondMenuGroup.startAnimation(hideAnimation);
                            secondMenuGroup.setVisibility(View.GONE);
                        } else {
                            item.setSelect(true);
                            secondMenuGroup.startAnimation(showAnimation);
                            secondMenuGroup.setVisibility(View.VISIBLE);
                        }
                        isSecondMenuShow = !isSecondMenuShow;
                        firstMenuAdapter.notifyDataSetChanged();
                    } else {
                        //点击一级菜单item，显示二级菜单
                        //1.先拿到id
                        secondMenuGroup.setVisibility(View.VISIBLE);
                        CategoryFirstMenuBean item = firstMenuAdapter.getItem(position);
                        String _id = item.get_id();
                        item.setSelect(true);
                        if (lastPosition >= 0) {
                            CategoryFirstMenuBean bean = firstMenuList.get(lastPosition);
                            bean.setSelect(false);
                        }
                        firstMenuAdapter.notifyDataSetChanged();
                        //2.通过_id从网络获取二级菜单的数据
                        new HttpThread(context, HttpModel.URL + HttpModel.CATEGORY_VALUE, "parames={\"id\":" + _id + "}", handler, HttpHandler.CATEGORY_VALUE_SUCCESS,false).start();
                        lastPosition = position;
                        if (!isSecondMenuShow) {
                            secondMenuGroup.startAnimation(showAnimation);
                        }
                    }
                }
            }
        });
        /**
         * 动画在执行的时候加状态锁
         * 结束动画的售后
         */
        showAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isStartAnimaton = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isStartAnimaton = false;
                isSecondMenuShow = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        hideAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isStartAnimaton = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isStartAnimaton = false;
                isSecondMenuShow = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    /**
     * 不加动画的二级菜单写法
     *
     * @param position
     */
    private void showSecondMenu(int position) {
        //3.控制二级菜单的显示隐藏
        if (lastPosition == position) {
            CategoryFirstMenuBean item = firstMenuAdapter.getItem(position);
            if (isSecondMenuShow) {
                item.setSelect(false);
                secondMenuGroup.setVisibility(View.GONE);
            } else {
                item.setSelect(true);
                secondMenuGroup.setVisibility(View.VISIBLE);
            }
            isSecondMenuShow = !isSecondMenuShow;
            firstMenuAdapter.notifyDataSetChanged();
        } else {
            //点击一级菜单item，显示二级菜单
            //1.先拿到id
            secondMenuGroup.setVisibility(View.VISIBLE);
            CategoryFirstMenuBean item = firstMenuAdapter.getItem(position);
            String _id = item.get_id();
            item.setSelect(true);
            if (lastPosition >= 0) {
                CategoryFirstMenuBean bean = firstMenuList.get(lastPosition);
                bean.setSelect(false);
            }
            firstMenuAdapter.notifyDataSetChanged();
            //2.通过_id从网络获取二级菜单的数据
            new HttpThread(context, HttpModel.URL + HttpModel.CATEGORY_VALUE, "parames={\"id\":" + _id + "}", handler, HttpHandler.CATEGORY_VALUE_SUCCESS).start();
            lastPosition = position;
            isSecondMenuShow = true;
        }
    }

    /**
     * 点击选项卡 切换不同的字体颜色，从服务器请求一级菜单数据
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //先清空在选中
            case R.id.category_pinlei_boy:
                clearSelect();
                //设置选中
                boyTv.setSelected(true);
                //设置不可编辑
                boyTv.setEnabled(false);
                //获取一级菜单的数据
                requestDatas(HttpHandler.CATEGORY_BOY_SUCCESS, HttpModel.URL + HttpModel.CATEGORY_BOY, HttpModel.KEY_BOY);
                //判断是否隐藏二级菜单
                isShowMenu();
                break;
            case R.id.category_pinlei_girls:
                clearSelect();
                girlTv.setSelected(true);
                girlTv.setEnabled(false);
                requestDatas(HttpHandler.CATEGORY_GIRLS_SUCCESS, HttpModel.URL + HttpModel.CATEGORY_GIRL, HttpModel.KEY_GIRLS);
                isShowMenu();
                break;
            case R.id.category_pinlei_life:
                clearSelect();
                lifeTv.setSelected(true);
                lifeTv.setEnabled(false);
                requestDatas(HttpHandler.CATEGORY_LIFE_SUCCESS, HttpModel.URL + HttpModel.CATEGORY_LIFE, HttpModel.KEY_LIFESTYLE);
                isShowMenu();
                break;
        }
    }

    /**
     * 点击选项卡判断隐藏已显示的二级菜单
     */
    private void isShowMenu() {
        if (isSecondMenuShow) {
            secondMenuGroup.setVisibility(View.GONE);
            isSecondMenuShow = false;
        }
    }

    /**
     * 清空所有的选中状态
     * 清空所有的不可编辑状态
     */
    private void clearSelect() {
        boyTv.setSelected(false);
        girlTv.setSelected(false);
        lifeTv.setSelected(false);

        boyTv.setEnabled(true);
        girlTv.setEnabled(true);
        lifeTv.setEnabled(true);
    }
}
