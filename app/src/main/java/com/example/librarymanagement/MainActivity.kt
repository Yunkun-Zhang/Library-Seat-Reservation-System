package com.example.librarymanagement

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.librarymanagement.Application.MyApplication
import com.example.librarymanagement.extension.DateUtil
import com.example.librarymanagement.model.User
import com.example.librarymanagement.ui.activity.RoomTest
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_room_test.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) //鼠标点在紫色字体上ctrl+B可以进入xml设置
        index.setOnClickListener {
            //val intent = Intent(this, RoomTest::class.java)
            //startActivity(intent)
            Intent(this, RoomTest::class.java).apply {
                // 传递参数
                //putExtra("bar", "Peter")
                //putExtra("info", 33)

                //传递一个整体(类）参数
                putExtra("user", User())
                // 设置跳转
                startActivity(this)
                // startActivityForResult()    可传回数据
            }
        }
        btn_object_date.setOnClickListener {
            //以下方法调用自DateUtil.kt，采取了单例对象的方式
            btn_object_date.text = "单例对象： 当前日期时间为${DateUtil.nowDateTime}"

        }
    }
}

