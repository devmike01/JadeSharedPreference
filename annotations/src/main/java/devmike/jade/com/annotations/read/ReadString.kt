package devmike.jade.com.annotations.read

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class ReadString(val key: String, val defaultValue: String ="") {
}