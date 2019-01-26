package devmike.jade.com.annotations.sharedprefs


@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class SharedInt(val value: Int) {
}