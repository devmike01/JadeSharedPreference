# JadeSharedPreference
A lite weight android library wrapper for SharedPreferences which uses annotation processing to generate boilerplate code for you.

* Eliminate the whole `SharedPreference` code
* Write less code to configure JadeSharedPreference
* Save multiple values to JadeSharedPrefence at onces
* Read from JadeSharedPreference using just annotations `@Read...`)

```kotin
class InecBox(context: Context) {
	
    //Read item from JadeSharedPreference	
    @ReadString("ballot")
    var ballotPaper: String? = null

    private var jsp :JadeSharedPreference jsp =JadeSharedPreference.plug(this, context)

    @SharedPref("key")
    fun inecBox() {
    	//Insert item to JadeSharedPreference
        jsp.insert("ballot", "Ballot Paper")
    }
    
    fun readBallot(){
    	Log.d(InecBox::class.simpleName, ballotPaper)
    }
    
}

```

### Configuration
##### Step 1: Add it in your root build.gradle at the end of repositories:
```groovy
allprojects {
	repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  ```
##### Step 2: Add the dependency
```groovy
	dependencies {
		implementation 'com.github.devmike01.JadeSharedPreference:binder:0.2.15'
		kapt 'com.github.devmike01.JadeSharedPreference:compiler:0.2.15'
	}	
```
##### Note: Add `apply plugin: 'kotlin-kapt'` - if you don't already have it, to your app `build.gradle` to allow the the processor generates the necessary codes.


License
-------

    Copyright 2013 Jake Wharton

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
