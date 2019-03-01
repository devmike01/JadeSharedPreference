package devmike.jade.com.compiler.factory

import devmike.jade.com.compiler.interfaces.JadeProcessor
import devmike.jade.com.compiler.preferencecompiler.PreferenceProcessorHelper
import devmike.jade.com.compiler.sharedprefcompiler.SharedPrefProcessorHelper
import java.lang.IllegalArgumentException

/**
 * Factory determine which the annotation to process
 */
object ProcessorFactory {

    enum class Processors{
        SHAREDPREFERENCE, PREFERENCE
    }

    fun make(processor: Processors): JadeProcessor{
        if (processor == ProcessorFactory.Processors.SHAREDPREFERENCE){
            return SharedPrefProcessorHelper()
        }else if (processor == ProcessorFactory.Processors.PREFERENCE){
            return PreferenceProcessorHelper()
        }
        throw IllegalArgumentException("${processor.name} is not supported")
    }
}