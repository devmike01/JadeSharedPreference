package devmike.jade.com.annotations.preference

@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class ReadLong(val key: String,
                                    val defaultValue: Long =0) {
}