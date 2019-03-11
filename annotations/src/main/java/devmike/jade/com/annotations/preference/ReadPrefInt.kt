package devmike.jade.com.annotations.preference

@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class ReadPrefInt(val key: String,
                             val defaultValue: Int =0) {
}