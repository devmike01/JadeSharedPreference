package devmike.jade.com.jadesharedpreference

import android.content.Context
import devmike.jade.com.annotations.SharedPref
import devmike.jade.com.annotations.read.ReadInt
import devmike.jade.com.annotations.read.ReadString
import devmike.jade.com.binder.JadeSharedPreference

 class TestClass(context: Context){

     var jsp: JadeSharedPreference = JadeSharedPreference.plug(this, context)

    val TEST_KEY: String ="test_key"

    @ReadInt("test")
    var testInt: Int =9


    @SharedPref("test0")
    fun init(context: Context){
    }


    public fun writeTest(value: Int){
        jsp.insert("test", 200)
    }

    public fun readTest(): Int{2
        return testInt
    }

}