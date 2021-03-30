package service

//import okhttp3.Call
import okhttp3.ResponseBody
import pojo.Student2
import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import rx.Observable


//创通讯接口
interface GetRequest_Interface {
    //Retrofit将 Http请求 抽象成 Java接口：采用 注解 描述网络请求参数 和配置网络请求参数
    //这是Retrofit的中枢神经部分。这是你指定的请求结构，将需要匹配的API
    //@GET("judgeUserServlet")
    @GET("judgeUserServlet")
    fun getApp(@Query("userName") userName: String, @Query("passWord") passWord: String) : retrofit2.Call<List<Student2>>
    //如果已知具体返回的类型，则可以写具体的类型

    @GET("saveUserServlet")
    fun registResult(@Query("userName") userName: String, @Query("passWord") passWord: String) : retrofit2.Call<List<Student2>>
    // @GET注解的作用:采用Get方法发送网络请求

    //getCall() = 接收网络请求数据的方法


    // getCall() = 接收网络请求数据的方法
    // 其中返回类型为Call<*>，*是接收数据的类（即上面定义的Translation类）
    // 如果想直接获得Responsebody中的内容，可以定义网络请求返回值为Call<ResponseBody>

    @GET("modifyUserServlet")
    fun getModifyResult(@Query("username") username: String, @Query("password") password: String):Call<ResponseBody>

    @POST("AddStudentServlet")
    fun getUpdateResult(@FieldMap params: Map<*, *>?): Observable<ResponseBody?>?

}
