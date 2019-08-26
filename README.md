[![Codacy Badge](https://api.codacy.com/project/badge/Grade/a1c9a1b1d1ce4ca7a201ab93492bf6e0)](https://app.codacy.com/project/snpmyn/Widget/dashboard)
[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0)

### 介绍
部件

### 依赖
#### 自身
##### app
implementation "org.jetbrains.kotlin:kotlin-stdlib:1.3.50@jar"
##### AndroidLibrary - Library
* implementation 'androidx.core:core-ktx:1.2.0-alpha03'
* implementation "org.jetbrains.kotlin:*kotlin-stdlib-jdk7*:$kotlin_version"
* api 'com.github.snpmyn:*Util*:master-SNAPSHOT'（避重）
* api 'com.hwangjr.rxbus:rxbus:2.0.1'（避重）
* api 'com.willowtreeapps.spruce:spruce-android:1.0.1'（避重）

#### com.github.snpmyn:Util(api)
##### AndroidLibrary - UtilOne
* api 'com.github.bumptech.glide:glide:4.9.0'（避重）
* api 'com.google.android.material:material:1.1.0-alpha09'（避重）
* api 'com.jakewharton.timber:timber:4.7.1'（避重）
* implementation 'com.qw:soulpermission:1.2.1_x'
* implementation 'org.apache.commons:commons-lang3:3.9'

##### AndroidLibrary - UtilTwo
* implementation 'androidx.core:core-ktx:1.2.0-alpha03'
* implementation "org.jetbrains.kotlin:*kotlin-stdlib-jdk7*:$kotlin_version"

### 权限
#### 自身
##### app
* android:name="android.permission.INTERNET"（避重）
* android:name="android.permission.RECORD_AUDIO"（避重）
* android:name="android.permission.READ_CONTACTS"（避重）
* android:name="android.permission.SEND_SMS"（避重）

##### AndroidLibrary - Library
* android:name="android.permission.ACCESS_COARSE_LOCATION"（避重）
* android:name="android.permission.ACCESS_FINE_LOCATION"（避重）

#### com.github.snpmyn:Util
##### app
* android:name="android.permission.WRITE_EXTERNAL_STORAGE"（避重）
* android:name="android.permission.READ_EXTERNAL_STORAGE"（避重）

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

