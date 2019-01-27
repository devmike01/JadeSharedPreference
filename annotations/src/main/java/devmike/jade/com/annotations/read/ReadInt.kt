package devmike.jade.com.annotations.read

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class ReadInt(val key: String,
                                   val defaultValue: Int =0) {
}