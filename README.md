# WitzCraft
Android library for get screen size and calculate text size base on screen width

### Maven
```
<dependency>
  <groupId>com.makeawitz</groupId>
  <artifactId>witzcraft</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```

### Gradle
```
implementation 'com.makeawitz:witzcraft:1.0.0'
```

## Example
### ScreenMetrics
```
// declare new screenMetrics object
val screenMetrics = ScreenMetrics(context) 

fun someFun() {
  val screenWidth: Int = screenMetrics.screenWidth          // width of the entire display screen in pixel
  val screenHeight: Int = screenMetrics.screenHeight        // height of the display screen in pixel without actionBar, statusBar, and navigationBar
  val screenRawHeight: Int = screenMetrics.screenRawHeight  // height of the entire display screen in pixel
  val dpi: Int = screenMetrics.dpi                          // densityDpi of the device
  
  // TODO: any app logic
}
```

### CalculatedTextSize
```
// declare new calculatedSize object
val calculatedSize = CalculatedTextSize(screenMetrics)

fun someFun() {
  val small: Float = calculatedSize.small
  val regular: Float = calculatedSize.regular
  val subHeader: Float = calculatedSize.subHeader
  val header: Float = calculatedSize.header
  val largeHeader: Float = calculatedSize.largeHeader
  val huge: Float = calculatedSize.huge
  
  // TODO: any app logic
}
```
