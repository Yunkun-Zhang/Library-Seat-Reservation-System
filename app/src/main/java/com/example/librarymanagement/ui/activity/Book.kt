package com.example.librarymanagement.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.librarymanagement.MainActivity
import com.example.librarymanagement.R
import com.example.librarymanagement.control.OrderControl
import com.stormkid.okhttpkt.core.Okkt
import com.stormkid.okhttpkt.rule.CallbackRule
import kotlinx.android.synthetic.main.activity_book.*
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onFocusChange


class Book : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)

        // 进入该页面前一定处于登录状态
        var userID = intent.getIntExtra("userID", 0)
        var seatID = intent.getIntExtra("seatID", 0)
        val orderID = intent.getIntExtra("orderID", 0)
        var start = intent.getIntExtra("start", 8)
        var end = intent.getIntExtra("end", 23)

        start_time.text = Editable.Factory.getInstance().newEditable(start.toString())
        end_time.text = Editable.Factory.getInstance().newEditable(end.toString())
        if (seatID != 0) seat.text = seatID.toString()

        if (orderID != 0) {
            // 获取订单信息，如start=13,end=18
            start_time.text = Editable.Factory.getInstance().newEditable(start.toString())
            end_time.text = Editable.Factory.getInstance().newEditable(end.toString())
            start_time.isEnabled = false
            end_time.isEnabled = false
            choose_seat.isEnabled = false
            subject.isEnabled = false
            gender.isEnabled = false
            cancel.isEnabled = true
            cancel.setOnClickListener {
                // 加入取消订单操作*****************************
                Intent(this, MainActivity::class.java).apply {
                    putExtra("orderID", 0)
                    putExtra("userID", userID)
                    startActivity(this)
                }
            }
        }
        else {
            // 取消=返回
            cancel.setOnClickListener { finish() }
            // 下一步：选座
            confirm.setOnClickListener {
                start = start_time.text.toString().toInt()
                end = end_time.text.toString().toInt()
                // 8:00 -- 23:00
                if (start < 8 || start > 23 || end < 8 || end > 23) {
                    val alertDialog = AlertDialog.Builder(this)
                    alertDialog.setMessage("请输入正确的时间！")
                    alertDialog.setNeutralButton("确定", null)
                    alertDialog.show()
                }
                else if (start >= end) {
                    val alertDialog = AlertDialog.Builder(this)
                    alertDialog.setMessage("至少预订1个小时！")
                    alertDialog.setNeutralButton("确定", null)
                    alertDialog.show()
                }
                else {
                    val sub = subject.text.toString()
                    val g: Boolean?

                    if (gender.selectedItem == "男") g = true
                    else if (gender.selectedItem == "女") g = false
                    else g = null

                    Okkt.instance.Builder().setUrl("/seat/getspareseats/tomorrow")
                        .putBody(hashMapOf("starttime" to start.toString(), "endtime" to end.toString()))
                        .post(object : CallbackRule<MutableList<Int>> {
                            override suspend fun onFailed(error: String) {}
                            override suspend fun onSuccess(entity: MutableList<Int>, flag: String) {
                                Intent(this@Book, SeatInfoActivity::class.java).apply {
                                    //putExtra("start", start)
                                    //putExtra("end", end)
                                    putExtra("userID", userID)
                                    putExtra("subject", sub)
                                    putExtra("gender", g)
                                    putExtra("list", entity.toIntArray())
                                    startActivity(this)
                                }
                            }
                        })


                }
            }

        }

        // 取消
        book_back.setOnClickListener { finish() }


    }

}