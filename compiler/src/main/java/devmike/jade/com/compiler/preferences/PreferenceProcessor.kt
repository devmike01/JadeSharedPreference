package devmike.jade.com.compiler.preferences

import com.google.auto.service.AutoService
import devmike.jade.com.compiler.AnnotationProcessor
import devmike.jade.com.compiler.FileGenerator.TypeElement.getTypeElementsToProcess
import devmike.jade.com.compiler.NameStore
import javax.annotation.processing.Processor
import javax.annotation.processing.SupportedOptions
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions(NameStore.KAPT_KOTLIN_GENERATED)

/**
 * Processor to generating Preference code
 */
class PreferenceProcessor : AnnotationProcessor() {
    override fun prefName(): String {
        return NameStore.SUFFIX_PREFERENCE_CLASSNAME
    }


}