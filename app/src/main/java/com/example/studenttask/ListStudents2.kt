package com.example.studenttask

import adapter.StudentsAdapter2
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
//import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_list_students2.*
import kotlinx.android.synthetic.main.title.*
import pojo.Student2
import java.io.FileInputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream


class ListStudents2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

        setContentView(R.layout.activity_list_students2)

        //这是以前使用intent的
        var intent: Intent =getIntent();
        //Log.d("intent",intent.toString())
        //if(intent.getSerializableExtra("studentList").toString().length==0)
        var test:ArrayList<Student2>;
        test=intent.getSerializableExtra("studentsList") as ArrayList<Student2>;
            Log.d("A", " AAA" + test.size);

        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);

        /*val toolbar: Toolbar = this.findViewById(R.id.toolbar) as Toolbar
        toolbar.setTitle("aaaaa")
        setSupportActionBar(toolbar)*/

        spinner1.onItemSelectedListener = object : OnItemSelectedListener {
            //parent就是父控件spinner
            //view就是spinner内填充的textview,id=@android:id/text1
            //position是值所在数组的位置
            //id是值所在行的位置，一般来说与positin一致
            override fun onItemSelected(
                parent: AdapterView<*>, view: View,
                pos: Int, id: Long
            ) {
                if(pos==1){
                    val intent2=Intent(this@ListStudents2, PersonalCenter::class.java);
                    /*intent2.putExtra("username",intent.getStringExtra("username"));
                    intent2.putExtra("password",intent.getStringExtra("password"));*/
                    this@ListStudents2.startActivity(intent2);
                }else if(pos==2)
                {
                    //Toast.makeText(this@ListStudents2,"B",Toast.LENGTH_SHORT).show();
                    val returnIntent=Intent(this@ListStudents2, Regist::class.java);
                    this@ListStudents2.startActivity(returnIntent);
                }
                else if(pos==3){
                    val fragmentIntent= Intent(this@ListStudents2, FragmentStaticDemo::class.java);
                    this@ListStudents2.startActivity(fragmentIntent);
                }
                else if(pos==4){
                    val imageIntent=Intent(this@ListStudents2, ImageDemo::class.java);
                    this@ListStudents2.startActivity(imageIntent);
                }
                else if(pos==5){
                    val downloadIntent=Intent(this@ListStudents2, GetPictureDemo::class.java);
                    this@ListStudents2.startActivity(downloadIntent);
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Another interface callback
            }
        }


        if(intent==null){
            //读取文件
            val stream: FileInputStream = openFileInput("studentList.txt")
            val ois = ObjectInputStream(stream)
            var studentList:List<Student2> = ois.readObject() as List<Student2>
            Log.d("students", studentList.toString());
            var linearLayoutManager = LinearLayoutManager(this);
            listStudents2.layoutManager=linearLayoutManager;
            var adapter= StudentsAdapter2(this, studentList);

            listStudents2.adapter=adapter;
        }
        else{
            var students:ArrayList<Student2>;
            students= intent.getSerializableExtra("studentsList") as ArrayList<Student2>;
            //Log.d("s",students.toString())
            var linearLayoutManager = LinearLayoutManager(this);
            listStudents2.layoutManager=linearLayoutManager;
            var adapter= StudentsAdapter2(this, students);

            listStudents2.adapter=adapter;

            val stream = openFileOutput("studentList.txt", MODE_PRIVATE);
            val oos = ObjectOutputStream(stream);
            oos.writeObject(students);
            //写入数据
        }

        //Log.d("Students",""+students.size);


    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        this.setIntent(intent)

        spinner1.setSelection(0,true)
        //设置默认值
    }
}

