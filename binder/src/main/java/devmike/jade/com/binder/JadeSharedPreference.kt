package devmike.jade.com.binder

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.util.Log
import devmike.jade.com.annotations.SharedPref
import java.lang.Error
import java.lang.IllegalArgumentException
import java.lang.NullPointerException
import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException

public object JadeSharedPreference {

    private var bindingClassNewInstance: Any? =null
    private  var bindingClass: Class<*>? =null

    /**
     * Retrieve the generated class here and try to call the entry function(plug(Context, Any))
     *  @targetClass is the class where we want to use the library
     *  @context is the context of the class where we are calling the library
     *
     *  We pass the required arguments into the generated class and invoke the functions
     *  required by the users
     */
    fun <T : Any> plug(targetClass: T, context: Context) : JadeSharedPreference{

        //val mTargetClassStr: String? =targetClass::class.simpleName
        val mPackage : String =targetClass::class.java.`package`.name
        val className: String ="$mPackage.JSP${targetClass::class.simpleName}"

        try {

            this.bindingClass = targetClass.javaClass.classLoader?.loadClass( className)
            val method = bindingClass?.getMethod("plug", Context::class.java, Any::class.java)

            this.bindingClassNewInstance =bindingClass?.newInstance()

            method?.invoke(bindingClassNewInstance, context, targetClass)

        }catch (cnf: ClassNotFoundException){
            throw NullPointerException("${targetClass::class.simpleName} was not setup properly. Please fix the error and ReBuild!")
        }

        return this
    }

    /**
     *
     * This function act as an intermediary between the developer and the JadeSharedPreference API
     *
     * @key is a String that represent the identifier of the value we're saving to the SharedPreference
     * @value is the value we're persisting in the SharedPreference
     */
    fun <T : Any> insert(key: String, value: T){
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


    }

    /**
     * Wipe out all the data persisted in the JadeSharedPreference
     */
    fun wipeOut(){
        val wipeFunc = bindingClass?.getMethod("clearSharedPref")
        wipeFunc?.invoke(this.bindingClassNewInstance)
    }

    /**
     * Call this function to remove a specific value from JadeSharedPreference
     *
     * @key is the key of the value we want to delete JadeSharedPreference
     */
    fun remove(key: String){
        val removeFunc = bindingClass?.getMethod("removeValue", String::class.java)
        removeFunc?.invoke(this.bindingClassNewInstance, key)
    }

}