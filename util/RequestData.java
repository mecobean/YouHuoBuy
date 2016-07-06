package cn.bjsxt.youhuo.util;

import cn.bjsxt.youhuo.bean.BaseBean;
import cn.bjsxt.youhuo.bean.CartListInfoBean;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 *
 */
public interface RequestData {
    @FormUrlEncoded
    @POST("{path}")
     Call<CartListInfoBean> requestData(@Path("path")String path, @Field("parames")String parames,@Body Class t );
}
