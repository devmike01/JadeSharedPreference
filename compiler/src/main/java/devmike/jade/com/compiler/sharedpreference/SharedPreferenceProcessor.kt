package devmike.jade.com.compiler.sharedpreference

import com.google.auto.service.AutoService
import devmike.jade.com.compiler.AnnotationProcessor
import devmike.jade.com.compiler.NameStore
import javax.annotation.processing.*
import javax.lang.model.SourceVersion


@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions(NameStore.KAPT_KOTLIN_GENERATED)
class SharedPreferenceProcessor : AnnotationProcessor() {

    override fun prefName(): String {
        return NameStore.SUFFIX_SHAREDPREF_CLASSNAME
    }


}