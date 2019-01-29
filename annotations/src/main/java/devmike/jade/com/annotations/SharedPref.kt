package devmike.jade.com.annotations

import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CONSTRUCTOR)
@Retention(AnnotationRetention.SOURCE)
annotation class SharedPref(val name: String= "devmike.jade.com_JadeKnife") {
}