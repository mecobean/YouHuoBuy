package cn.bjsxt.youhuo.util;

import android.app.Activity;
import android.content.Context;

import cn.bjsxt.youhuo.dialog.BaseDialog;
import cn.bjsxt.youhuo.dialog.LoadingDialog;
import cn.bjsxt.youhuo.fragment.MainHomeFragment;

/**
 * 网络访问的线程
 */
public class HttpThread extends Thread {

    private HttpUtil httpUtil;
    private String urlPath;
    private String params;
    private HttpHandler handler;
    private int what;
    private BaseDialog loadingDialog;
    private Activity activity;
    private boolean isShowLoadingDialog = true;
    /**
     * loadingDialog dismiss的接口回调
     */
    private OnLoadingDialogDismissListener dismissListener;
    private long lastTime;

    public HttpThread(Context context, String urlPath, String params, HttpHandler handler, int what) {
        httpUtil = new HttpUtil(context);
        this.urlPath = urlPath;
        this.params = params;
        this.handler = handler;
        this.what = what;
        activity = (Activity) context;
        loadingDialog = new LoadingDialog(activity);
        isShowLoadingDialog = true;
    }

    public HttpThread(Context context, String urlPath, String params, HttpHandler handler, int what, boolean isShowLoadingDialog) {
        httpUtil = new HttpUtil(context);
        this.urlPath = urlPath;
        this.params = params;
        this.handler = handler;
        this.what = what;
        activity = (Activity) context;
        loadingDialog = new LoadingDialog(activity);
        this.isShowLoadingDialog = isShowLoadingDialog;
    }

    /**
     * 网络请求
     */
    @Override
    public void run() {
        super.run();
        //判断当前是否有网络连接
        if (httpUtil.isNetOk()) {
            if (isShowLoadingDialog) {
                //显示加载的dialog
                lastTime = System.currentTimeMillis();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.show();
                    }
                });
            }
            //进行网络连接
            String result = httpUtil.requestPost(urlPath, params, what);
            if (result.equals("error")) {//返回码不是200
                handler.sendEmptyMessage(HttpModel.UNKNOWN_NETWORK);
            } else if (result.equals("netError")) {//网络请求出错
                handler.sendEmptyMessage(HttpModel.ERROR_NETWORK);
            } else {
                handler.obtainMessage(what, result).sendToTarget();
            }
            if (isShowLoadingDialog && loadingDialog != null) {
                long time = 1000 - (System.currentTimeMillis() - lastTime);
                if (time > 0) {//小于于一秒
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //取消加载的dialog
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.dismiss();
                        if (dismissListener != null && !loadingDialog.isShowing())
                            dismissListener.omDismiss();
                    }
                });

            }
        } else {//无网络连接
            handler.sendEmptyMessage(HttpModel.NO_NETWORK);
        }
    }

    public interface OnLoadingDialogDismissListener {
        void omDismiss();
    }

    public void setOnLoadingDialogDismissListener(OnLoadingDialogDismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }
}
