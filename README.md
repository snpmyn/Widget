### 介绍
部件

### 依赖
##### 自身
###### app
api "org.jetbrains.kotlin:kotlin-stdlib:1.3.41@jar"（避重）
###### AndroidLibrary - Library
* implementation "androidx.core:core-ktx:1.0.2"
* implementation "org.jetbrains.kotlin:*kotlin-stdlib-jdk7*:$kotlin_version"
* implementation 'com.pnikosis:materialish-progress:1.7'
* api 'com.hwangjr.rxbus:rxbus:2.0.0'（避重）
* api 'com.willowtreeapps.spruce:spruce-android:1.0.1'（避重）
* api 'com.github.snpmyn:*Util*:master-SNAPSHOT'（避重）
##### com.github.snpmyn:Util(api)
###### AndroidLibrary - Application
api 'org.litepal.android:java:3.0.0'（避重）
###### AndroidLibrary - UtilOne
* api 'com.github.bumptech.glide:glide:4.9.0'（避重）
* api 'com.google.android.material:material:1.1.0-alpha08'（避重）
* implementation 'com.qw:soulpermission:1.1.8'
* implementation 'org.apache.commons:commons-lang3:3.9'

### 权限
##### app
* <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />（避重）
* <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />（避重）
* <uses-permission android:name="android.permission.RECORD_AUDIO" />（避重）
##### AndroidLibrary - Library
* <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />（避重）
* <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />（避重）
