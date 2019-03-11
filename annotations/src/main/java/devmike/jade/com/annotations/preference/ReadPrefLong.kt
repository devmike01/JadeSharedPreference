package devmike.jade.com.annotations.preference

@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class ReadPrefLong(val key: String,
                              val defaultValue: Long =0) {
}