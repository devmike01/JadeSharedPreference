package devmike.jade.com.jadesharedpreference

import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import devmike.jade.com.annotations.read.ReadLong
import devmike.jade.com.annotations.read.ReadString
import devmike.jade.com.annotations.sharedprefs.SharedPref
import devmike.jade.com.annotations.sharedprefs.SharedString
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    @ReadString("key","dsd")
    lateinit var myVaue: String


    @ReadLong("DD", 6)
    var ff: Long =0

    lateinit var jsp :JSPMainActivity


    @SharedPref("key")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        jsp = JSPMainActivity.plug(this, this)

        jsp.insertValue("Hello World222", "key")

        hel.text = myVaue

    }
}
