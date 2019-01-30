package devmike.jade.com.annotations.read
@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class ReadStringSet (val key: String,
                                val defaultValue: Array<String> = []){
}