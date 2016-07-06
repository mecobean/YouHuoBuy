package cn.bjsxt.youhuo.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import cn.bjsxt.youhuo.R;
import cn.bjsxt.youhuo.util.ToastUtils;

/**
 * 网络加载等待的dialog
 */
public class LoadingDialog extends BaseDialog{

    private final View rootView;
    public LoadingDialog(Context context) {
        super(context);
        rootView = inflater.inflate(R.layout.dialog_loading, null);
        setGravity(Gravity.CENTER);
        setCanceledOnTouchOutside(false);
        setContentView(rootView,window.getWindowManager().getDefaultDisplay().getWidth(), WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        ToastUtils.toast("数据正在加载中...");
    }
}
