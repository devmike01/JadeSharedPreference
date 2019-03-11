package devmike.jade.com.compiler

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import java.io.File
import java.util.HashSet
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

/**
 * This writes the generated class to the developer project directory.
 */
object FileGenerator {

    private lateinit var fileSpecBuilder: FileSpec.Builder

    private lateinit var file: File

    fun builder(packageName: String, name: String): FileGenerator{
        fileSpecBuilder = FileSpec.builder(packageName, name)
        return this
    }

    fun addType(typeSpec: TypeSpec): FileGenerator{
        fileSpecBuilder.addType(typeSpec)
        return this
    }

    fun addFile(file :File): FileGenerator{
        this.file = file
        return this
    }

    fun build(){
        fileSpecBuilder.build()
            .writeTo(file)
    }


    object TypeElement{
        public fun getTypeElementsToProcess(elements: Set<Element>, supportedAnnotations: Set<Element>): Set<javax.lang.model.element.TypeElement> {
            val typeElements = HashSet<javax.lang.model.element.TypeElement>()
            for (element in elements) {
                if (element is javax.lang.model.element.TypeElement) {
                    var found = false
                    for (subElement in element.getEnclosedElements()) {
                        for (mirror in subElement.getAnnotationMirrors()) {
                            for (annotation in supportedAnnotations) {
                                if (mirror.getAnnotationType().asElement() == annotation) {
                                    typeElements.add(element)
                                    found = true
                                    break
                                }
                            }
                            if (found) break
                        }
                        if (found) break
                    }
                }
            }
            return typeElements
        }
    }
}