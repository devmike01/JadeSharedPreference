package devmike.jade.com.compiler

import com.google.auto.service.AutoService
import devmike.jade.com.annotations.SharedPref
import devmike.jade.com.annotations.read.*
import devmike.jade.com.compiler.interfaces.JadeProcessor
import devmike.jade.com.compiler.sharedprefcompiler.SharedPrefProcessorHelper
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements


@AutoService(JadeProcessor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions(SharedPrefProcessorHelper.KAPT_KOTLIN_GENERATED)
 open class AnnotationProcessor : AbstractProcessor() {
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
        SharedPrefProcessorHelper().process(processingEnv, annotations, roundEnv)
        return true
    }


    override fun getSupportedAnnotationTypes(): MutableSet<String?>  {

        return mutableSetOf(
            SharedPref::class.java.name,
            ReadFloat::class.java.name, ReadInt::class.java.name,
            ReadString::class.java.name,
            ReadLong::class.java.name,
            ReadStringSet::class.java.name)
    }


    open fun process(processingEnv: ProcessingEnvironment, annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment){}

}