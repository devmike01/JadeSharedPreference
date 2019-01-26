package devmike.jade.com.annotations.sharedprefs

import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class SharedPref(val name: String= "devmike.jade.com_JadeKnife") {
}