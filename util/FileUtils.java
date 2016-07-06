package cn.bjsxt.youhuo.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import cn.bjsxt.youhuo.MyApplication;

/**
 * 操作文件的工具类
 */
public class FileUtils {
    private Context context;

    public FileUtils(Context context) {
        this.context = context;
    }

    /**
     * 判断该文件名是否存在内部存储中
     *
     * @param fileName 要判断文件名
     * @return 是否存在
     */
    public boolean isSaveFile(String fileName) {
        LogUtil.logE(LogUtil.INFO_TAG, fileName);
        File file = new File(context.getFilesDir(), fileName);
        return file.exists();
    }

    /**
     * 从内部存储中读取保存的Json数据
     *
     * @param fileName 要读取的文件名
     * @return
     */
    public String readCacheJson(String fileName) {
        String result = null;
        try {
            //1.拿到文件输出流
            FileInputStream fis = MyApplication.getInstance().openFileInput(fileName);
            //2.使用缓冲流读取
            InputStreamReader is = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(is);
            String line ;
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null){
                sb.append(line);
            }
            result = sb.toString().trim();
            br.close();
            is.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 保存Json文件到内部存储
     * @param json 要保存的内容
     * @param fileName 要保存的文件名
     */
    public void saveJsonFile(String json,String fileName){

        if (json == null || json.equals("") || fileName == null){
            return;
        }
        //写入
        try {
            FileOutputStream fos = MyApplication.getInstance().openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(json.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
