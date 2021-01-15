<div align=center><img src="https://github.com/snpmyn/Widget/raw/master/image.png"/></div>

[![Build Status](https://travis-ci.org/snpmyn/Widget.svg?branch=master)](https://travis-ci.org/snpmyn/Widget)
[![codecov.io](https://codecov.io/github/snpmyn/Widget/branch/master/graph/badge.svg)](https://codecov.io/github/snpmyn/Widget)
[![SNAPSHOT](https://jitpack.io/v/Jaouan/Revealator.svg)](https://jitpack.io/#snpmyn/Widget)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/0674f8455af24734b1749a94cf94da93)](https://www.codacy.com/manual/snpmyn/Widget?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=snpmyn/Widget&amp;utm_campaign=Badge_Grade)
[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0)
[![API](https://img.shields.io/badge/API-19%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=19)

[![GitHub stars](https://img.shields.io/github/stars/Bigkoo/Widget.svg?style=social)](https://github.com/Bigkoo/Widget/stargazers) 
[![GitHub forks](https://img.shields.io/github/forks/Bigkoo/Widget.svg?style=social)](https://github.com/Bigkoo/Widget/network) 
[![GitHub watchers](https://img.shields.io/github/watchers/Bigkoo/Widget.svg?style=social)](https://github.com/Bigkoo/Widget/watchers)

### 介绍
部件。

### 架构
| 模块 | 说明 | 补充 |
|:-:|:-:|:-:|
| 示例app | 用法举例 | 无 |
| 一方库Library | 纯本地实现 | 无 |
| 一方库Matisse | 据三方库[Matisse](https://github.com/zhihu/Matisse)实现 | 无 |
| 一方库Ucrop | 据三方库[uCrop](https://github.com/Yalantis/uCrop)实现 | 无 |
| 一方库BgaQrCodeAndroid | 据三方库[BGAQRCode-Android](https://github.com/bingoogolapple/BGAQRCode-Android)实现 | 含一方库Ucrop（限一方库BgaQrCodeAndroid用）|
| 一方库Banner | 据三方库[banner](https://github.com/youth5201314/banner)实现 | 无 |
| 一方库AutoSize | 据三方库[AndroidAutoSize](https://github.com/JessYanCoding/AndroidAutoSize)实现 | 无 |

### 依赖、权限
| 模块 | 依赖 |
|:-:|:-:|
| 示例app | implementation 'org.jetbrains.kotlin:kotlin-stdlib:1.4.21@jar' |
| 示例app | implementation 'io.reactivex.rxjava2:rxandroid:2.1.1' |
| 示例app | implementation 'io.reactivex.rxjava2:rxandroid:2.2.14' |
| 示例app | implementation project(path: ':library') |
| 示例app | implementation project(path: ':matisse') |
| 示例app | implementation project(path: ':ucrop') |
| 示例app | implementation project(path: ':BgaQrCodeAndroid') |
| 示例app | implementation project(path: ':banner') |
| 示例app | implementation project(path: ':smartrefreshlayout') |
| 示例app | implementation project(path: ':autosize') |
| 一方库Library | implementation 'androidx.core:core-ktx:1.5.0-alpha05' |
| 一方库Library | implementation "org.jetbrains.kotlin:*kotlin-stdlib-jdk7*:$kotlin_version" |
| 一方库Library | implementation 'androidx.palette:palette-ktx:1.0.0' |
| 一方库Library | api 'com.github.snpmyn.Util:*utilone*:v0.0.1.2X'（避重）|
| 一方库Library | api 'com.github.snpmyn.Util:*utiltwo*:v0.0.1.2X'（避重）|
| 一方库Library | api 'com.willowtreeapps.spruce:spruce-android:1.0.1'（避重）|
| 一方库Matisse | implementation 'com.github.snpmyn.Util:utilone:v0.0.1.2X' |
| 一方库Matisse | api 'com.zhihu.android:matisse:0.5.3-beta3'（避重）|
| 一方库Ucrop | api 'com.github.yalantis:ucrop:2.2.4'（避重）|
| 一方库BgaQrCodeAndroid | implementation project(path: ':matisse') |
| 一方库BgaQrCodeAndroid | implementation 'com.github.snpmyn.Util:*utilone*:v0.0.1.2X' |
| 一方库Banner | implementation 'com.github.snpmyn.Util:utilone:v0.0.1.2X' |
| 一方库Banner | api 'com.youth.banner:banner:1.4.10'（避重）|
| 一方库AutoSize | implementation 'me.jessyan:autosize:1.1.2' |
| 二方库Util-示例app | implementation project(path: ':utilone') |
| 二方库Util-示例app | implementation project(path: ':utiltwo') |
| 二方库Util-UtilOne | api 'com.google.android.material:material:1.3.0-beta01'（避重）|
| 二方库Util-UtilOne | api 'com.github.bumptech.glide:glide:4.11.0'（避重）|
| 二方库Util-UtilOne | api 'io.reactivex:rxandroid:1.2.1'（避重）|
| 二方库Util-UtilOne | api 'io.reactivex:rxjava:1.3.8'（避重）|
| 二方库Util-UtilOne | api 'com.jakewharton.timber:timber:4.7.1'（避重）|
| 二方库Util-UtilOne | api 'com.tencent:mmkv-static:1.0.23'（避重）|
| 二方库Util-UtilOne | implementation 'com.getkeepsafe.relinker:relinker:1.3.1' |
| 二方库Util-UtilOne | implementation 'com.qw:soulpermission:1.2.2_x' |
| 二方库Util-UtilOne | implementation 'org.apache.commons:commons-lang3:3.11' |
| 二方库Util-UtilTwo | implementation 'androidx.core:core-ktx:1.5.0-alpha05' |
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
| 示例app | android:name="android.permission.VIBRATE"（避重）|
| 一方库Library | 无 |
| 一方库Matisse | 无 |
| 一方库Ucrop | 无 |
| 一方库BgaQrCodeAndroid | 无 |
| 一方库Banner | 无 |
| 一方库AutoSize | 无 |
| 二方库Util-app | android:name="android.permission.READ_EXTERNAL_STORAGE"（避重）|
| 二方库Util-app | android:name="android.permission.WRITE_EXTERNAL_STORAGE"（避重）|
| 二方库Util-UtilOne | 无 |
| 二方库Util-UtilTwo | 无 |

### 问题
#### autosize
> AutoSizeInitConfigure 用 Log.d(xxx, xxx)，须替换为公共方法。
> 用 AndroidAutoSize 致 MaterialDesignAlertDialogBuilder 创建对话框向右偏移。

### 使用
> [SECURITY](https://github.com/snpmyn/Widget/blob/master/SECURITY.md)<br>
> 版本快速迭代中，拉取失败暂时查看源码。

build.gradle(module)
```
// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {  
    repositories {
        google()
        jcenter()
        
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:4.1.1'            

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }  
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
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
    configurations.all {
        resolutionStrategy.cacheChangingModulesFor 0, 'seconds'
    }
}

dependencies {
    implementation 'com.github.snpmyn.Widget:library:v0.0.1.3X'
    implementation 'com.github.snpmyn.Widget:matisse:v0.0.1.3X'
    implementation 'com.github.snpmyn.Widget:ucrop:v0.0.1.3X'
    implementation 'com.github.snpmyn.Widget:BgaQrCodeAndroid:v0.0.1.3X'
    implementation 'com.github.snpmyn.Widget:banner:v0.0.1.3X'
    implementation 'com.github.snpmyn.Widget:autosize:v0.0.1.3X'
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

