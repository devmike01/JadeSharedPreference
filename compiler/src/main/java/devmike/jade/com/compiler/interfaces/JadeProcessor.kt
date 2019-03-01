package devmike.jade.com.compiler.interfaces

import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement

interface JadeProcessor {

    fun process(
        processingEnv: ProcessingEnvironment, annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    )
}
