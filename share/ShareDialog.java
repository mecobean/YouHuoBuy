package cn.bjsxt.youhuo.share;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import cn.bjsxt.youhuo.R;
import cn.bjsxt.youhuo.dialog.BaseDialog;
import cn.bjsxt.youhuo.util.ToastUtils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.system.text.ShortMessage;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.favorite.WechatFavorite;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 *
 */
public class ShareDialog extends BaseDialog implements View.OnClickListener, AdapterView.OnItemClickListener, PlatformActionListener{
    /**
     * 分享内容的参数实体类
     */
    private ShareWebBean shareWebBean;
    /**
     * 分享平台名称
     */
    private int[] res;
    /**
     * 分享平台Logo
     */
    private String[] titles;
    /**
     * gridView  (定高)
     */
    private GridView gv;
    /**
     * 取消按钮
     */
    private Button btn;
    /**
     * gridView 布局动画控制器
     */
    private LayoutAnimationController controller;
    /**
     * dialog 布局
     */
    private View rootView;

    public ShareDialog(Context context, ShareWebBean shareWebBean) {
        super(context);
        this.shareWebBean = shareWebBean;
        initView();
        initWindow();
        ShareSDK.initSDK(context);

    }

    /**
     * 初始化dialog
     */
    private void initView() {
        initData();
        //填充布局
        rootView = inflater.inflate(R.layout.share, null);
        gv = (GridView) rootView.findViewById(R.id.gv);
        btn = (Button) rootView.findViewById(R.id.cancel);

        initListener();
        //初始化dialog View
        setContentView(rootView, window.getWindowManager().getDefaultDisplay().getWidth(), WindowManager.LayoutParams.WRAP_CONTENT);
        initAdapter();
        window.getDecorView().setBackgroundColor(Color.TRANSPARENT);

//        window.getDecorView().setPadding(0,0,0,0);
        gv.setHorizontalSpacing(10);
        gv.setVerticalSpacing(10);
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        btn.setOnClickListener(this);
        gv.setOnItemClickListener(this);
    }

    /**
     * 初始化分享平台信息
     */
    private void initData() {
        res = new int[]{R.drawable.ssdk_oks_classic_wechat, R.drawable.ssdk_oks_classic_wechatmoments
                , R.drawable.ssdk_oks_classic_wechatfavorite
                , R.drawable.ssdk_oks_classic_qq, R.drawable.ssdk_oks_classic_sinaweibo
                , R.drawable.ssdk_oks_classic_qzone, R.drawable.ssdk_oks_classic_tencentweibo
                , R.drawable.ssdk_oks_classic_shortmessage

        };
        titles = new String[]{"微信", "朋友圈", "微信收藏", "qq", "新浪微博", "QQ空间", "腾讯微博", "短信"};

    }

    /**
     * 初始化gridVIew adapter
     */
    private void initAdapter() {
        MyAdapter adapter = new MyAdapter();
        gv.setAdapter(adapter);
        //缩放动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f
                , Animation.RELATIVE_TO_SELF, 0.5f
        );
        scaleAnimation.setDuration(1500);
        //布局动画  ------------------------------> 动画  -----每个item 执行动画的间隔时间 0（所有item同时执行）
        controller = new LayoutAnimationController(scaleAnimation, 0);
        /**
         * item执行布局动画的顺序
         * ORDER_REVERSE 反序
         * ORDER_RANDOM 随机
         * ORDER_NORMAL 默认顺序
         */
//        controller.setOrder(LayoutAnimationController.ORDER_REVERSE);


    }

    /**
     * 初始化dialog window属性
     */
    private void initWindow() {
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.dialogChooseAnimation);
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String title = titles[position];
        dismiss();
        switch (title) {
            case "微信":
                shareWeChat(shareWebBean.getText(),shareWebBean.getImgURL(), shareWebBean.getWebURL(), shareWebBean.getTitle());
                break;
            case "朋友圈":
                sharePengYou(shareWebBean.getText(),shareWebBean.getImgURL(), shareWebBean.getWebURL(), shareWebBean.getTitle());
                break;
            case "微信收藏":
                shareFavorite(shareWebBean.getText(),shareWebBean.getImgURL(), shareWebBean.getWebURL(), shareWebBean.getTitle());
                break;
            case "qq":
                shareQQ(shareWebBean.getText(),shareWebBean.getImgURL(), shareWebBean.getWebURL(), shareWebBean.getTitle());
                break;
            case "新浪微博":
                shareWIBo(shareWebBean.getText()+" "+shareWebBean.getWebURL(),shareWebBean.getImgURL());
                break;
            case "QQ空间":
                shareQzone(shareWebBean.getTitle(),shareWebBean.getWebURL(),shareWebBean.getText(),shareWebBean.getImgURL(),"有货商城",shareWebBean.getWebURL());
                break;
            case "腾讯微博":
                ToastUtils.toast("腾讯微博");
                break;
            case "短信":
                shareShortMesg(shareWebBean.getTitle(),shareWebBean.getText());
                break;
        }
    }

    /**
     * 分享到qq空间
     * @param title 标题
     * @param titleUrl 标题地址
     * @param text 内容
     * @param imgURL 图片
     * @param site 网站名称
     * @param siteUrl 网站url
     */
    private void shareQzone(String title, String titleUrl, String text, String imgURL, String site,String siteUrl) {
        //拿到ShareSKD分享
        Platform.ShareParams sp = new Platform.ShareParams();
        //设置分享类型 (网页)
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setText(text);
        sp.setImageUrl(imgURL);
        sp.setTitle(title);
        sp.setTitleUrl(titleUrl);
        sp.setSite(site);
        sp.setSiteUrl(siteUrl);
        //设置分享平台 （微信朋友圈）
        Platform platform = ShareSDK.getPlatform(QZone.NAME);
        //检查是否用客户端
        boolean b = platform.isClientValid();
        if (!b) {
            Toast.makeText(activity, "当前没有Qzone客户端", Toast.LENGTH_SHORT).show();
        }
        //分享
        platform.share(sp);
        //分享回调
        platform.setPlatformActionListener(this);
    }

    /**
     * 收藏到微信
     * @param text
     * @param imgURL
     * @param webURL
     * @param title
     */
    private void shareFavorite(String text, String imgURL, String webURL, String title) {
        //拿到ShareSKD分享
        Platform.ShareParams sp = new Platform.ShareParams();
        //设置分享类型 (网页)
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setText(text);
        sp.setImageUrl(imgURL);
        sp.setTitle(title);
        sp.setUrl(webURL);
        //设置分享平台 （微信朋友圈）
        Platform platform = ShareSDK.getPlatform(WechatFavorite.NAME);
        //检查是否用客户端
        boolean b = platform.isClientValid();
        if (!b) {
            Toast.makeText(activity, "当前没有微信客户端", Toast.LENGTH_SHORT).show();
        }
        //分享
        platform.share(sp);
        //分享回调
        platform.setPlatformActionListener(this);
    }

    /**
     * 分享到短信
     * @param title
     * @param text
     */
    private void shareShortMesg(String title, String text) {
        Platform.ShareParams platform = new Platform.ShareParams();
        platform.setShareType(Platform.SHARE_TEXT);
        platform.setTitle(title);
        platform.setText(text);
        Platform sp = ShareSDK.getPlatform(ShortMessage.NAME);
        //分享
        sp.share(platform);
        //分享回调
        sp.setPlatformActionListener(this);
    }

    /**
     * 微信朋友圈
     *
     * @param text      分享文本
     * @param imagePath 图片路径
     * @param url       网页URL
     * @param title     分享内容标题
     */
    private void sharePengYou(String text, String imagePath, String url, String title) {
        //拿到ShareSKD分享
        Platform.ShareParams sp = new Platform.ShareParams();
        //设置分享类型 (网页)
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setText(text);
        sp.setImageUrl(imagePath);
        sp.setTitle(title);
        sp.setUrl(url);
        //设置分享平台 （微信朋友圈）
        Platform platform = ShareSDK.getPlatform(WechatMoments.NAME);
        //检查是否用客户端
        boolean b = platform.isClientValid();
        if (!b) {
            Toast.makeText(activity, "当前没有微信客户端", Toast.LENGTH_SHORT).show();
        }
        //分享
        platform.share(sp);
        //分享回调
        platform.setPlatformActionListener(this);
    }

    /**
     * 分享到新浪微博
     * @param text 分享的内容（可拼接webUrl）
     * @param imagePath 图片路径
     */
    private void shareWIBo(String text, String imagePath) {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setText(text);
        sp.setImageUrl(imagePath);
        Platform platform = ShareSDK.getPlatform(SinaWeibo.NAME);
        platform.share(sp);
        platform.setPlatformActionListener(this);
    }

    private void shareQQ(String text, String imagePath, String textUrl, String title) {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setText(text);
        sp.setImageUrl(imagePath);
        sp.setTitleUrl(textUrl);
        sp.setTitle(title);
        Platform platform = ShareSDK.getPlatform(QQ.NAME);
        platform.share(sp);
        platform.setPlatformActionListener(this);
    }

    private void shareWeChat(String text, String imagePath, String url, String title) {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setText(text);
        sp.setImageUrl(imagePath);
        sp.setTitle(title);
        sp.setUrl(url);
        Platform platform = ShareSDK.getPlatform(Wechat.NAME);
        boolean b = platform.isClientValid();
        if (!b) {
            Toast.makeText(activity, "当前没有微信客户端", Toast.LENGTH_SHORT).show();
        }

        platform.share(sp);
        platform.setPlatformActionListener(this);
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

        Toast.makeText(activity, "分享成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        Toast.makeText(activity, "分享失败-->" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancel(Platform platform, int i) {
        Toast.makeText(activity, "分享取消", Toast.LENGTH_SHORT).show();
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = View.inflate(activity, R.layout.item_share, null);
            TextView tv = (TextView) convertView.findViewById(R.id.tv);
            tv.setText(titles[position]);
            ImageView iv = (ImageView) convertView.findViewById(R.id.iv);
            iv.setImageResource(res[position]);
            return convertView;
        }
    }

}
