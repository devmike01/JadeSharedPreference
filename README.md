# JadeSharedPreference
A lite wight android library wrapper for SharedPreferences which uses annotation processing to generate boilerplate code for you.

* Eliminate the whole `SharedPreference` code
* Write less code to configure SharedPreference
* Save multiple values to SharedPrefence at onces
* Read SharedPreference using just annotations `@Read...`)

```kotin
class MainActivity : AppCompatActivity() {

    @ReadFloat("hk")
    var myVaue: Float =0.0f

    private lateinit var jsp :JadeSharedPreference

    @SharedPref("key")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        jsp =JadeSharedPreference.plug(this, this)

        jsp.insert("hk", 1.6)
        hel.text = myVaue.toString()

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
	}
```



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
