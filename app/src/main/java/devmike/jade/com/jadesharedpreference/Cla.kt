package devmike.jade.com.jadesharedpreference

import android.content.Context
import devmike.jade.com.annotations.read.ReadString
import devmike.jade.com.binder.JadeSharedPreference

class Cla {

    @ReadString("key")
    lateinit var vals : String

    constructor(context: Context){

        JadeSharedPreference.plug(this, context)
    }

    //private lateinit var
}