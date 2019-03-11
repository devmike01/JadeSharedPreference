package devmike.jade.com.annotations.preference

@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class ReadPrefString(val key: String,
                                val defaultValue: String =""){
}