package devmike.jade.com.binder

import android.content.Context
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.lang.NullPointerException
import java.lang.reflect.Method

public object JadeSharedPreference {

    private var bindingClassNewInstance: Any? =null
    private  var bindingClass: Class<*>? =null
    private var method: Method? =null

    fun <T : Any> preference(targetClass: T, context: Context) : JadeSharedPreference{

        val className ="${targetClass::class.java.`package`.name}.JSP_Preference${targetClass::class.simpleName}"

        // val className ="${targetClass::class.java.`package`.name}JSP_Preference${targetClass::class.simpleName}"

        try {

            //Wire up plug() method to generated SharedPreference file
            this.bindingClass = targetClass.javaClass.classLoader?.loadClass( className)
            method = bindingClass?.getMethod("plug", Context::class.java, Any::class.java)

            this.bindingClassNewInstance =bindingClass?.newInstance()
            method?.invoke(bindingClassNewInstance, context, targetClass)

        }catch (ite: Exception){
            if(ite is NoSuchMethodException) {
                throw NullPointerException("${method?.toString()} does not exist")
            }else if (ite is ClassNotFoundException){
                throw ClassNotFoundException("$className was not generated. Please fix the error and try again!")
            }
        }

        return this
    }

    //Access point for SharedPreference
    fun <T : Any> apply(targetClass: T, context: Context) : JadeSharedPreference{

        val className ="${targetClass::class.java.`package`.name}.JSP${targetClass::class.simpleName}"

       // val className ="${targetClass::class.java.`package`.name}JSP_Preference${targetClass::class.simpleName}"

        try {

            //Wire up plug() method to generated SharedPreference file
            this.bindingClass = targetClass.javaClass.classLoader?.loadClass( className)
            method = bindingClass?.getMethod("plug", Context::class.java, Any::class.java)

            this.bindingClassNewInstance =bindingClass?.newInstance()
            method?.invoke(bindingClassNewInstance, context, targetClass)

        }catch (ite: Exception){
            if(ite is NoSuchMethodException) {
                throw NullPointerException("${method?.toString()} does not exist")
            }else if (ite is ClassNotFoundException){
                throw ClassNotFoundException("$className was not generated. Please fix the error and try again!")
            }
        }

        return this
    }


    fun <T : Any> insert(key: String, value: T){
        try{
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
                    insertStringSetFunc?.invoke(this.bindingClassNewInstance, value.toMutableSet(), key)
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
            }catch (e: Exception){
            if (e is NoSuchMethodException){
                throw NoSuchMethodException("")
            }
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