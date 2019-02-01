package devmike.jade.com.binder

import android.app.Activity
import android.content.Context
import android.util.Log
import java.lang.Error
import java.lang.IllegalArgumentException
import java.lang.NullPointerException
import java.lang.reflect.InvocationTargetException

public object JadeSharedPreference {

    private var bindingClassNewInstance: Any? =null
    private  var bindingClass: Class<*>? =null

    fun <T : Any> plug(targetClas: T, context: Context) : JadeSharedPreference{

        this.bindingClass = targetClas.javaClass.classLoader?.loadClass( "${targetClas::class.java.`package`.name}.JSP"
                + targetClas::class.simpleName)
        val method = bindingClass?.getMethod("plug", Context::class.java, Any::class.java)

        this.bindingClassNewInstance =bindingClass?.newInstance()
        try {
            method?.invoke(bindingClassNewInstance, context, targetClas)

        }catch (uip: UninitializedPropertyAccessException){
            throw NullPointerException("@SharedPref() is missing in your constructor")
        }

        return this
    }

    fun <T : Any> insert(key: String, value: T){
        Log.d("TAGGGG", value::class.simpleName)
        if (value is String) {
            val insertStringFunc = bindingClass?.getMethod("insertValue", String::class.java,
                String::class.java)
            insertStringFunc?.invoke(this.bindingClassNewInstance, value, key)
        }else if (value is Int) {
            val insertIntFunc = bindingClass?.getMethod("insertValue", Int::class.java, String::class.java)
            insertIntFunc?.invoke(this.bindingClassNewInstance, value, key)
        }else if(value is LinkedHashSet<*>) {
            if (!value.isEmpty()){
                if (value.elementAt(0) is String){
                    val insertStringSetFunc = bindingClass?.getMethod("insertValue", Set::class.java, String::class.java)
                    insertStringSetFunc?.invoke(this.bindingClassNewInstance, value, key)
                    return
                }

                throw  IllegalArgumentException("JadeSharedPreference only support string set!")
            }

         }else if (value is Float) {
            val insertFloatFunc = bindingClass?.getMethod("insertValue", Float::class.java, String::class.java)
            insertFloatFunc?.invoke(this.bindingClassNewInstance, value.toFloat(), key)
        }else if (value is Long) {
            val insertLongFunc = bindingClass?.getMethod("insertValue", Long::class.java, String::class.java)
            insertLongFunc?.invoke(this.bindingClassNewInstance, value, key)
        }else{
            throw IllegalArgumentException("Unsupported value type $value")
        }


    }

    fun wipeOut(){
        val wipeFunc = bindingClass?.getMethod("clearSharedPref")
        wipeFunc?.invoke(this.bindingClassNewInstance)
    }

    fun remove(key: String){
        val removeFunc = bindingClass?.getMethod("removeValue", String::class.java)
        removeFunc?.invoke(this.bindingClassNewInstance, key)
    }

}