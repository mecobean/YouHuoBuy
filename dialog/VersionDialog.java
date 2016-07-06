package cn.bjsxt.youhuo.dialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bjsxt.youhuo.R;
import cn.bjsxt.youhuo.bean.VersionBean;

/**
 * 版本更新 dialog
 */
public class VersionDialog extends BaseDialog {

    private  VersionBean versionBean;
    @Bind(R.id.version_content)
    TextView versionContent;
    @Bind(R.id.version_updata)
    Button versionUpdata;
    @Bind(R.id.version_cancel)
    Button versionCancel;
    private View rootView;

    public VersionDialog(Context context, VersionBean versionBean) {
        super(context);
        this.versionBean = versionBean;
        rootView = inflater.inflate(R.layout.dialog_version, null);
        ButterKnife.bind(this,rootView);
        StringBuilder sb = new StringBuilder();
        sb.append("最新版本："+versionBean.getVersion()+"\n");
        sb.append("新版本大小：3.14M\n\n");
        sb.append("更新内容：\n");
        sb.append("1.此处乱码.....\n");
        sb.append("2.优化用户体验，炫酷交互等你来探索\n");
        sb.append("PS：服务器下载地址失效，点击不会下载.....");
        versionContent.setText(sb.toString());
        setContentView(rootView, (int) (activity.getWindowManager().getDefaultDisplay().getWidth()*0.8f), WindowManager.LayoutParams.WRAP_CONTENT);
        decorView.setBackgroundColor(Color.TRANSPARENT);
        setGravity(Gravity.CENTER);
    }

    @OnClick({R.id.version_updata, R.id.version_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.version_updata://更新
                //intent
                Intent intent = new Intent(Intent.ACTION_VIEW);
                //设置uri
                intent.setData(Uri.parse(versionBean.getUrl()));
                activity.startActivity(intent);
                dismiss();
                break;
            case R.id.version_cancel://取消
                dismiss();
                break;
        }
    }

}
