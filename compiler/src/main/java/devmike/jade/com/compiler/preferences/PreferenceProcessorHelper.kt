package devmike.jade.com.compiler.preferences

import com.squareup.kotlinpoet.*
import devmike.jade.com.annotations.SettingsPreference
import devmike.jade.com.annotations.preference.*
import devmike.jade.com.annotations.sharedpreference.*
import devmike.jade.com.compiler.FileGenerator
import devmike.jade.com.compiler.NameStore
import java.io.File
import java.lang.NullPointerException
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.util.ElementFilter
import kotlin.reflect.KClass

class PreferenceProcessorHelper {

    open class Builder {

        private lateinit var buildClassAccessBuilder: FunSpec.Builder
        private lateinit var sharedPrefListenerBuilder: FunSpec.Builder
        private lateinit var generatedRoot: String
        private lateinit var packageName: String
        private lateinit var classBuilder: TypeSpec.Builder


        fun addParameters(
            annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment,
            processingEnv: ProcessingEnvironment
        ): PreferenceProcessorHelper.Builder {
            //ProcessorHelper.process(processingEnv, annotations, roundEnv)

            val elementsToProcess =
                FileGenerator.TypeElement.getTypeElementsToProcess(roundEnv.rootElements, annotations)
            generatedRoot = processingEnv.options[NameStore.KAPT_KOTLIN_GENERATED].orEmpty()

            for (typeElement in elementsToProcess) {

                //Get the simple name of class that contains the annotation
                val typeName = typeElement.simpleName.toString()

                //Use the package name and the type name to form a class name
                packageName = processingEnv.elementUtils.getPackageOf(typeElement).qualifiedName.toString()

                //Root class name
                val className = ClassName(packageName, typeName)


                //Generic class
                val genericClass: ClassName = ClassName("", NameStore.Class.ANY_CLASS)

                //Create SharedPreference change listener function
                sharedPrefListenerBuilder = FunSpec.builder(NameStore.Method.SHAREDPREFERENCE_CHANGED)
                    .addModifiers(KModifier.OVERRIDE)
                    .addParameter(
                        NameStore.Variable.SHARED_PREF_VALUE,
                        ClassName(
                            NameStore.Package.ANDROID_SHAREDPREF,
                            NameStore.Class.ANDROID_SHARED_PREF
                        )
                    )
                    .addParameter(NameStore.Variable.SHARED_VALUE_KEY, String::class)


                //Define the wrapper class and implements the SharedPreference Listener
                buildClassWrapper(genericClass, NameStore.getGeneratedPreferenceClassName(typeName))

                //Method to access CLASS members
                buildClassAccessBuilder =
                    FunSpec.builder(NameStore.Method.INIT_SHAREDPREF)//NameStore.Method.INIT_SHAREDPREF)
                        .addModifiers(KModifier.PUBLIC)
                        .addParameter(
                            NameStore.Variable.CONTEXT,
                            ClassName(NameStore.Package.CONTEXT, NameStore.Class.ANDROID_CONTEXT)
                        )
                        .addParameter(NameStore.Variable.CLASS_VAR, genericClass)
                        .addStatement("this.%L = %L", NameStore.Variable.CLASS_VAR, NameStore.Variable.CLASS_VAR)
                        .addStatement(
                            "%N(%N)", NameStore.Method.SHARED_PREF,
                            NameStore.Variable.CONTEXT
                        ).addStatement(
                            "%L.registerOnSharedPreferenceChangeListener(this)",
                            NameStore.Variable.SHARED_PREF_VALUE
                        )


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



               // val buildReadSharedPrefValueBuilder = FunSpec.builder(NameStore.Method.SHARED_PREF_READ_VALUE)
                 //   .addModifiers(KModifier.PRIVATE)

                for (element in ElementFilter.constructorsIn(typeElement.enclosedElements)) {
                    if (element != null) {
                        //Create the element where we initialize the SharedPrefence
                        val settingsPref = element.getAnnotation(SettingsPreference::class.java)


                        //Read data from annotations
                        val readStringAn = element.getAnnotation(ReadPrefString::class.java)
                        val readStringSetAn = element.getAnnotation(ReadPrefStringSet::class.java)
                        val readIntAn = element.getAnnotation(ReadPrefInt::class.java)
                        val readLongAn = element.getAnnotation(ReadPrefLong::class.java)
                        val readFloatAn = element.getAnnotation(ReadPrefFloat::class.java)
                        val readAll = element.getAnnotation(ReadAllPref::class.java)

                        if (settingsPref != null) {

                            //Generate code for a element that access preference
                            buildSharedPrefBuilder.addModifiers(KModifier.PUBLIC)
                                .addStatement(
                                    "this.%L = %L.%N(%L)", //Generate the sharedpreference
                                    NameStore.Variable.SHARED_PREF_VALUE,
                                    ClassName(
                                        NameStore.Package.ANDROID_PREFERENCE,
                                        NameStore.Class.PREFERENCE_MANAGER
                                    ),
                                    NameStore.Method.GET_DEFAULTSHAREDPREFERENCES,
                                    NameStore.Variable.CONTEXT
                                )
                                .addStatement(
                                    "this.%L = %L.edit()",
                                    NameStore.Variable.SHARED_EDITOR,
                                    NameStore.Variable.SHARED_PREF_VALUE
                                )
                                .addStatement("%N()", NameStore.Method.SHARED_PREF_READ_VALUE)


                            //Read Preference file if a variable is annotated
                            if (readFloatAn != null) {
                                annotationBuilder(
                                    className,
                                    element, Float::class.simpleName, readFloatAn.defaultValue,
                                    readFloatAn.key
                                )
                            }

                            if (readStringAn != null) {
                                annotationBuilder(
                                    className,
                                    element,
                                    String::class.java.simpleName,
                                    readStringAn.defaultValue,
                                    readStringAn.key
                                )
                            }

                            if (readStringSetAn != null) {
                                annotationBuilder(
                                    className, element,
                                    NameStore.Types.STRINGSET,
                                    "mutableSetOf(\"\")",
                                    readStringSetAn.key
                                )

                            }

                            if (readIntAn != null) {
                                annotationBuilder(
                                    className, element, Int::class.simpleName,
                                    readIntAn.defaultValue,
                                    readIntAn.key
                                )
                            }

                            if (readLongAn != null) {

                                annotationBuilder(
                                    className, element,
                                    LONG.simpleName, readLongAn.defaultValue, readLongAn.key
                                )
                            }

                            if (readAll != null) {
                                annotationBuilder(
                                    className, element,
                                    MutableMap::class.java.name,
                                    null,
                                    NameStore.Variable.READ_ALL
                                )
                            }



                            val buildReadSharedPrefValueBuilder = FunSpec.builder(NameStore.Method.SHARED_PREF_READ_VALUE)
                                .addModifiers(KModifier.PRIVATE)
                            //Generate the statement that saved value into the SharedPreference
                            for (annotatedParam in ElementFilter.fieldsIn(typeElement.enclosedElements)) {
                                if (annotatedParam != null) {
                                    val annotatedList = listOf(
                                        ReadPrefString::class.simpleName,
                                        ReadPrefLong::class.simpleName,
                                        ReadPrefStringSet::class.simpleName,
                                        ReadPrefFloat::class.simpleName,
                                        ReadPrefInt::class.simpleName,
                                        ReadAllPref::class.simpleName
                                    )

                                    pickAnnotation(
                                        buildReadSharedPrefValueBuilder, annotatedList,
                                        annotatedParam, className
                                    )
                                }

                            }


                            //Create method for inserting values into shared preference
                            insertValueMethodBuilder(classBuilder)
                            classBuilder.addFunction(buildReadSharedPrefValueBuilder.build())
                            classBuilder.addFunction(buildClassAccessBuilder.build())
                            classBuilder.addFunction(buildSharedPrefBuilder.build())
                            classBuilder.addFunction(sharedPrefListenerBuilder.build())
                            wipeOutSharedPref(classBuilder)

                            val file = File(generatedRoot).apply { mkdir() }


                            FileGenerator.builder(packageName, NameStore.getGeneratedPreferenceClassName(typeName))
                                .addType(classBuilder.build())
                                .addFile(file)
                                .build()
                        }

                    }

                }


            }

            return this

        }


        private fun annotationBuilder(
            className: ClassName,
            annotatedMethod: Element, annotatedVarType: String?,
            defaultValue: Any?, key: String
        ) {
            if (annotatedVarType != null) {
                listenerBuilder(
                    className, annotatedMethod, annotatedVarType,
                    defaultValue, key
                )
            }
        }

        private fun listenerBuilder(
            className: ClassName,
            annotatedMethod: Element,
            annotatedVarType: String, defaultValue: Any?, key: String
        ) {

            sharedPrefListenerBuilder
                .addComment("Get $annotatedVarType value from SharedPreference")
            if (key != NameStore.Variable.READ_ALL && defaultValue != null) {
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
            } else {
                sharedPrefListenerBuilder.addStatement(
                    "(%L as %L).%L(%L.all)",
                    NameStore.Variable.CLASS_VAR,
                    className,
                    annotatedMethod.simpleName.toString(),
                    NameStore.Variable.SHARED_PREF_VALUE
                )
            }

        }


        private fun placeHolder(at: String): String {
            if (at.equals(Float::class.simpleName)) {
                return "%Lf"
            } else if (at.equals(String::class.simpleName)) {
                return "%S"
            }
            return "%L"
        }


        private fun pickAnnotation(
            buildReadSharedPrefValueBuilder:
            FunSpec.Builder,
            annotatedList: List<String?>, annotatedParam: Element,
            className: ClassName
        ) {

            for (an in annotatedList) {
                if (an == ReadPrefStringSet::class.simpleName) {
                    val valueSharedPref = annotatedParam.getAnnotation(ReadPrefStringSet::class.java)
                    if (valueSharedPref != null)
                        buildReadSharedPrefValueBuilder.addStatement(
                            "(%L as %L).%L = %L.get%L(%S, %L)",
                            NameStore.Variable.CLASS_VAR,
                            className,
                            annotatedParam.simpleName,
                            NameStore.Variable.SHARED_PREF_VALUE,
                            "StringSet",
                            valueSharedPref.key, "mutableSetOf(\"\")"
                        )
                }

                if (an == ReadPrefString::class.simpleName) {
                    val valueSharedPref = annotatedParam.getAnnotation(ReadPrefString::class.java)
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
                if (an == ReadPrefInt::class.simpleName) {
                    val valueSharedPref = annotatedParam.getAnnotation(ReadPrefInt::class.java)
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
                if (an == ReadPrefLong::class.simpleName) {
                    val valueSharedPref = annotatedParam.getAnnotation(ReadPrefLong::class.java)
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
                if (an == ReadPrefFloat::class.simpleName) {
                    val valueSharedPref = annotatedParam.getAnnotation(ReadPrefFloat::class.java)
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

                if (an == ReadAllPref::class.simpleName) {
                    val readAllAnnotation = annotatedParam.getAnnotation(ReadAllPref::class.java)
                    if (readAllAnnotation != null) {
                        try {
                            buildReadSharedPrefValueBuilder.addStatement(
                                "(%N as %L).%L = %N.all",
                                NameStore.Variable.CLASS_VAR,
                                className,
                                annotatedParam.simpleName,
                                NameStore.Variable.SHARED_PREF_VALUE
                            )
                        } catch (npe: NullPointerException) {
                            error(npe)
                        }
                    }
                }
            }
        }

        private fun wipeOutSharedPref(classBuilder: TypeSpec.Builder) {
            //Generate code to clear the sharedpreference
            classBuilder.addFunction(
                FunSpec.builder(NameStore.Method.CLEAR_SHARE_PREF)
                    .addStatement("%L.clear().apply()", NameStore.Variable.SHARED_EDITOR)
                    .build()
            )

            //Remove an item from SharedPreferences
            classBuilder.addFunction(
                FunSpec.builder(NameStore.Method.REMOVE_FROM_SHARE_PREF)
                    .addParameter(NameStore.Variable.SHARED_VALUE_KEY, String::class)
                    .addStatement(
                        "%L.remove(%L).apply()",
                        NameStore.Variable.SHARED_EDITOR, NameStore.Variable.SHARED_VALUE_KEY
                    )
                    .build()
            )

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


        fun build(): PreferenceProcessorHelper {
            return PreferenceProcessorHelper()
        }


        fun buildClassWrapper(genericClass: ClassName, className: String): TypeSpec.Builder {
            classBuilder = TypeSpec.classBuilder(className)
                .addModifiers(KModifier.INTERNAL).addProperty(
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
                ).addSuperinterface(
                    ClassName(
                        NameStore.Package.ANDROID_SHAREDPREF,
                        NameStore.Class.ANDROID_SHARED_PREF.plus(".${NameStore.Interface.ONSHAREDPREF_INTERFACE}")
                    )
                ).addFunction(
                    FunSpec.builder(NameStore.Method.UNREGISTER_SHARED_PREF)
                        .addComment("Call this function to unregister SharedPreference callback")
                        .addStatement(
                            "%N.%N(this)", NameStore.Variable.SHARED_PREF_VALUE,
                            NameStore.Method.UNREGISTER_SHARED_PREF_LISTENER
                        ).build()
                )
            return classBuilder
        }

    }
}