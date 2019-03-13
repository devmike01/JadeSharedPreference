package devmike.jade.com.compiler

import devmike.jade.com.annotations.Preference
import devmike.jade.com.compiler.sharedpreference.ProcessorHelper
import devmike.jade.com.annotations.SharedPref
import devmike.jade.com.annotations.preference.*
import devmike.jade.com.annotations.sharedpreference.*
import devmike.jade.com.compiler.preferences.PreferenceProcessorHelper
import java.lang.IllegalArgumentException
import javax.annotation.processing.*
import javax.lang.model.element.TypeElement


abstract class AnnotationProcessor : AbstractProcessor() {

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        //ProcessorHelper.process(processingEnv, annotations, roundEnv)

        if (prefName() == NameStore.SUFFIX_SHAREDPREF_CLASSNAME) {
            ProcessorHelper.Builder()
                .addParameters(annotations, roundEnv, processingEnv)
                .build()
        }else if (prefName() == NameStore.SUFFIX_PREFERENCE_CLASSNAME){
            PreferenceProcessorHelper.Builder()
                .addParameters(annotations, roundEnv, processingEnv)
                .build()
        }
        return true
    }

    abstract fun prefName(): String


    override fun getSupportedAnnotationTypes(): MutableSet<String?>  {

        if (prefName() == NameStore.SUFFIX_PREFERENCE_CLASSNAME){
            //Generates codes for Preference
            return mutableSetOf(
                Preference::class.java.name,
                ReadPrefFloat::class.java.name, ReadInt::class.java.name,
                ReadPrefString::class.java.name,
                ReadPrefLong::class.java.name,
                ReadPrefStringSet::class.java.name)
        }else if(prefName() == NameStore.SUFFIX_SHAREDPREF_CLASSNAME){
            //Generate codes for SharedPreference
            return mutableSetOf(
                SharedPref::class.java.name,
                ReadFloat::class.java.name, ReadInt::class.java.name,
                ReadString::class.java.name,
                ReadLong::class.java.name,
                ReadStringSet::class.java.name)
        }

        throw IllegalArgumentException("AnnotationProcessor: ${prefName()} is not supported")
    }



}