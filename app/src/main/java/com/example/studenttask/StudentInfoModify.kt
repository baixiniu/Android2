package com.example.studenttask

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_get_picture_demo.*
import kotlinx.android.synthetic.main.activity_student_info_modify.*
import okhttp3.ResponseBody
import pojo.AndroidScheduler
import pojo.Student2
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.Observable
import rx.Subscriber
import rx.functions.Func1
import rx.schedulers.Schedulers
import service.GetRequest_Interface

class StudentInfoModify : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_info_modify)

        var intentFromListStudents2=intent;
        var student:Student2=intentFromListStudents2.getSerializableExtra("student") as Student2;

        stuname.setText(student.stuName);
        stuNo.setText(student.stuNo);
        stumajor.setText(student.major);
        stutel.setText(student.tel);
        stuclass.setText(student.classes);
        stugender.setText(student.gender);
        studoor.setText(student.dormNo);
        if(student.gender.equals("男")){
            stuHead.setImageResource(R.drawable.boy);
        }else{
            stuHead.setImageResource(R.drawable.girl);
        }

        modifyReturn.setOnClickListener{
            val toListStudents2Intent=Intent(this,ListStudents2::class.java);
            this.startActivity(toListStudents2Intent);
        }

        var postInfo:HashMap<String,String>;
        postInfo= HashMap();
        postInfo.put("stuno",student.stuNo.toString());
        postInfo.put("stuname",student.stuName.toString());
        postInfo.put("classes",student.classes.toString());
        postInfo.put("gender",student.gender.toString());
        postInfo.put("major",student.major.toString());
        postInfo.put("tel",student.tel.toString());
        postInfo.put("door",student.dormNo.toString());
        postInfo.put("photopath",student.photoPath.toString());

        val retrofit = Retrofit.Builder()
            //.baseUrl("http://10.15.156.84:9100/")
            .baseUrl("http://10.17.108.14:9100/")
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//设置适配器
            .addConverterFactory(GsonConverterFactory.create())//设置数据解析器
            .build()
        val myService = retrofit.create(GetRequest_Interface::class.java);

        studentModify.setOnClickListener{
            var observable=myService.getUpdateResult(postInfo);
            if (observable != null) {
                observable.subscribeOn(Schedulers.newThread())
                    .map(Func1 { args0->
                        return@Func1 args0;
                    })
                    .observeOn(AndroidScheduler.mainThread())
                    .subscribe(object : Subscriber<ResponseBody?>(){
                        override fun onCompleted() {

                        }

                        override fun onError(e: Throwable?) {

                        }

                        override fun onNext(t: ResponseBody?) {
                            Toast.makeText(this@StudentInfoModify,t.toString(),Toast.LENGTH_SHORT).show();
                        }

                    })
            }
        }
    }
}