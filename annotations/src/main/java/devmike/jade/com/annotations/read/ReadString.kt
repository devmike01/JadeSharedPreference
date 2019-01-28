package devmike.jade.com.annotations.read

import android.support.annotation.IntDef
import android.support.annotation.IntegerRes


@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class ReadString(val key: String,
                            val defaultValue: String =""){
}