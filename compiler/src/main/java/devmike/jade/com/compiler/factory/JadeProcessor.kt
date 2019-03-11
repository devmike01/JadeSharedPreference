package devmike.jade.com.compiler.factory

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeSpec
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement
import kotlin.reflect.KClass

interface JadeProcessor {

    fun buildAccess(genericClass: ClassName, classBuilder: TypeSpec.Builder)
}