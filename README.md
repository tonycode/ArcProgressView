ArcProgressView
===============

[![Platform](http://img.shields.io/badge/platform-android-brightgreen.svg?style=flat)](https://developer.android.com)
[![Language](http://img.shields.io/badge/language-kotlin-blue.svg?style=flat)](https://kotlinlang.org)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![API](https://img.shields.io/badge/API-16%2B-blue.svg?style=flat)](https://apilevels.com)
[![Release](https://jitpack.io/v/tonycode/ArcProgressView.svg)](https://jitpack.io/#tonycode/ArcProgressView)

Android ui-component that display progress as an Arc

![demo](docs/demo.gif)


## Gradle

```kotlin
repositories {
    //...
    maven("https://jitpack.io")
}
```

```kotlin
dependencies {
    //...
    implementation("com.github.tonycode:ArcProgressView:0.1.0")
}
```


## ArcProgressView

```xml
<dev.tonycode.views.ArcProgressView
    android:id="@+id/arcProgressView1"
    android:layout_width="300dp"
    android:layout_height="300dp"
    app:apv_startAngle="-180"
    app:apv_sweepAngle="180"
    app:apv_trackWidth="16dp"
    app:apv_trackColor="#dbdffd"
    app:apv_progress="0.5"
    app:apv_progressWidth="12dp"
    app:apv_progressColor="#646fd4"
    />
```

```kotlin
// configure programmatically
vb.arcProgressView1.apply {
    startAngle = -180f
    sweepAngle = 180f
    trackWidth = 16.dp
    trackColor = 0xFFDBDFFD.toInt()
    progress = 0.5f
    progressWidth = 12.dp
    progressColor = 0xFF646FD4.toInt()
}
```


## License

[MIT](LICENSE)
