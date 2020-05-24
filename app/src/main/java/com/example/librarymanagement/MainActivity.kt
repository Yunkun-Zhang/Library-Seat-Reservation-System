package com.example.librarymanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.librarymanagement.adapter.StudentDao
import com.example.librarymanagement.database.AppDataBase
import com.example.librarymanagement.extension.DateUtil
import com.example.librarymanagement.model.User
import com.example.librarymanagement.ui.activity.Login
import com.example.librarymanagement.ui.activity.RoomTest
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) //鼠标点在紫色字体上ctrl+B可以进入xml设置
        index.setOnClickListener {
            //val intent = Intent(this, RoomTest::class.java)
            //startActivity(intent)
            Intent(this, RoomTest::class.java).apply {
                // 传递参数
                putExtra("bar", "Peter")
                putExtra("info", 33)

                //传递一个整体(类）参数
                // putExtra("user", User())
                // 设置跳转
                startActivity(this)
                // startActivityForResult()    可传回数据
            }
        }

        /*to_login.setOnClickListener {
            Intent(this, RoomTest::class.java).apply {
                putExtra("bar", "Peter")
                putExtra("info", 33)
                startActivity(this)
            }
        }*/

        val oDao = AppDataBase.instance.getOrderDao()
        var x = oDao.getOrderByGender(male = true)
        var y = oDao.getOrderByTimePeriod(6,9)

        btn_object_date.setOnClickListener {
            btn_object_date.text = "当前数据${y}, \n 当前日期时间为${DateUtil.nowDateTime}"
        }
    }
}

