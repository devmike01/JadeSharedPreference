package devmike.jade.com.annotations.preference

@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class ReadPrefFloat(val key: String, val defaultValue: Float =0f) {
}