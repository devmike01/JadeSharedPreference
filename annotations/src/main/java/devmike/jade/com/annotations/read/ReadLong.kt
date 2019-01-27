package devmike.jade.com.annotations.read


@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class ReadLong(val key: String,
                                    val defaultValue: Long =0) {
}