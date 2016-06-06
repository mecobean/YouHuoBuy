package cn.bjsxt.youhuo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import cn.bjsxt.youhuo.R;
import cn.bjsxt.youhuo.adapter.AdapterMainGV;
import cn.bjsxt.youhuo.bean.MainHomeGVBean;

/**
 * Created by Mecono on 2016/6/3.
 */
public class MainHomeFragment extends BaseFragment {

    private View rootView;
    private GridView gv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main_home, null);
        initDatas();
        return rootView;
    }

    /**
     * 初始化所有的数据
     */
    private void initDatas() {
        initViews();
        initGV();

    }

    /**
     * 初始化gridView
     */
    private void initGV() {
        List<MainHomeGVBean> list = new ArrayList<>();
        String[] strGv = getResources().getStringArray(R.array.main_home_gv);
        List<Integer> listID = new ArrayList<>();
        listID.add(R.mipmap.btn_xpdz_n);
        listID.add(R.mipmap.btn_qqyx_n);
        listID.add(R.mipmap.btn_cptj);
        listID.add(R.mipmap.btn_qbpl_n);
        listID.add(R.mipmap.btn_dpzn_n);
        listID.add(R.mipmap.btn_qxsc_n);
        listID.add(R.mipmap.btn_mxcp_n);
        listID.add(R.mipmap.btn_zkjx_n);
        for (int i = 0; i < strGv.length; i++) {
            MainHomeGVBean bean = new MainHomeGVBean();
            bean.setStr(strGv[i]);
            bean.setImgID(listID.get(i));
            list.add(bean);
        }
        gv.setAdapter(new AdapterMainGV(getContext(),list));
    }
    /**
     * 初始化
     */
    private void initViews() {
        gv = (GridView) rootView.findViewById(R.id.gv_main_home);
    }
}
