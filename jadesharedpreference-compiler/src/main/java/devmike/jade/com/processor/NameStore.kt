package devmike.jade.com.processor

public object NameStore {

    //public fun getGeneratedClassName(className: String) = className.plus(BindingSuffix.
       // GENERATED_CLASS_SUFFIX)

    public fun getGeneratedClassName(param: String) =
        "JSP$param"


    object Package {
        const val LOGGER : String = "android.util"
        const val CONTEXT ="android.content"
        const val JADEKNIFE_BASE ="devmike.jade.com_JadeSharedPreference"
        const val ANDROID_SHAREDPREF="android.content"
        const val GENERATED_CLASS ="JadeSharedPreference"
        const val CLASS_ ="kotlin"
    }

    object Variable {
        const val SHARED_VALUE_KEY ="key"
        const val SHARED_PREF_VALUE = "sharedPref"
        const val SHARED_EDITOR = "editor"
        const val CONTEXT ="context"
        const val CLASS_VAR ="mClass"

        const val ARGUMENT ="arg0"

    }


    object Class {
        //Android
        const val ANDROID_CONTEXT ="Context"
        const val ANDROID_SHARED_PREF ="SharedPreferences"
        const val ANY_CLASS = "Any"
        const val ANDROID_SHARED_PREF_EDITOR ="SharedPreferences.Editor"
        const val STRING_SET ="Set<String>"
        const val STRING ="String"
    }

    object Method {
        const val CONTEXT ="context"
        const val SHARED_PREF ="generateSharePref"
        const val SHARED_PREF_INSERT_VALUE ="insertValue"
        const val SHARED_PREF_READ_VALUE ="readValue"
        const val INIT_SHAREDPREF ="plug"
        const val CLEAR_SHARE_PREF ="clearSharedPref"
        const val REMOVE_FROM_SHARE_PREF= "removeValue"
        const val ADD_OTHER_SET ="customSet";
    }

    object Types{
        const val STRING ="String"
        const val FLOAT ="Float"
        const val INT ="Int"
        const val LONG ="Long"
        const val STRINGSET ="StringSet"
    }

}