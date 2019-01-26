package devmike.jade.com.annotations.sharedprefs


@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class SharedFloat(val value: Float) {
}