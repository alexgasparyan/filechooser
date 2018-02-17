# FileChooser

Extremely simple, lightweight library for choosing files from android device. 

![image](https://raw.githubusercontent.com/alexgasparyan/filechooser/master/sample.gif)  


**Choose:**
* Image, video or both by opening a chooser (from internal storage, cloud, gallery or camera)
* Audio files (from internal storage or audio recorder)
* Any kind of file from internal storage or cloud

**Advantages:**
* Universal code for all android versions and devices
* Easy to use
* Permission handling set in library using EasyPermissions library (https://github.com/googlesamples/easypermissions)
* Rich info when file is chosen (name, size, path, uri, duration (for video), bitmap (for image and video))
* `minSdkVersion 17` (Android 4.2 Jelly Bean)
* Support for both fragment and activity


## Usage ##

A sample project is attached to library where all the features of library are used. You can also test devices and OS versions with this.

Add maven url in app level gradle file:

```gradle
repositories {
      maven {
          url 'https://armdroid.bintray.com/file-chooser/'
      }
}
```

Add dependency in app module gradle file:

```gradle
implementation 'com.armdroid:filechooser:1.0.6'
```

Add necessary permissions to `Manifest` file that you would normally add. For example, to use camera add:

```xml
<uses-permission android:name="android.permission.CAMERA"/>
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
```

To pass results to FileChooser override:
* `onRequestPermissionsResult`
* `onActivityResult`

To clean resources call `release` in `onDestroy`.
* Below is a sample code for `Activity`.
* Exact same code is applied to `Fragment`, **_however the activity that contains the fragment should call `super.onActivityResult()` if `onActivityResult` is overriden_**

```java
public class MainActivity extends AppCompatActivity implements OnContentSelectedListener {

    private FileChooser fileChooser;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.image_view);
        fileChooser = new FileChooser(this);
        fileChooser.getImage(this, true);
    }

    @Override
    public void onContentSelected(int fileType, Content content) {
        if (fileType == FileType.TYPE_IMAGE) {
            imageView.setImageBitmap(content.getBitmap());
        }
    }

    @Override
    public void onError(Error error) {
        if (error.getType() == Error.NULL_PATH_ERROR) {
            Toast.makeText(this, "Could not locate path of the file", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        fileChooser.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fileChooser.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        fileChooser.release();
        super.onDestroy();
    }
}
```


## Note ##
* To guarantee that the current activity is not recreated when returned add `android:configChanges="screenLayout|screenSize|orientation"` in `Manifest` or handle changes yourself
* Library does not guarantee that the chosen file has the type specified by user if chosen from third party cloud or internal storage apps (i.e. Google Drive, Dropbox, ES Explorer, Astro)
* Library uses request codes from 6233 to 6242 to start activities when choosing file
* Library uses provider with authority name `{YOUR_PACKAGE_NAME}.fileProvider` (see merged manifest)

## Supported devices and OS
Please add device names with OS version that have successfully passed all tests available in sample project in the comments of this gist:

https://gist.github.com/alexgasparyan/7f130d9571e7c413a904b3ff031e37f0

## More ##
* Contribution and error reporting is very much appreciated
