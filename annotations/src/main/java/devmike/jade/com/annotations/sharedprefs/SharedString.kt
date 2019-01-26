package devmike.jade.com.annotations.sharedprefs

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class SharedString(val key: String, val value: String, val defaultValue: String) {
}