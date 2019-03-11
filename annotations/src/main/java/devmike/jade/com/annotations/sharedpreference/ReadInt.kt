package devmike.jade.com.annotations.sharedpreference

@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class ReadInt(val key: String,
                                   val defaultValue: Int =0) {
}