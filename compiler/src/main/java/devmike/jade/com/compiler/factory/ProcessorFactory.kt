package devmike.jade.com.compiler.factory

import devmike.jade.com.compiler.preferences.PreferenceProcessor
import devmike.jade.com.compiler.sharedpreference.SharedPreferenceProcessor
import java.lang.IllegalArgumentException

object ProcessorFactory {

    /*fun make(factory: Factory): JadeProcessor{
        if (factory == Factory.SHAREDPREFERENCE){
            return SharedPreferenceProcessor()
        }else if(factory == Factory.PREFERENCE){
            return PreferenceProcessor()
        }

        throw IllegalArgumentException("${factory.name} is not allowed")
    }*/
}