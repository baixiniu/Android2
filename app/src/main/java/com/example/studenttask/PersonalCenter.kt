package com.example.studenttask

import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import database.MyDatabaseHelper
import kotlinx.android.synthetic.main.activity_personal_center.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import service.GetRequest_Interface


class PersonalCenter : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_center)

        personHead.setImageResource(R.drawable.userdeafult);

        val sharedPreferences: SharedPreferences =this.getSharedPreferences("user", MODE_PRIVATE);
        val editor: SharedPreferences.Editor =  sharedPreferences.edit();
        //editor.commit();

        //val intent=intent;
        PersonName.setText(sharedPreferences.getString("username",""));
        PersonPassword.setText(sharedPreferences.getString("password",""));

        //设置返回按钮
        returnButton.setOnClickListener{
            val returnIntent=Intent(this, ListStudents2::class.java);
            this.startActivity(returnIntent);
            //this.finish();
        }

        modifyButton.setOnClickListener{
            val username:String=PersonName.text.toString();
            val password:String=PersonPassword.text.toString();
            if(username.equals(sharedPreferences.getString("username",""))&&password.equals(sharedPreferences.getString("password",""))){
                //这里打算使用Alertialog
                //Toast.makeText(this,"请确保修改后的信息和原有信息不一致",Toast.LENGTH_SHORT).show();
                var alertDialog:AlertDialog.Builder=AlertDialog. Builder(this);
                alertDialog.setTitle("修改错误");
                alertDialog.setMessage("请确保修改后的信息和原有信息不一致");
                alertDialog.setCancelable(true);
                alertDialog.setPositiveButton("确定",DialogInterface.OnClickListener{
                     dialog: DialogInterface?, which: Int ->
                        dialog?.dismiss();
                })
                alertDialog.setNegativeButton("返回",DialogInterface.OnClickListener{
                    dialog, which ->
                        dialog.dismiss();
                })

                alertDialog.show();
            }else{
                //对数据的修改一般而言一般有两种，一种是使用内部自带方法，不推荐
                //二是使用语句，推荐，具体百度吧，这里使用自带的失败了
                /*
                var dbhelper: MyDatabaseHelper =
                    MyDatabaseHelper(this, "userDB", null, 3);
                val db: SQLiteDatabase = dbhelper.getWritableDatabase()
                val values2 = ContentValues();
                values2.put("username", username);
                values2.put("password",password);
                val usernames:Array<String>;
                usernames= arrayOf(username);
                db.update("user",values2,"username=?",usernames);

                val columns:Array<String>;
                columns= arrayOf("username","password");

                //为了验证是否修改成功，写了一个查询
                var cursor:Cursor = db.query("user", columns,null,null,null,null,null,);
                //利用游标遍历所有数据对象
                //为了显示全部，把所有对象连接起来，放到TextView中
                var textview_data:String = "";
                while(cursor.moveToNext()) {
                        var name:String = cursor . getString (cursor.getColumnIndex("name"));
                        textview_data = textview_data + "\n" + name;
                    }
                Log.d("AAA",textview_data);*/

                val retrofit = Retrofit.Builder()
                    //.baseUrl("http://10.15.156.84:9100/")
                    .baseUrl("http://10.17.108.14:9100/")
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//设置适配器
                    .addConverterFactory(GsonConverterFactory.create())//设置数据解析器
                    .build()
                val myService = retrofit.create(GetRequest_Interface::class.java);

                val method=myService.getModifyResult(username,password);

                method.enqueue(object :Callback<ResponseBody>{
                    override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                        if (response != null) {
                            if(response.body().string().equals("fail"))
                                Toast.makeText(this@PersonalCenter,"修改错误",Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(this@PersonalCenter,"修改成功",Toast.LENGTH_SHORT).show();
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                        Toast.makeText(this@PersonalCenter,"网络出错",Toast.LENGTH_SHORT).show();
                    }
                })

                //editor.clear();

                editor.putString("username",username);
                editor.putString("password",password);
                editor.commit();

                }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        this.setIntent(intent)

        val sharedPreferences: SharedPreferences =this.getSharedPreferences("user", MODE_PRIVATE);
        val editor: SharedPreferences.Editor =  sharedPreferences.edit();
        editor.commit();

        //val intent=intent;
        PersonName.setText(sharedPreferences.getString("username",""));
        PersonPassword.setText(sharedPreferences.getString("password",""));
    }
}