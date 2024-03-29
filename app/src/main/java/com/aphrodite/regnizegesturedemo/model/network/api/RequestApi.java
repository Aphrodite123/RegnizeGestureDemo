package com.aphrodite.regnizegesturedemo.model.network.api;

import com.aphrodite.regnizegesturedemo.model.network.response.GestureResponse;
import com.aphrodite.regnizegesturedemo.model.network.response.PersonResponse;
import com.aphrodite.regnizegesturedemo.model.network.response.StudentResponse;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * <p>描述：通用的的api接口</p>
 * <p>
 * 1.加入基础API，减少Api冗余<br>
 * 2.支持多种方式访问网络（get,put,post,delete），包含了常用的情况<br>
 * 3.传统的Retrofit用法，服务器每增加一个接口，就要对应一个api，非常繁琐<br>
 * 4.如果返回ResponseBody在返回的结果中去获取T,又会报错，这是因为在运行过程中,通过泛型传入的类型T丢失了,所以无法转换,这叫做泛型擦除。
 * 《泛型擦除》不知道的童鞋自己百度哦！！<br>
 * </p>
 * <p>
 * 注意事项：
 * <br>
 * 1.使用@url,而不是@Path注解,后者放到方法体上,会强制先urlencode,然后与baseurl拼接,请求无法成功<br>
 * 2.注意事项： map不能为null,否则该请求不会执行,但可以size为空.
 * <br>
 * </p>
 * Created by Aphrodite on 2019/4/23.
 */
public interface RequestApi {
    @Multipart      //文件上传类型头
    @POST("jqd/study/gesture")
    Observable<GestureResponse> queryGesture(@Part List<MultipartBody.Part> partLis);

    @GET("ServletDemo_war_exploded/QueryPersonServlet")
    Observable<PersonResponse> queryPerson();

    @GET("ServletDemo_war_exploded/QueryPersonServlet")
    Observable<StudentResponse> queryStudent();
}
