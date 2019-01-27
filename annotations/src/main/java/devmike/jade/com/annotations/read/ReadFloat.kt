package devmike.jade.com.annotations.read

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class ReadFloat(val key: String, val defaultValue: Float =0f) {
}