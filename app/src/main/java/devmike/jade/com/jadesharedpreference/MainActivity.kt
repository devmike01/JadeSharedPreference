package devmike.jade.com.jadesharedpreference

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import devmike.jade.com.annotations.SharedPref
import devmike.jade.com.annotations.read.*
import devmike.jade.com.binder.JadeSharedPreference
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity @SharedPref("key") constructor(): AppCompatActivity() {


    private lateinit var jsp :JadeSharedPreference

    //@SharedPref("key")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        jsp =JadeSharedPreference.plug(this, this)

        save_btn1.setOnClickListener(this::btnTestOne)
        save_btn2.setOnClickListener(this::btnTestOne)
        save_btn3.setOnClickListener(this::btnTestOne)
        save_btn4.setOnClickListener(this::btnTestOne)
        save_btn5.setOnClickListener(this::btnTestOne)

    }

    public fun btnTestOne(v: View){
        val ed: String = ed_1.text.toString()
        when (v) {
            save_btn1 -> jsp.insert("string", ed)git
            save_btn2 -> jsp.insert("integer_anything", ed_2.text.toString().toInt())
            save_btn3 -> jsp.insert("float_key", ed_3.text.toString().toFloat())
            save_btn5 -> jsp.insert("stringset__", mutableSetOf(ed_5.text.toString().split(",")))
            save_btn4 -> jsp.insert("long_", ed_4.text.toString().toLong())
        }
    }


    @ReadFloat("float_key")
    public fun listenToFloatReadChanges(f: Float){
        read_3.text = f.toString()
    }

    @ReadString("string")
    public fun readStringChanges(s: String){
        read_1.text = s
    }

    @ReadInt("integer_anything")
    public fun listenToIntegerChanges(i: Int){
        read_2.text =i.toString()
    }

    @ReadLong("long_")
    fun longChangeListener(l: Long){
        read_4.text = l.toString()
    }

    @ReadStringSet("stringset__")
    fun stringSetChanges(set: MutableSet<String>){
        read_5.text = set.toString()
    }
}