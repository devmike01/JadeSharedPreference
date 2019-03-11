package devmike.jade.com.annotations.sharedpreference
@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class ReadStringSet (val key: String){
}