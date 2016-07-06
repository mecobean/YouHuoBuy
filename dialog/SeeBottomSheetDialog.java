package cn.bjsxt.youhuo.dialog;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import cn.bjsxt.youhuo.R;

/**
 * Created by Mecono on 2016/6/23.
 */
public class SeeBottomSheetDialog extends BottomSheetDialog {

    public SeeBottomSheetDialog(@NonNull Context context) {
        super(context);
//        activity = (Activity) context;
//        Window window = getWindow();
//        //去标题
//        window.requestFeature(Window.FEATURE_NO_TITLE);
//        //触摸dialog外边dismiss dialog
//        setCanceledOnTouchOutside(true);
//        //拿到decorView
//        decorView = window.getDecorView();
//        rootView = inflater.inflate(R.layout.dialog_loading, null);
//        setCanceledOnTouchOutside(false);
//        setContentView(rootView,window.getWindowManager().getDefaultDisplay().getWidth(), WindowManager.LayoutParams.WRAP_CONTENT);
    }
}
