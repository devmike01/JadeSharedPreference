package devmike.jade.com.jadesharedpreference

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import devmike.jade.com.annotations.SettingsPreference
import devmike.jade.com.annotations.SharedPref
import devmike.jade.com.annotations.sharedpreference.*
import devmike.jade.com.binder.JadeSharedPreference
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity constructor(): AppCompatActivity() {


    private lateinit var jsp :JadeSharedPreference

    //Read string from JadeSharedPreference
    @ReadString("string")
    lateinit var mString: String

    //Read Integer from JadeSharedPreference
    @ReadInt("integer_anything")
    var mInteger: Int =0

    //Read Long from JadeSharedPreference
    @ReadLong("long_")
    var mLong: Long =0

    //Read Float from JadeSharedPreference
    @ReadFloat("float_key")
    var mFloat: Float =0f

    //Read StringSet from JadeSharedPreference
    @ReadStringSet("stringset__")
    lateinit var mStringSet: MutableSet<String>
    
    @ReadAll
    lateinit var mMapAll: Map<String, *>


    private val settingPref = PreferenceManager.getDefaultSharedPreferences(this)


    //@SharedPref("key")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        jsp =JadeSharedPreference.plug(this, this)


        read_1.text = mString
        read_2.text = mInteger.toString()
        read_3.text = mFloat.toString()
        read_4.text = mLong.toString()
        read_5.text = mStringSet.toString()

        save_btn1.setOnClickListener(this::btnTestOne)
        save_btn2.setOnClickListener(this::btnTestOne)
        save_btn3.setOnClickListener(this::btnTestOne)
        save_btn4.setOnClickListener(this::btnTestOne)
        save_btn5.setOnClickListener(this::btnTestOne)

       // val vClass =JSPTestClass()
        //vClass.insertValue("hello", "World Of Django")

        Log.d("MainActivity", mMapAll.toString())

    }

    fun btnTestOne(v: View){
        /**
         * Click to write to JadeSharedPreference
         */
        val ed: String = ed_1.text.toString()
        when (v) {
            save_btn1 -> jsp.insert("string", ed)
            save_btn2 -> jsp.insert("integer_anything", ed_2.text.toString().toInt())
            save_btn3 -> jsp.insert("float_key", ed_3.text.toString().toFloat())
            save_btn5 -> jsp.insert("stringset__", mutableSetOf(ed_5.text.toString()))
            save_btn4 -> jsp.insert("long_", ed_4.text.toString().toLong())
        }
    }


    //Listen to Float changes in realtime
    @ReadFloat("float_key")
    fun listenToFloatReadChanges(f: Float){
        read_3.text = f.toString()
    }

    //Listen to String changes in realtime
    @ReadString("string")
    fun readStringChanges(s: String){
        read_1.text = s
    }

    //Listen to Integer changes in realtime
    @ReadInt("integer_anything")
    fun listenToIntegerChanges(i: Int){
        read_2.text =i.toString()
    }

    //Listen to Long changes in realtime
    @ReadLong("long_")
    fun longChangeListener(l: Long){
        read_4.text = l.toString()
    }

    //Listen to StringSet changes in realtime
    @ReadStringSet("stringset__")
    fun stringSetChanges(set: MutableSet<String>){
        read_5.text = set.toString()
    }

    @ReadAll
    fun readAllItems(allItems: MutableMap<String, *>){
        Log.d("MainActivity", allItems.toString())
    }
}