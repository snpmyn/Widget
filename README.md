### 介绍
部件

### 依赖
#### 自身
##### app
implementation "org.jetbrains.kotlin:kotlin-stdlib:1.3.41@jar"
##### AndroidLibrary - Library
* implementation "androidx.core:core-ktx:1.2.0-alpha02"
* implementation "org.jetbrains.kotlin:*kotlin-stdlib-jdk7*:$kotlin_version"
* api 'com.github.snpmyn:*Util*:master-SNAPSHOT'（避重）
* implementation 'com.pnikosis:materialish-progress:1.7'
* api 'com.hwangjr.rxbus:rxbus:2.0.1'（避重）
* api 'com.willowtreeapps.spruce:spruce-android:1.0.1'（避重）
#### com.github.snpmyn:Util(api)
##### AndroidLibrary - UtilOne
* api 'com.github.bumptech.glide:glide:4.9.0'（避重）
* api 'com.google.android.material:material:1.1.0-alpha08'（避重）
* api 'com.jakewharton.timber:timber:4.7.1'（避重）
* implementation 'com.qw:soulpermission:1.1.8'
* implementation 'org.apache.commons:commons-lang3:3.9'

### 权限
#### 自身
##### app
* <uses-permission android:name="android.permission.RECORD_AUDIO" />（避重）
* <uses-permission android:name="android.permission.READ_CONTACTS" />（避重）
* <uses-permission android:name="android.permission.SEND_SMS" />（避重）
##### AndroidLibrary - Library
* <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />（避重）
* <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />（避重）
#### com.github.snpmyn:Util
##### app
* <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />（避重）
* <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />（避重）

