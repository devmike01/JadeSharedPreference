package devmike.jade.com.annotations.sharedpreference

@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class ReadLong(val key: String,
                                    val defaultValue: Long =0) {
}