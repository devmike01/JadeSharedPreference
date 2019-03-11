package devmike.jade.com.compiler

public object NameStore {

    //public fun getGeneratedClassName(className: String) = className.plus(BindingSuffix.
       // GENERATED_CLASS_SUFFIX)

    const val SUFFIX_SHAREDPREF_CLASSNAME: String ="JSP"
    const val SUFFIX_PREFERENCE_CLASSNAME: String ="JSP_Preference"

    public fun getGeneratedSharedPrefClassName(param: String) =
        "JSP$param"


    public fun getGeneratedPreferenceClassName(param: String) =
        "JSP_Preference$param"


    const val KAPT_KOTLIN_GENERATED = "kapt.kotlin.generated"

    object Package {
        const val CONTEXT ="android.content"
        const val ANDROID_SHAREDPREF="android.content"
        const val ANDROID_PREFERENCE ="android.preference"
    }

    object Variable {
        const val SHARED_VALUE_KEY ="key"
        const val SHARED_PREF_VALUE = "sharedPref"
        const val SHARED_EDITOR = "editor"
        const val CONTEXT ="context"
        const val CLASS_VAR ="mClass"
        const val SHAREDPREF_LISTENER ="sharedPrefListener"
        const val ARGUMENT ="arg0"
        const val READ_ALL = "Hey! Read All"
        const val PREFERENCE ="settingPref"
    }


    object Class {
        //Android
        const val ANDROID_CONTEXT ="Context"
        const val ANDROID_SHARED_PREF ="SharedPreferences"
        const val ANY_CLASS = "Any"
        const val ANDROID_SHARED_PREF_EDITOR ="SharedPreferences.Editor"
        const val STRING_SET ="Set<String>"
        const val STRING ="String"
        const val PREFERENCE_MANAGER ="PreferenceManager"
    }

    object Method {
        const val GET_DEFAULTSHAREDPREFERENCES ="getDefaultSharedPreferences"
        const val UNREGISTER_SHARED_PREF_LISTENER ="unregisterOnSharedPreferenceChangeListener"
        const val CONTEXT ="context"
        const val SHARED_PREF ="generateSharePref"
        const val SHARED_PREF_INSERT_VALUE ="insertValue"
        const val SHARED_PREF_READ_VALUE ="readValue"
        const val INIT_SHAREDPREF ="plug"
        const val CLEAR_SHARE_PREF ="clearSharedPref"
        const val REMOVE_FROM_SHARE_PREF= "removeValue"
        const val ADD_OTHER_SET ="customSet"
        const val UNPLUG ="unplug"
        const val UNREGISTER_SHARED_PREF ="unregisterSharedPref"
        const val SHAREDPREFERENCE_CHANGED="onSharedPreferenceChanged"
    }

    object Types{
        const val STRING ="String"
        const val FLOAT ="Float"
        const val INT ="Int"
        const val LONG ="Long"
        const val STRINGSET ="StringSet"
    }

    object Interface{
        const val ONSHAREDPREF_INTERFACE ="OnSharedPreferenceChangeListener"
    }

}