package devmike.jade.com.annotations.read

@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class ReadBoolean(val key: String, val defaultValue: Boolean =false) {
}