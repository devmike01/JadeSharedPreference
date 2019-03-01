package devmike.jade.com.jadesharedpreference

import android.content.Context
import devmike.jade.com.annotations.SharedPref
import devmike.jade.com.annotations.read.ReadInt
import devmike.jade.com.annotations.read.ReadString
import devmike.jade.com.binder.JadeSharedPreference

 class TestClass constructor(var context: Context){

     //@ReadInt("hee")
     //val goal: Int? =null

     var jsp: JadeSharedPreference = JadeSharedPreference.plug(this, context)

    val TEST_KEY: String ="test_key"

    fun init(){

    }


    public fun writeTest(value: Int){
        jsp.insert("test", 200)
    }


}