package devmike.jade.com.annotations.sharedpreference

@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class ReadFloat(val key: String, val defaultValue: Float =0f) {
}