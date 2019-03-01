package devmike.jade.com.compiler.preferencecompiler

import devmike.jade.com.compiler.AnnotationProcessor
import devmike.jade.com.compiler.factory.ProcessorFactory
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement

class SettingsAnnotationProcessor : AnnotationProcessor() {
    override fun process(
        processingEnv: ProcessingEnvironment,
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment) {

        //Start processing the annotations
        val processor = ProcessorFactory.make(ProcessorFactory.Processors.PREFERENCE)
        processor.process(processingEnv, annotations, roundEnv)
    }

}