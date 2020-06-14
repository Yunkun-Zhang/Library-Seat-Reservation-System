package com.example.librarymanagement

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.librarymanagement.extension.StartConversation
import com.example.librarymanagement.ui.activity.*
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.ml.scan.HmsScan
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    //扫码需要参数
    private val REQUEST_CODE_SCAN = 0X01
    val DEFAULT_VIEW = 0x22

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) //鼠标点在紫色字体上ctrl+B可以进入xml设置

        // 获取userID
        var userID = intent.getIntExtra("userID", 0)
        var seat = intent.getIntExtra("seat", -1)
        if (seat != -1) {
            var alertDialog = AlertDialog.Builder(this)
            alertDialog.setMessage("预订$seat 座位成功！")
            alertDialog.setNeutralButton("确定", null)
            alertDialog.show()
        }

        // 界面的一些显示更改
        if (userID != 0) {
            login_state.text = "空闲"
            log_or_sign.visibility = View.GONE
            logout.visibility = View.VISIBLE
        }
        else {
            login_state.text = "未登录"
            log_or_sign.visibility = View.VISIBLE
            logout.visibility = View.GONE
        }

        main_to_login.setOnClickListener {
            Intent(this, Login::class.java).apply {
                startActivity(this)
            }
        }

        main_to_signup.setOnClickListener {
            Intent(this, Signup::class.java).apply {
                startActivity(this)
            }
        }

        main_to_book.setOnClickListener {
            // 先判断是否登录
            if (login_state.text.toString() == "未登录") {
                Intent(this, Login::class.java).apply {
                    startActivity(this)
                }
            }
            else {
                Intent(this, Book::class.java).apply {
                    putExtra("userID", userID)
                    startActivity(this)
                }
            }
        }

        logout.setOnClickListener {
            Intent(this, MainActivity::class.java).apply {
                startActivity(this)
            }
        }

        //融云
        conversationlist.setOnClickListener {
            Intent(this, ConversationListActivity::class.java).apply {
                startActivity(this)
            }
        }

        //创建新对话
        conversation.setOnClickListener {
            Intent(this, ConversationActivity::class.java).apply {
                StartConversation().startConversation("1234",this@MainActivity)
            }
        }

    }

    /**
     * 扫码实现开始
     */
    fun newViewBtnClick(view: View?) {
        //DEFAULT_VIEW为用户自定义用于接收权限校验结果
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                DEFAULT_VIEW
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (permissions == null || grantResults == null || grantResults.size < 2 || grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        if (requestCode == DEFAULT_VIEW) {
            //调用DefaultView扫码界面
            ScanUtil.startScan(
                this@MainActivity,
                REQUEST_CODE_SCAN,
                HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.ALL_SCAN_TYPE).create()
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //当扫码页面结束后，处理扫码结果
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK || data == null) {
            return
        }
        //从onActivityResult返回data中，用 ScanUtil.RESULT作为key值取到HmsScan返回值
        if (requestCode == REQUEST_CODE_SCAN) {
            val obj: Any = data.getParcelableExtra(ScanUtil.RESULT)
            if (obj is HmsScan) {
                if (!TextUtils.isEmpty(obj.getOriginalValue())) {
                    //obj.getOriginalValue()就是扫码出来的信息，类型为String
                    //这行代码是扫码后的提示框，可以考虑删掉
                    Toast.makeText(this, obj.getOriginalValue(), Toast.LENGTH_SHORT).show()
                    //这个是在logcat显示相关信息的，可以删掉
                    Log.i("resultscan",obj.getOriginalValue())
                }
                return
            }
        }

    }
    /**扫码实现结束*/

}

