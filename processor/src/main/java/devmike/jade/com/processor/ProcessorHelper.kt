package devmike.jade.com.processor

import com.squareup.kotlinpoet.*
import devmike.jade.com.annotations.read.*
import devmike.jade.com.annotations.sharedprefs.SharedPref
import devmike.jade.com.annotations.sharedprefs.SharedString
import java.io.File
import java.util.HashSet
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.Name
import javax.lang.model.element.TypeElement
import javax.lang.model.util.ElementFilter

internal object ProcessorHelper {

    const val KAPT_KOTLIN_GENERATED = "kapt.kotlin.generated"

    public fun process(
        processingEnv: ProcessingEnvironment,
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ) {
        val elementsToProcess = getTypeElementsToProcess(roundEnv.rootElements, annotations)
        val generatedRoot: String = processingEnv.options[KAPT_KOTLIN_GENERATED].orEmpty()

        for (typeElement in elementsToProcess) {

            val typeName: String = typeElement.simpleName.toString()

            //Use the package name and the type name to form a class name
            val packageName: String = processingEnv.elementUtils.getPackageOf(typeElement).qualifiedName.toString()

            //Root class name
            val className = ClassName(packageName, typeName)


            //Generic class
            val genericClass = ClassName("", NameStore.Class.ANY_CLASS)


            //Define the wrapper class
            val classBuilder = TypeSpec.objectBuilder(NameStore.getGeneratedClassName(typeName))
                .addProperty(
                    PropertySpec.builder(
                        NameStore.Variable.CLASS_VAR, genericClass
                            .copy(nullable = false)
                    )
                        .mutable()
                        .addModifiers(KModifier.PRIVATE, KModifier.LATEINIT)
                        .build()
                ).addProperty(
                    PropertySpec.builder(
                        NameStore.Variable.SHARED_PREF_VALUE,
                        ClassName(
                            NameStore.Package.ANDROID_SHAREDPREF,
                            NameStore.Class.ANDROID_SHARED_PREF
                        ).copy(nullable = false)
                    )
                        .mutable()
                        .addModifiers(KModifier.PRIVATE, KModifier.LATEINIT)
                        .build()
                ).addProperty(
                    PropertySpec.builder(
                        NameStore.Variable.SHARED_EDITOR,
                        ClassName(
                            NameStore.Package.ANDROID_SHAREDPREF,
                            NameStore.Class.ANDROID_SHARED_PREF_EDITOR
                        )
                            .copy(nullable = false)
                    )
                        .mutable()
                        .addModifiers(KModifier.PRIVATE, KModifier.LATEINIT)
                        .build()
                )
                .addModifiers(KModifier.PUBLIC)


            //Method to access CLASS members
            val buildClassAccessBuilder = FunSpec.builder(NameStore.Method.INIT_SHAREDPREF)
                .addModifiers(KModifier.PUBLIC)
                .addParameter(NameStore.Variable.CONTEXT, className)
                .addParameter(NameStore.Variable.CLASS_VAR, genericClass)
                .addStatement("this.%L = %L", NameStore.Variable.CLASS_VAR, NameStore.Variable.CLASS_VAR)
                .addStatement(
                    "%N(%N)", NameStore.Method.SHARED_PREF,
                    NameStore.Variable.CONTEXT
                ).returns(ClassName(packageName, NameStore.getGeneratedClassName(className.simpleName)))
                .addStatement("return this")

            /**Start the code that generates SharedPreference**/
            //Get Shared pref
            val buildSharedPrefBuilder = FunSpec.builder(NameStore.Method.SHARED_PREF)
                .returns(
                    ClassName(
                        NameStore.Package.ANDROID_SHAREDPREF,
                        NameStore.Class.ANDROID_SHARED_PREF
                    )
                )
                .addParameter(
                    NameStore.Variable.CONTEXT,
                    ClassName(
                        NameStore.Package.CONTEXT,
                        NameStore.Class.ANDROID_CONTEXT
                    )
                )
                .addModifiers(KModifier.PRIVATE)
            for (parameter in ElementFilter.methodsIn(typeElement.enclosedElements)) {
                val baseSharedPrefA = parameter.getAnnotation(SharedPref::class.java)
                if (baseSharedPrefA != null) {
                    buildSharedPrefBuilder.addModifiers(KModifier.PUBLIC)
                        .addStatement(
                            "this.%L = %N.getSharedPreferences(%S, %L.%L)", //Generate the sharedpreference
                            NameStore.Variable.SHARED_PREF_VALUE,
                            NameStore.Variable.CONTEXT,
                            baseSharedPrefA.name,
                            ClassName(
                                NameStore.Package.CONTEXT,
                                NameStore.Class.ANDROID_CONTEXT
                            ),
                            "MODE_PRIVATE"
                        )
                        .addStatement(
                            "this.%L = %L.edit()",
                            NameStore.Variable.SHARED_EDITOR,
                            NameStore.Variable.SHARED_PREF_VALUE
                        )
                        //.addStatement("%N(%N)", NameStore.Method.SHARED_PREF_INSERT_VALUE,
                        //  NameStore.Variable.SHARED_EDITOR)
                        .addStatement("%N()", NameStore.Method.SHARED_PREF_READ_VALUE)
                        .addComment("Read the saved value from the SharedPreference")
                        .addStatement("return %N", NameStore.Variable.SHARED_PREF_VALUE)

                }
            }


            //TODO:FIXING NONWWWWW
            funSpecBuilder_build(classBuilder)


            /*-------*/
            //TODO:FIXING NONWWWWW
            val buildReadSharedPrefValueBuilder = FunSpec.builder(NameStore.Method.SHARED_PREF_READ_VALUE)
                .addModifiers(KModifier.PRIVATE)
            //Generate the statement that saved value into the SharedPreference
            for (annotatedParam in ElementFilter.fieldsIn(typeElement.enclosedElements)) {
                val annotatedList = listOf(ReadString::class.simpleName,
                    ReadLong::class.simpleName, ReadStringSet::class.simpleName,
                    ReadFloat::class.simpleName,
                    ReadInt::class.simpleName)

                pickAnnotation(buildReadSharedPrefValueBuilder, annotatedList,
                    annotatedParam, className)
                //if (valueSharedPref != null) {
                  //  buildReadSharedPrefValueBuilder.addStatement()
               // }

            }

            classBuilder.addFunction(buildClassAccessBuilder.build())
            classBuilder.addFunction(buildReadSharedPrefValueBuilder.build())
            classBuilder.addFunction(buildSharedPrefBuilder.build())


            val file = File(generatedRoot as String).apply { mkdir() }
            FileSpec.builder(packageName, NameStore.getGeneratedClassName(typeName))
                .addType(classBuilder.build())
                .build()
                .writeTo(file)
        }
    }

    private fun pickAnnotation(buildReadSharedPrefValueBuilder:
                   FunSpec.Builder,
                   annotatedList: List<String?>, annotatedParam: Element,
                   className: ClassName) {

        /* "(%L as %L).%L = %L.get%L(%S, %S)",
        NameStore.Variable.CLASS_VAR,
        className,
        annotatedParam.simpleName,
        NameStore.Variable.SHARED_PREF_VALUE,
        valueSharedPref.defaultValue::class.simpleName.toString(),
        valueSharedPref.key,
        valueSharedPref.defaultValue*/

        for (an in annotatedList) {
            if (an == ReadStringSet::class.simpleName) {
               val valueSharedPref = annotatedParam.getAnnotation(ReadStringSet::class.java)
                if (valueSharedPref != null)
                    buildReadSharedPrefValueBuilder.addStatement(
                        "(%L as %L).%L = %L.get%L(%S, %L)",
                        NameStore.Variable.CLASS_VAR,
                        className,
                        annotatedParam.simpleName,
                        NameStore.Variable.SHARED_PREF_VALUE,
                        valueSharedPref.defaultValue::class.simpleName.toString(),
                        valueSharedPref.key,
                        valueSharedPref.defaultValue
                    )
            }else if (an == ReadString::class.simpleName) {
                val  valueSharedPref = annotatedParam.getAnnotation(ReadString::class.java)
                if (valueSharedPref != null)
                    buildReadSharedPrefValueBuilder.addStatement(
                        "(%L as %L).%L = %L.get%L(%S, %S)",
                        NameStore.Variable.CLASS_VAR,
                        className,
                        annotatedParam.simpleName,
                        NameStore.Variable.SHARED_PREF_VALUE,
                        valueSharedPref.defaultValue::class.simpleName.toString(),
                        valueSharedPref.key,
                        valueSharedPref.defaultValue
                    )

            }else if (an == ReadInt::class.simpleName) {
                val valueSharedPref = annotatedParam.getAnnotation(ReadInt::class.java)
                if (valueSharedPref != null)
                    buildReadSharedPrefValueBuilder.addStatement(
                        "(%L as %L).%L = %L.get%L(%S, %L)",
                        NameStore.Variable.CLASS_VAR,
                        className,
                        annotatedParam.simpleName,
                        NameStore.Variable.SHARED_PREF_VALUE,
                        valueSharedPref.defaultValue::class.simpleName.toString(),
                        valueSharedPref.key,
                        valueSharedPref.defaultValue
                    )

            }else if (an == ReadLong::class.simpleName) {
                val valueSharedPref = annotatedParam.getAnnotation(ReadLong::class.java)
                if (valueSharedPref != null)
                    buildReadSharedPrefValueBuilder.addStatement(
                        "(%L as %L).%L = %L.get%L(%S, %L)",
                        NameStore.Variable.CLASS_VAR,
                        className,
                        annotatedParam.simpleName,
                        NameStore.Variable.SHARED_PREF_VALUE,
                        valueSharedPref.defaultValue::class.simpleName.toString(),
                        valueSharedPref.key,
                        valueSharedPref.defaultValue
                    )

            } else if (an == ReadFloat::class.simpleName) {
                val valueSharedPref = annotatedParam.getAnnotation(ReadFloat::class.java)
                if (valueSharedPref != null)
                    buildReadSharedPrefValueBuilder.addStatement(
                        "(%L as %L).%L = %L.get%L(%S, %L)",
                        NameStore.Variable.CLASS_VAR,
                        className,
                        annotatedParam.simpleName,
                        NameStore.Variable.SHARED_PREF_VALUE,
                        valueSharedPref.defaultValue::class.simpleName.toString(),
                        valueSharedPref.key,
                        valueSharedPref.defaultValue
                    )

                //  }
            }
        }
    }

    private fun funSpecBuilder_build(classBuilder: TypeSpec.Builder) {
        val types = listOf(
            String::class.simpleName, Integer::class.simpleName,
            Long::class.simpleName, Float::class.simpleName,
            "${String::class.simpleName}${Set::class.simpleName}",
            Boolean::class.simpleName
        )
        var builder: FunSpec.Builder = FunSpec.getterBuilder()
        for (i in types.indices) {
            val type = types[i]
            if (type != null) {

                //Start creating methods
                builder =
                        FunSpec.builder(NameStore.Method.SHARED_PREF_INSERT_VALUE)
                            .addModifiers(KModifier.PUBLIC)
                if (type.equals("${String::class.simpleName}${Set::class.simpleName}")) {
                    builder.addParameter(NameStore.Variable.ARGUMENT, ClassName("", NameStore.Class.STRING_SET))
                } else {
                    builder.addParameter(NameStore.Variable.ARGUMENT, ClassName("", type))
                }
                builder.addParameter(NameStore.Variable.SHARED_VALUE_KEY, String::class)
                builder.addComment("Insert $type type into SharedPreferences")
                builder.addStatement(
                    "%L.put%L(%L, %L)",
                    NameStore.Variable.SHARED_EDITOR,
                    type,
                    NameStore.Variable.SHARED_VALUE_KEY,
                    NameStore.Variable.ARGUMENT
                )
                builder.addCode("${NameStore.Variable.SHARED_EDITOR}.commit() \n")
                classBuilder.addFunction(builder.build())
            }
        }
    }

    fun getTypeElementsToProcess(elements: Set<Element>, supportedAnnotations: Set<Element>): Set<TypeElement> {
        val typeElements = HashSet<TypeElement>()
        for (element in elements) {
            if (element is TypeElement) {
                var found = false
                for (subElement in element.getEnclosedElements()) {
                    for (mirror in subElement.getAnnotationMirrors()) {
                        for (annotation in supportedAnnotations) {
                            if (mirror.getAnnotationType().asElement() == annotation) {
                                typeElements.add(element as TypeElement)
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