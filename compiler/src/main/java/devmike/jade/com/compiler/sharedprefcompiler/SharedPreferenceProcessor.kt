package devmike.jade.com.compiler.sharedprefcompiler

import devmike.jade.com.compiler.AnnotationProcessor
import devmike.jade.com.compiler.factory.ProcessorFactory
import devmike.jade.com.compiler.interfaces.JadeProcessor
import devmike.jade.com.compiler.preferencecompiler.PreferenceProcessorHelper
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement

class SharedPreferenceProcessor: AnnotationProcessor(), JadeProcessor {

    override fun process(
        processingEnv: ProcessingEnvironment,
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment) {

        //Start processing the annotations
        val processor = ProcessorFactory.make(ProcessorFactory.Processors.SHAREDPREFERENCE)
        processor.process(processingEnv, annotations, roundEnv)
    }
}