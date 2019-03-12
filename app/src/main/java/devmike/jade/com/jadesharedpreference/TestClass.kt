package devmike.jade.com.jadesharedpreference

import android.content.Context
import devmike.jade.com.annotations.SettingsPreference
import devmike.jade.com.annotations.SharedPref
import devmike.jade.com.annotations.preference.ReadPrefString
import devmike.jade.com.binder.JadeSharedPreference
import java.util.concurrent.CountDownLatch

open class TestClass @SettingsPreference @SharedPref constructor(val context: Context){

     lateinit var jsp: JadeSharedPreference

     val countDown = CountDownLatch(1)

     @ReadPrefString("string")
    lateinit var mString: String

    fun init(){
        jsp =JadeSharedPreference.preference(this, context)
    }


    public fun writeTest(value: String){
        jsp.insert("test", value)
        //countDown.await()
    }

     @ReadPrefString("string")
     fun listenToStringChanges(value: String){
        // countDown.countDown()
     }

    public fun getString(): String{
        return mString
    }

}