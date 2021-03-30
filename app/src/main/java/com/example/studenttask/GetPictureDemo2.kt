package com.example.studenttask

import android.app.ProgressDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_get_picture_demo2.*
import pojo.AndroidScheduler
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.Subscriber
import rx.functions.Func1
import rx.schedulers.Schedulers
import service.GetPictureAPIWithJava
import java.io.File


class GetPictureDemo2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_picture_demo2)

        getPictureButton.setOnClickListener{
            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl("https://www.baidu.com/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) //添加Rxjava
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val serviceApi: GetPictureAPIWithJava = retrofit.create<GetPictureAPIWithJava>(
                GetPictureAPIWithJava::class.java
            )

            serviceApi.downloadPicFromNet("http://pic41.nipic.com/20140509/4746986_145156378323_2.jpg")
                .subscribeOn(Schedulers.newThread()) //在新线程中实现该方法
                .map(Func1  { arg0 ->
                        return@Func1 BitmapFactory.decodeStream(arg0.byteStream()) //返回一个bitmap对象
                })
                .observeOn(AndroidScheduler.mainThread()) //在Android主线程中展示
                .subscribe(object : Subscriber<Bitmap?>() {
                    var dialog: ProgressDialog = ProgressDialog(this@GetPictureDemo2)
                    override fun onStart() {
                        dialog.show()
                        super.onStart()
                    }

                    override fun onCompleted() {
                        dialog.dismiss()
                    }

                    override fun onError(arg0: Throwable) {
                        Log.d("TAG", "onError ===== $arg0")
                    }


                    override fun onNext(t: Bitmap?) {

                    }
                })
        }
    }
}