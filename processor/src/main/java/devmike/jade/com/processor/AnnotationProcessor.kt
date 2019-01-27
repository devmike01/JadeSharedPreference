package devmike.jade.com.processor

import com.google.auto.service.AutoService
import devmike.jade.com.annotations.read.*
import devmike.jade.com.annotations.sharedprefs.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements


@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions(ProcessorHelper.KAPT_KOTLIN_GENERATED)

class AnnotationProcessor : AbstractProcessor() {
    private lateinit var filer: Filer
    private lateinit var messager: Messager
    private lateinit var elementUtil: Elements

    override fun init (processingEnv: ProcessingEnvironment){
        super.init(processingEnv)
        this.filer = processingEnv.filer
        this.messager = processingEnv.messager
        this.elementUtil = processingEnv.elementUtils
    }

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        ProcessorHelper.process(processingEnv, annotations, roundEnv)
        return true
    }


    override fun getSupportedAnnotationTypes(): MutableSet<String?>  {

        //_____
        return mutableSetOf(SharedPref::class.java.name,
            ReadFloat::class.java.name, ReadInt::class.java.name, ReadString::class.java.name,
            ReadLong::class.java.name, ReadStringSet::class.java.name)
    }



}