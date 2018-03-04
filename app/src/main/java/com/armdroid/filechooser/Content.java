package com.armdroid.filechooser;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;

/**
 * Created by Alex Gasparyan on 7/24/2017.
 */

public class Content {

    private String path;
    private Bitmap bitmap;
    private Uri uri;
    private long size;
    private long duration;
    private String fileName;

    public Content(String path, Bitmap bitmap, Uri uri, long size, long duration) {
        this.path = path;
        this.bitmap = bitmap;
        this.uri = uri;
        this.size = size;
        this.duration = duration;
        this.fileName = new File(path).getName();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getSize() {
        return size;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
