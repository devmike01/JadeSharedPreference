package devmike.jade.com.jadesharedpreference

import android.content.Context
import android.util.Log
import devmike.jade.com.annotations.Preference
import devmike.jade.com.annotations.preference.ReadPrefString
import devmike.jade.com.binder.JadeSharedPreference
import java.util.concurrent.CountDownLatch

class TestClass @Preference constructor(val context: Context){

     var jsp: JadeSharedPreference = JadeSharedPreference.preference(this, context)

    private val TAG = "TestClass"

     @ReadPrefString("string")
    lateinit var mString: String


    fun writeTest(value: String){

        jsp.insert("string", value)
    }

     @ReadPrefString("string")
     fun listenToStringChanges(value: String){
         Log.d(TAG, "VALUE: $value")
     }

    public fun getString(): String{
        return mString
    }

}