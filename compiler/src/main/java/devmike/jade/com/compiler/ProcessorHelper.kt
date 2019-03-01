package devmike.jade.com.compiler

import com.squareup.kotlinpoet.*
import devmike.jade.com.annotations.SharedPref
import devmike.jade.com.annotations.read.*
import devmike.jade.com.processor.NameStore
import java.io.File
import java.lang.NullPointerException
import java.util.EnumSet.range
import java.util.HashSet
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.util.ElementFilter

internal object ProcessorHelper {

    const val KAPT_KOTLIN_GENERATED = "kapt.kotlin.generated"
    private lateinit var buildClassAccessBuilder: FunSpec.Builder
    private lateinit var sharedPrefListenerBuilder: FunSpec.Builder
    private lateinit var readAllBuilder : FunSpec.Builder

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

            //Create SharedPreference change listener function
            sharedPrefListenerBuilder =FunSpec.builder(NameStore.Method.SHAREDPREFERENCE_CHANGED)
                .addModifiers(KModifier.OVERRIDE)
                .addParameter(
                    NameStore.Variable.SHARED_PREF_VALUE,
                    ClassName(
                        NameStore.Package.ANDROID_SHAREDPREF,
                        NameStore.Class.ANDROID_SHARED_PREF
                    )
                )
                .addParameter(NameStore.Variable.SHARED_VALUE_KEY, String::class)


            //Define the wrapper class
            val classBuilder = TypeSpec.classBuilder(NameStore.getGeneratedClassName(typeName))
                .addModifiers(KModifier.INTERNAL) .addProperty(
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
                ) .addSuperinterface(
                    ClassName(
                        NameStore.Package.ANDROID_SHAREDPREF,
                        NameStore.Class.ANDROID_SHARED_PREF.plus(".${NameStore.Interface.ONSHAREDPREF_INTERFACE}")
                    )
                ).addFunction(FunSpec.builder(NameStore.Method.UNREGISTER_SHARED_PREF)
                    .addComment("Call this function to unregister SharedPreference callback")
                    .addStatement("%N.%N(this)",NameStore.Variable.SHARED_PREF_VALUE,
                        NameStore.Method.UNREGISTER_SHARED_PREF_LISTENER).build())

            //Method to access CLASS members
            buildClassAccessBuilder = FunSpec.builder(NameStore.Method.INIT_SHAREDPREF)
                .addModifiers(KModifier.PUBLIC)
                .addParameter(NameStore.Variable.CONTEXT, ClassName(NameStore.Package.CONTEXT, NameStore.Class.ANDROID_CONTEXT))
                .addParameter(NameStore.Variable.CLASS_VAR, genericClass)
                .addStatement("this.%L = %L", NameStore.Variable.CLASS_VAR, NameStore.Variable.CLASS_VAR)
                .addStatement(
                    "%N(%N)", NameStore.Method.SHARED_PREF,
                    NameStore.Variable.CONTEXT
                ).addStatement("%L.registerOnSharedPreferenceChangeListener(this)",
                    NameStore.Variable.SHARED_PREF_VALUE)


                //.returns(ClassName(packageName, NameStore.getGeneratedClassName(className.simpleName)))
               // .addStatement("return this")

            /**Start the code that generates SharedPreference**/
            //Get Shared pref
            val buildSharedPrefBuilder = FunSpec.builder(NameStore.Method.SHARED_PREF)
                .addParameter(
                    NameStore.Variable.CONTEXT,
                    ClassName(
                        NameStore.Package.CONTEXT,
                        NameStore.Class.ANDROID_CONTEXT
                    )
                )
                .addModifiers(KModifier.PRIVATE)
            for (method in ElementFilter.constructorsIn(typeElement.enclosedElements)) {
                if (method != null) {
                    //Create the method where we initialize the SharedPrefence
                    val baseSharedPrefA = method.getAnnotation(SharedPref::class.java)
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
                        // .addComment("Read the saved value from the SharedPreference")

                    }
                }
            }

            //Create method for inserting values into shared preference
            insertValueMethodBuilder(classBuilder)



            val buildReadSharedPrefValueBuilder = FunSpec.builder(NameStore.Method.SHARED_PREF_READ_VALUE)
                .addModifiers(KModifier.PRIVATE)
            //Generate the statement that saved value into the SharedPreference
            for (annotatedParam in ElementFilter.fieldsIn(typeElement.enclosedElements)) {
                if (annotatedParam != null) {
                    val annotatedList = listOf(
                        ReadString::class.simpleName,
                        ReadLong::class.simpleName,
                        ReadStringSet::class.simpleName,
                        ReadFloat::class.simpleName,
                        ReadInt::class.simpleName,
                        ReadAll::class.simpleName
                    )

                    //Select the annotations and use it to generate the appropriate method
                    pickAnnotation(
                        buildReadSharedPrefValueBuilder, annotatedList,
                        annotatedParam, className
                    )
                }

            }


            for (annotatedMethod in ElementFilter.methodsIn(typeElement.enclosedElements)){
                if(annotatedMethod != null){

                    //Add all the annotation to a set

                    //Loop through all the annotated methods

                    val readStringAn = annotatedMethod.getAnnotation(ReadString::class.java)
                    val readStringSetAn = annotatedMethod.getAnnotation(ReadStringSet::class.java)
                    val readIntAn = annotatedMethod.getAnnotation(ReadInt::class.java)
                    val readLongAn = annotatedMethod.getAnnotation(ReadLong::class.java)
                    val readFloatAn = annotatedMethod.getAnnotation(ReadFloat::class.java)
                    val readAll = annotatedMethod.getAnnotation(ReadAll::class.java)


                    if (readFloatAn != null){
                        annotationBuilder(className,
                            annotatedMethod, Float::class.simpleName, readFloatAn.defaultValue,
                            readFloatAn.key)
                    }

                    if(readStringAn != null){
                        annotationBuilder(className,
                            annotatedMethod,
                            String::class.java.simpleName,
                            readStringAn.defaultValue,
                            readStringAn.key)
                    }

                    if (readStringSetAn != null){
                        annotationBuilder(className, annotatedMethod,
                            NameStore.Types.STRINGSET,
                            "mutableSetOf(\"\")",
                            readStringSetAn.key)

                    }

                    if (readIntAn != null){
                        annotationBuilder(className, annotatedMethod, Int::class.simpleName,
                            readIntAn.defaultValue,
                            readIntAn.key)
                    }

                    if (readLongAn != null){

                        annotationBuilder(className, annotatedMethod,
                            LONG.simpleName, readLongAn.defaultValue, readLongAn.key)
                    }

                    if (readAll != null){
                        annotationBuilder(className, annotatedMethod,
                            MutableMap::class.java.name,
                            null,
                            NameStore.Variable.READ_ALL)
                    }
                }
            }

            classBuilder.addFunction(buildClassAccessBuilder.build())
            classBuilder.addFunction(buildReadSharedPrefValueBuilder.build())
            classBuilder.addFunction(buildSharedPrefBuilder.build())
            classBuilder.addFunction(sharedPrefListenerBuilder.build())
            wipeOutSharedPref(classBuilder)


            val file = File(generatedRoot as String).apply { mkdir() }
            FileSpec.builder(packageName, NameStore.getGeneratedClassName(typeName))
                .addType(classBuilder.build())
                .build()
                .writeTo(file)
        }
    }

    private fun annotationBuilder(className : ClassName,
                                  annotatedMethod: Element, annotatedVarType: String?,
                                      defaultValue: Any?, key: String){
        if (annotatedVarType != null) {
            listenerBuilder(className, annotatedMethod, annotatedVarType,
                defaultValue, key)
        }
    }

    private fun listenerBuilder(className : ClassName,
                                annotatedMethod: Element,
                                annotatedVarType: String, defaultValue: Any?, key: String){

        sharedPrefListenerBuilder
            .addComment("Get $annotatedVarType value from SharedPreference")
        if (key != NameStore.Variable.READ_ALL && defaultValue !=null) {
            sharedPrefListenerBuilder.addStatement(
                "if(%N.equals(%S)){(%L as %L).%L(%L.get%L(%S, ${placeHolder(annotatedVarType)}))}",
                    NameStore.Variable.SHARED_VALUE_KEY,
                    key,
                    NameStore.Variable.CLASS_VAR,
                    className,
                    annotatedMethod.simpleName.toString(),
                    NameStore.Variable.SHARED_PREF_VALUE,
                    annotatedVarType,
                    key,
                    defaultValue
                )
        }else{
            sharedPrefListenerBuilder.addStatement("(%L as %L).%L(%L.all)",
                NameStore.Variable.CLASS_VAR,
                className,
                annotatedMethod.simpleName.toString(),
                NameStore.Variable.SHARED_PREF_VALUE
            )
        }

    }

    private fun placeHolder(at: String): String{
        if (at.equals(Float::class.simpleName)){
            return "%Lf"
        }else if(at.equals(String::class.simpleName)){
            return "%S"
        }
        return "%L"
    }


    private fun pickAnnotation(buildReadSharedPrefValueBuilder:
                   FunSpec.Builder,
                               annotatedList: List<String?>, annotatedParam: Element,
                               className: ClassName) {

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
                        "StringSet",
                        valueSharedPref.key,"mutableSetOf(\"\")")
            }

            if (an == ReadString::class.simpleName) {
                val  valueSharedPref = annotatedParam.getAnnotation(ReadString::class.java)
                if (valueSharedPref != null)
                    buildReadSharedPrefValueBuilder.addStatement(
                        "(%L as %L).%L = %L.get%L(%S, %S)",
                        NameStore.Variable.CLASS_VAR,
                        className,
                        annotatedParam.simpleName,
                        NameStore.Variable.SHARED_PREF_VALUE,
                        String::class.simpleName.toString(),
                        valueSharedPref.key,
                        valueSharedPref.defaultValue
                    )

            }
            if (an == ReadInt::class.simpleName) {
                val valueSharedPref = annotatedParam.getAnnotation(ReadInt::class.java)
                if (valueSharedPref != null)
                    buildReadSharedPrefValueBuilder.addStatement(
                        "(%L as %L).%L = %L.get%L(%S, %L)",
                        NameStore.Variable.CLASS_VAR,
                        className,
                        annotatedParam.simpleName,
                        NameStore.Variable.SHARED_PREF_VALUE,
                        Int::class.simpleName.toString(),
                        valueSharedPref.key,
                        valueSharedPref.defaultValue
                    )

            }
            if (an == ReadLong::class.simpleName) {
                val valueSharedPref = annotatedParam.getAnnotation(ReadLong::class.java)
                if (valueSharedPref != null)
                    buildReadSharedPrefValueBuilder.addStatement(
                        "(%L as %L).%L = %L.get%L(%S, %L)",
                        NameStore.Variable.CLASS_VAR,
                        className,
                        annotatedParam.simpleName,
                        NameStore.Variable.SHARED_PREF_VALUE,
                        Long::class.simpleName.toString(),
                        valueSharedPref.key,
                        valueSharedPref.defaultValue
                    )

            }
            if (an == ReadFloat::class.simpleName) {
                val valueSharedPref = annotatedParam.getAnnotation(ReadFloat::class.java)
                if (valueSharedPref != null)
                    buildReadSharedPrefValueBuilder.addStatement(
                        "(%L as %L).%L = %L.get%L(%S, %Lf)",
                        NameStore.Variable.CLASS_VAR,
                        className,
                        annotatedParam.simpleName,
                        NameStore.Variable.SHARED_PREF_VALUE,
                        Float::class.simpleName.toString(),
                        valueSharedPref.key,
                        valueSharedPref.defaultValue
                    )

                //  }
            }

            if (an == ReadAll::class.simpleName){
                val readAllAnnotation = annotatedParam.getAnnotation(ReadAll::class.java)
                if(readAllAnnotation != null){
                    try {
                        buildReadSharedPrefValueBuilder.addStatement(
                                "(%N as %L).%L = %N.all",
                                NameStore.Variable.CLASS_VAR,
                                className,
                                annotatedParam.simpleName,
                                NameStore.Variable.SHARED_PREF_VALUE
                            )
                    }catch (npe: NullPointerException){
                        error(npe)
                    }
                }
            }
        }
    }

    private fun wipeOutSharedPref(classBuilder: TypeSpec.Builder){
        //Generate code to clear the sharedpreference
        classBuilder.addFunction(FunSpec.builder(NameStore.Method.CLEAR_SHARE_PREF)
            .addStatement("%L.clear().apply()", NameStore.Variable.SHARED_EDITOR)
            .build())

        //Remove an item from SharedPreferences
        classBuilder.addFunction(FunSpec.builder(NameStore.Method.REMOVE_FROM_SHARE_PREF)
            .addParameter(NameStore.Variable.SHARED_VALUE_KEY, String::class)
            .addStatement("%L.remove(%L).apply()",
                NameStore.Variable.SHARED_EDITOR, NameStore.Variable.SHARED_VALUE_KEY)
            .build())

    }

    private fun insertValueMethodBuilder(classBuilder: TypeSpec.Builder) {
        //Build function to insert data into SharedPreference
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
                builder.addCode("${NameStore.Variable.SHARED_EDITOR}.apply() \n")
                classBuilder.addFunction(builder.build())
            }
        }

    }


    private fun getTypeElementsToProcess(elements: Set<Element>, supportedAnnotations: Set<Element>): Set<TypeElement> {
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