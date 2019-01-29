package devmike.jade.com.jadesharedpreference

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import devmike.jade.com.annotations.read.ReadString
import devmike.jade.com.annotations.SharedPref
import devmike.jade.com.annotations.read.ReadFloat
import devmike.jade.com.annotations.read.ReadInt
import devmike.jade.com.binder.JadeSharedPreference
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    @ReadFloat("hk")
    var myVaue: Float =0.0f

    private lateinit var jsp :JadeSharedPreference

    @SharedPref("key")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        jsp =JadeSharedPreference.plug(this, this)

        jsp.insert("hk", 1.6)
       //hel.text = myVaue.toString()

    }
}
