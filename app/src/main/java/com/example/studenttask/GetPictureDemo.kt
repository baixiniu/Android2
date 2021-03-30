package com.example.studenttask

//import rx.Scheduler
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.*
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_get_picture_demo.*
import okhttp3.ResponseBody
import pojo.AndroidScheduler
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.Subscriber
import rx.functions.Func1
import rx.schedulers.Schedulers
import service.GetPictureApi
import java.io.File


class GetPictureDemo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_picture_demo)

        getPictureButton.setOnClickListener {
            var retrofit = Retrofit.Builder()
                .baseUrl("http://10.17.108.14:9100/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            var getPictureApi = retrofit.create(GetPictureApi::class.java);

            //返回了一个被观察者，由接口的定义可知
            var getPictureResult =
                getPictureApi.downloadPicture("images/error.jpg")
            /**
             * 在Java中，被观察者的创建是比较复杂的，
             * 大致为：
             * 1.调用Observable的create（）
             * 2.传入ObservableOnSubscribe对象，重写ObservableOnSubscribe对象的subscribe（）
             * 这个订阅方法主要用来做什么，主要用来把把被观察者（Observable）的事件发送出去
             *      那这个事件又是做什么勒，可以被观察者做出相应的动作，
             * 在rxjava当中，把被观察者的创建方式封装了，只需要在retrofit当中声明即可
             *
             * 异步方式
             * Java中对这部分的处理也较为复杂，大致步骤为
             * 1.被观察者调用observeOn();主要确定发射事件线程类型
             * 2.被观察者调用subscribeOn();处理事件线程是哪一个，同时需要处理具体的内容，如将下载好的内容翻译为图片
             * 3.被观察者调用subscribe()，主要确定观察者，参数一般为观察者实例，观察者主要是做一些不是太重要的事，如显示图片等
             *
             *在rxjava当中，也进行了封装，只需要重写上述三个方法
             *
             * */
            getPictureResult.subscribeOn(Schedulers.newThread())
                .map( Func1 { args0->
                    return@Func1 BitmapFactory.decodeStream(args0.byteStream())  //返回一个bitmap对象
                })
                .observeOn(AndroidScheduler.mainThread())//在Android主线程中展示
                .subscribe(object : Subscriber<Bitmap?>(){
                    override fun onCompleted() {
                        Log.d("Com","Com")
                    }

                    override fun onError(e: Throwable?) {
                        Log.d("Err","Err");
                        if (e != null) {
                            e.printStackTrace()
                        };
                    }

                    override fun onNext(t: Bitmap?) {
                        imageView.setImageBitmap(t);
                        //Log.d("Nex","Nex")
                    }

                })
        }
    }

}


