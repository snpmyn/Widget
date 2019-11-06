<div align=center><img src="https://github.com/snpmyn/Widget/raw/master/image.png"/></div>

[![Build Status](https://travis-ci.org/snpmyn/Widget.svg?branch=master)](https://travis-ci.org/snpmyn/Widget)
[![codecov.io](https://codecov.io/github/snpmyn/Widget/branch/master/graph/badge.svg)](https://codecov.io/github/snpmyn/Widget)
[![SNAPSHOT](https://jitpack.io/v/Jaouan/Revealator.svg)](https://jitpack.io/#snpmyn/Widget)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/a1c9a1b1d1ce4ca7a201ab93492bf6e0)](https://app.codacy.com/project/snpmyn/Widget/dashboard)
[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0)
[![API](https://img.shields.io/badge/API-19%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=19)

### 介绍
部件。

### 架构

| 模块 | 说明 |
|:-:|:-:|
| 示例app | 用法举例 |
| 一方库Library | 纯本地实现 |
| 一方库Matisse | 据三方库[Matisse](https://github.com/zhihu/Matisse)实现 |
| 一方库Ucrop | 据三方库[uCrop](https://github.com/Yalantis/uCrop)实现 |

### 依赖、权限

| 模块 | 依赖 |
|:-:|:-:|
| 示例app | implementation "org.jetbrains.kotlin:kotlin-stdlib:1.3.50@jar" |
| 示例app | implementation 'io.reactivex.rxjava2:rxandroid:2.1.1' |
| 示例app | implementation 'io.reactivex.rxjava2:rxandroid:2.2.13' |
| 一方库Library | implementation 'androidx.core:core-ktx:1.2.0-beta01' |
| 一方库Library | implementation "org.jetbrains.kotlin:*kotlin-stdlib-jdk7*:$kotlin_version" |
| 一方库Library | implementation 'androidx.palette:palette:1.0.0' |
| 一方库Library | api 'com.github.snpmyn:*Util*:master-SNAPSHOT'（避重）|
| 一方库Library | api 'com.willowtreeapps.spruce:spruce-android:1.0.1'（避重）|
| 一方库Matisse | implementation 'com.github.snpmyn:*Util*:master-SNAPSHOT' |
| 一方库Matisse | api 'com.zhihu.android:matisse:0.5.3-beta3'（避重）|
| 一方库Ucrop | api 'com.github.yalantis:ucrop:2.2.4'（避重）|
| 二方库Util-UtilOne | api 'com.github.bumptech.glide:glide:4.10.0'（避重）|
| 二方库Util-UtilOne | api 'com.google.android.material:material:1.2.0-alpha01'（避重）|
| 二方库Util-UtilOne | api 'io.reactivex:rxandroid:1.2.1'（避重）|
| 二方库Util-UtilOne | api 'io.reactivex:rxjava:1.3.8'（避重）|
| 二方库Util-UtilOne | api 'com.jakewharton.timber:timber:4.7.1'（避重）|
| 二方库Util-UtilOne | api 'com.tencent:mmkv-static:1.0.23'（避重）|
| 二方库Util-UtilOne | implementation 'com.getkeepsafe.relinker:relinker:1.3.1' |
| 二方库Util-UtilOne | implementation 'com.qw:soulpermission:1.2.2_x' |
| 二方库Util-UtilOne | implementation 'org.apache.commons:commons-lang3:3.9' |
| 二方库Util-UtilTwo | implementation 'androidx.core:core-ktx:1.2.0-beta01' |
| 二方库Util-UtilTwo | implementation "org.jetbrains.kotlin:*kotlin-stdlib-jdk7*:$kotlin_version" |

| 模块 | 权限 |
|:-:|:-:|
| 示例app | android:name="android.permission.INTERNET"（避重）|
| 示例app | android:name="android.permission.RECORD_AUDIO"（避重）|
| 示例app | android:name="android.permission.READ_CONTACTS"（避重）|
| 示例app | android:name="android.permission.SEND_SMS"（避重）|
| 示例app | android:name="android.permission.ACCESS_NETWORK_STATE"（避重）|
| 示例app | android:name="android.permission.ACCESS_COARSE_LOCATION"（避重）|
| 示例app | android:name="android.permission.ACCESS_FINE_LOCATION"（避重）|
| 示例app | android:name="android.permission.CAMERA"（避重）|
| 二方库Util-app | android:name="android.permission.WRITE_EXTERNAL_STORAGE"（避重）|
| 二方库Util-app | android:name="android.permission.READ_EXTERNAL_STORAGE"（避重）|

### 使用
build.gradle(module)
```
// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {  
    repositories {
        google()
        jcenter()
                
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.5.2'           

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()

    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}
```
build.gradle(app)
```
apply plugin: 'com.android.application'

android {
    ...
    defaultConfig {
        ...      
    }       
    compileOptions {
        sourceCompatibility 1.8
        targetCompatibility 1.8
    }
    configurations.all {
        resolutionStrategy.cacheChangingModulesFor 0, 'seconds'
    }
}

dependencies {
    implementation 'com.github.snpmyn.Widget:library:master-SNAPSHOT'
    implementation 'com.github.snpmyn.Widget:matisse:master-SNAPSHOT'
    implementation 'com.github.snpmyn.Widget:ucrop:master-SNAPSHOT'
}
```

### License
```
Copyright 2019 snpmyn

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

