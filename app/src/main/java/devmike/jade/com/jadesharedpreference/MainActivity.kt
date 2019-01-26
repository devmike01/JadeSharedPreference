package devmike.jade.com.jadesharedpreference

import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import devmike.jade.com.annotations.read.ReadString
import devmike.jade.com.annotations.sharedprefs.SharedPref
import devmike.jade.com.annotations.sharedprefs.SharedString
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    @SharedPref("name")
    lateinit var sharedPref: SharedPreferences

    @SharedString("key", "MY VALUE", "")
    lateinit var myKey: String

    @ReadString("key")
    lateinit var myVaue: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        JadeSharedPreference_MainActivity(this, this)
        hel.text = myVaue

    }
}
