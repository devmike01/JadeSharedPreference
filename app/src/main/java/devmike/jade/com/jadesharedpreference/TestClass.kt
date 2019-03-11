package devmike.jade.com.jadesharedpreference

import android.content.Context
import devmike.jade.com.annotations.SettingsPreference
import devmike.jade.com.annotations.SharedPref
import devmike.jade.com.annotations.preference.ReadPrefString
import devmike.jade.com.annotations.sharedpreference.ReadString
import devmike.jade.com.binder.JadeSharedPreference

 class TestClass  constructor(context: Context){

     var jsp: JadeSharedPreference = JadeSharedPreference.plug(this, context)

     @ReadPrefString("string")
    var TEST_KEY: String ="test_key"

    fun init(context: Context){
    }


    public fun writeTest(value: Int){
        jsp.insert("test", 200)
    }


}