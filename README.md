# FileChooser

Extremely simple, lightweight library for choosing files from android device. 

**Choose:**
* Image, video or both by opening a chooser (from internal storage, cloud, gallery or camera)
* Audio files (from internal storage or audio recorder)
* Any kind of file from internal storage

**Advantages:**
* Easy to use
* Universal code for all android versions and devices
* Permission handling set in library using EasyPermissions library (https://github.com/googlesamples/easypermissions)
* Rich info when file is chosen (name, size, path, uri, duration (for video), bitmap (for image and video))
* Support for both fragment and activity


## Usage ##

Add maven url in app level gradle file:

```gradle
repositories {
      maven {
          url 'https://armdroid.bintray.com/file-chooser/'
      }
}
```

Add dependency in app module:

```gradle
implementation 'com.armdroid:filechooser:1.0'
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

To clean resources call release in `onDestroy`

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
        fileChooser.openChooserForVideo(this, true);
    }

    @Override
    public void onContentSelected(int i, Content content) {
        imageView.setImageBitmap(content.getBitmap());
    }

    @Override
    public void onError(Error error) {
        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

#NOTE#
* Library uses request codes from 6233 to 6241
