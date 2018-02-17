package com.armdroid.testchooser;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.filechooser.Content;
import com.filechooser.Error;
import com.filechooser.FileChooser;
import com.filechooser.OnContentSelectedListener;

public class MainActivity extends AppCompatActivity implements OnContentSelectedListener, View.OnClickListener {

    private FileChooser fileChooser;
    private ImageView imageView;
    private TextView textView;
    private Spinner spinner;
    private Switch switchView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        spinner = findViewById(R.id.spinner);
        button = findViewById(R.id.button);
        switchView = findViewById(R.id.switchView);
        button.setOnClickListener(this);
        fileChooser = new FileChooser(this);
    }

    @Override
    public void onClick(View v) {
        int pos = spinner.getSelectedItemPosition();
        switch (pos) {
            case 0:
                fileChooser.getImage(this, switchView.isChecked());
                break;
            case 1:
                fileChooser.getFile(this);
                break;
            case 2:
                fileChooser.getVideo(this, switchView.isChecked());
                break;
            case 3:
                fileChooser.getImageOrVideo(this, switchView.isChecked());
                break;
            case 4:
                fileChooser.getAudio(this);
                break;
            case 5:
                fileChooser.takePhoto(this);
                break;
            case 6:
                fileChooser.takeVideo(this);
                break;
            case 7:
                fileChooser.recordAudio(this);
                break;
            case 8:
                fileChooser.openChooserForImage(this, switchView.isChecked());
                break;
            case 9:
                fileChooser.openChooserForVideo(this, switchView.isChecked());
                break;
        }
    }

    @Override
    public void onContentSelected(int fileType, Content content) {
        if (content.getBitmap() != null) {
            imageView.setImageBitmap(content.getBitmap());
        }

        textView.setText(
                "Name: " + content.getFileName() + "\n" +
                        "Size: " + content.getSize() + "\n" +
                        "Path: " + content.getPath() + "\n" +
                        "Uri: " + content.getUri().toString() + "\n" +
                        "Duration: " + content.getDuration()
        );
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


