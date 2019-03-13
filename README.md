# JadeSharedPreference

[![Android Arsenal]( https://img.shields.io/badge/Android%20Arsenal-JadeSharedPreference-green.svg?style=flat )]( https://android-arsenal.com/details/1/7504 )  [![CircleCI](https://circleci.com/gh/devmike01/JadeSharedPreference/tree/master.svg?style=svg)](https://circleci.com/gh/devmike01/JadeSharedPreference/tree/master)

A light weight library which uses annotation processing to generate codes that let you read and write to both `SharedPreferences` and `Preference` files.

* Eliminate the whole `SharedPreference` boilerplace codes
* Write less code to configure JadeSharedPreference
* Save multiple values to both `Preference` and `SharedPreferences` at onces
* Read from `Preference` and `SharedPreferences` using just annotations `@Read...`)
* Listen to value changes in real-time

No one like writing lot of codes just to do a simple task. I mean, who does this:

```kotlin
    val sp: SharedPreference = getSharedPreferences("coconut_head", Context.MODE_PRIVATE)
    val value: String? =sp.getString("key", null)
```
When you can achieve same thing with less line of codes. Such as this:
```kotlin
    @ReadString("key")
    val value: String?= null
```

### What's new in version `1.3.0`

* Bug fixes
* Support for `Preference` file


### Usage

```kotlin
class InecBox @SharedPref("key") @Preference constructor(context: Context) {

    val TAG: String = "InecBox"
	
    @ReadString("ballot")
    var ballotPaper: String? = null
    
    
    @ReadPrefString("pref_ballot")
    var fakeBallot: String? = null

    //Setup for SharedPreference file
    private var jsp :JadeSharedPreference =JadeSharedPreference.apply(this, context)
    
    //Setup for Preference file
    private var jp :JadeSharedPreference =JadeSharedPreference.preference(this, context)

    fun inecBox() {
        jsp.insert("ballot", "Ballot Paper")
    }
    
    fun readBallot(){
    	Log.d(TAG, ballotPaper)
    }
    
    fun fakeInecBox() {
        jp.insert("pref_ballot", "Fake Ballot Paper")
    }
    
    fun readFakeBallot(){
    	Log.d(TAG, fakeBallot)
    }
    
}

```
Check the sample project to see more sample usages.

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
		implementation 'com.github.devmike01.JadeSharedPreference:binder:1.2.16'
		kapt 'com.github.devmike01.JadeSharedPreference:compiler:1.2.16'
	}	
```
> Note: Add `apply plugin: 'kotlin-kapt'` - if you don't already have it, to your app `build.gradle` to allow the the processor generates the necessary codes.


License
-------

    Copyright 2018 Oladipupo Gbenga

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
