package devmike.jade.com.compiler.preferencecompiler

import devmike.jade.com.compiler.interfaces.JadeProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement

class PreferenceProcessorHelper : JadeProcessor {
    override fun process(
        processingEnv: ProcessingEnvironment,
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ) {

    }
}