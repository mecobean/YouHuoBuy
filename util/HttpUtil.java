package cn.bjsxt.youhuo.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 网络访问工具类
 */
public class HttpUtil {
    private Context context;

    public HttpUtil(Context context) {
        this.context = context;
    }

    /**
     * 判断网络链接
     *
     * @return 当wifi或mobile网络的状态为CONNECTED的时候，返回true；否则返回false
     */
    public boolean isNetOk() {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo.State wifiState = wifiInfo.getState();
        if (NetworkInfo.State.CONNECTED.equals(wifiState)) {
            LogUtil.logW(LogUtil.INFO_TAG, "当前wifi联网");
            return true;
        }
        NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo.State mobileState = mobileInfo.getState();
        if (NetworkInfo.State.CONNECTED.equals(mobileState)) {
            LogUtil.logW(LogUtil.INFO_TAG, "当前mobile联网");
            return true;
        }
        return false;
    }

    /**
     * post方式进行网络访问（返回结果为Json/String类型用此方法）
     * 定义当what值大于0的时候保存数据  下于0 不保存
     *
     * @param urlPath 访问的地址
     * @param params  参数
     * @return 服务器返回的结果
     */
    public String requestPost(String urlPath, String params, int what) {
        String result = null;
        try {
            //创建访问的路径
            URL url = new URL(urlPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置超时
            conn.setConnectTimeout(10 * 1000);
            conn.setReadTimeout(10 * 1000);
            //设置请求的方法
            conn.setRequestMethod("POST");
            //设置可读可写
            conn.setDoInput(true);
            conn.setDoOutput(true);

            //写入请求参数
            OutputStream os = conn.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            byte[] bytes = params.getBytes();
            dos.write(bytes);
            dos.close();
            os.close();

            //读取服务器返回的Json数据
            conn.connect();
            if (conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuffer sb = new StringBuffer();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                result = sb.toString().trim();
                br.close();
                is.close();

                //将文件保存到内部存储中
                if (what > 0) {
//                    if (result.startsWith("[")){
//                        new FileUtils(context).saveJsonFile(result, "data" + what + ".txt");
//                    }else {
//                        JSONObject jsonObject = new JSONObject(result);
//                        String scuess = jsonObject.getString("sucess");
//                        String sucessfully = jsonObject.getString("sucessfully");
//                        scuess = ( (scuess == null ? "" : scuess)+(sucessfully == null ? "" : sucessfully) );
//                        if (TextUtils.isEmpty(scuess)) {
//                            new FileUtils(context).saveJsonFile(result, "data" + what + ".txt");
//                        }else if ("ok".equals(scuess)){
                    new FileUtils(context).saveJsonFile(result, "data" + what + ".txt");
//                        }
//                    }
                }
            } else {//返回码不是200
                result = "error";
            }
        } catch (MalformedURLException e) {
            result = "netError";
        } catch (IOException e) {
            result = "netError";
        }
        return result;
    }
}
