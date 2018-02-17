package com.filechooser;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Alex Gasparyan on 7/24/2017.
 */

public class FileChooser implements EasyPermissions.PermissionCallbacks {

    private final int GET_IMAGE_REQUEST_CODE = 6233;
    private final int GET_VIDEO_REQUEST_CODE = 6234;
    private final int GET_FILE_REQUEST_CODE = 6235;
    private final int GET_AUDIO_REQUEST_CODE = 6236;
    private final int GET_IMAGE_VIDEO_REQUEST_CODE = 6237;
    private final int TAKE_PHOTO_REQUEST_CODE = 6238;
    private final int TAKE_VIDEO_REQUEST_CODE = 6239;
    private final int RECORD_AUDIO_REQUEST_CODE = 6240;
    private final int OPEN_CHOOSER_IMAGE_REQUEST_CODE = 6241;
    private final int OPEN_CHOOSER_VIDEO_REQUEST_CODE = 6242;

    private final int GET_IMAGE_PERMISSION = 1;
    private final int GET_VIDEO_PERMISSION = 2;
    private final int GET_FILE_PERMISSION = 3;
    private final int GET_AUDIO_PERMISSION = 4;
    private final int GET_IMAGE_VIDEO_PERMISSION = 5;
    private final int TAKE_PHOTO_PERMISSION = 6;
    private final int TAKE_VIDEO_PERMISSION = 7;
    private final int RECORD_AUDIO_PERMISSION = 8;
    private final int OPEN_CHOOSER_PERMISSION = 9;

    private final String DEFAULT_DIALOG_TEXT = "Following permissions are required in order to choose resources from phone";
    private AppCompatActivity activity;
    private Fragment fragment;
    private Uri uri;
    private String filePath;
    private OnContentSelectedListener contentListener;
    private boolean justFromGallery = false;
    private String type;
    private ArrayList<String> filePaths;


    public FileChooser(AppCompatActivity activity) {
        this.activity = activity;
        this.filePaths = new ArrayList<>();
    }

    public FileChooser(AppCompatActivity activity, Fragment fragment) {
        this.fragment = fragment;
        this.activity = activity;
        this.filePaths = new ArrayList<>();
    }

    public void getImage(OnContentSelectedListener listener, boolean justFromGallery) {
        getImage(listener, null, justFromGallery);
    }

    public void getImage(OnContentSelectedListener listener, @Nullable String dialogText, boolean justFromGallery) {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
        this.contentListener = listener;
        this.justFromGallery = justFromGallery;
        if (EasyPermissions.hasPermissions(activity, permissions)) {
            getImage();
        } else {
            requestPermissions(dialogText == null ? DEFAULT_DIALOG_TEXT : dialogText,
                    GET_IMAGE_PERMISSION, permissions);
        }
    }

    @AfterPermissionGranted(GET_IMAGE_PERMISSION)
    private void getImage() {
        Intent mediaChooser = new Intent(justFromGallery ? Intent.ACTION_PICK : Intent.ACTION_GET_CONTENT);
        mediaChooser.setType("image/*");
        startActivityForResult(mediaChooser, GET_IMAGE_REQUEST_CODE);
    }

    public void getImageOrVideo(OnContentSelectedListener listener, boolean justFromGallery) {
        getImageOrVideo(listener, null, justFromGallery);
    }

    public void getImageOrVideo(OnContentSelectedListener listener, @Nullable String dialogText, boolean justFromGallery) {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
        this.contentListener = listener;
        this.justFromGallery = justFromGallery;
        if (EasyPermissions.hasPermissions(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            getImageOrVideo();
        } else {
            requestPermissions(dialogText == null ? DEFAULT_DIALOG_TEXT : dialogText,
                    GET_IMAGE_VIDEO_PERMISSION, permissions);
        }
    }

    @AfterPermissionGranted(GET_IMAGE_VIDEO_PERMISSION)
    private void getImageOrVideo() {
        Intent mediaChooser = new Intent(justFromGallery ? Intent.ACTION_PICK : Intent.ACTION_GET_CONTENT);
        mediaChooser.setType("image/* video/*");
        startActivityForResult(mediaChooser, GET_IMAGE_VIDEO_REQUEST_CODE);
    }

    public void getVideo(OnContentSelectedListener listener, boolean justFromGallery) {
        getVideo(listener, null, justFromGallery);
    }

    public void getVideo(OnContentSelectedListener listener, @Nullable String dialogText, boolean justFromGallery) {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        this.contentListener = listener;
        this.justFromGallery = justFromGallery;
        if (EasyPermissions.hasPermissions(activity, permissions)) {
            getVideo();
        } else {
            requestPermissions(dialogText == null ? DEFAULT_DIALOG_TEXT : dialogText,
                    GET_VIDEO_PERMISSION, permissions);
        }
    }

    @AfterPermissionGranted(GET_VIDEO_PERMISSION)
    private void getVideo() {
        Intent intent = new Intent(justFromGallery ? Intent.ACTION_PICK : Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        startActivityForResult(intent, GET_VIDEO_REQUEST_CODE);
    }

    public void getFile(OnContentSelectedListener listener) {
        getFile(listener, null);
    }

    public void getFile(OnContentSelectedListener listener, @Nullable String dialogText) {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
        this.contentListener = listener;
        if (EasyPermissions.hasPermissions(activity, permissions)) {
            getFile();
        } else {
            requestPermissions(dialogText == null ? DEFAULT_DIALOG_TEXT : dialogText,
                    GET_FILE_PERMISSION, permissions);
        }
    }

    @AfterPermissionGranted(GET_FILE_PERMISSION)
    private void getFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, GET_FILE_REQUEST_CODE);
    }

    public void getAudio(OnContentSelectedListener listener) {
        getAudio(listener, null);
    }

    public void getAudio(OnContentSelectedListener listener, @Nullable String dialogText) {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
        this.contentListener = listener;
        if (EasyPermissions.hasPermissions(activity, permissions)) {
            getAudio();
        } else {
            requestPermissions(dialogText == null ? DEFAULT_DIALOG_TEXT : dialogText,
                    GET_AUDIO_PERMISSION, permissions);
        }
    }

    @AfterPermissionGranted(GET_AUDIO_PERMISSION)
    private void getAudio() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        startActivityForResult(intent, GET_AUDIO_REQUEST_CODE);
    }

    public void takePhoto(OnContentSelectedListener listener) {
        takePhoto(listener, null);
    }

    public void takePhoto(OnContentSelectedListener listener, @Nullable String dialogText) {
        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        this.contentListener = listener;
        if (EasyPermissions.hasPermissions(activity, permissions)) {
            takePhoto();
        } else {
            requestPermissions(dialogText == null ? DEFAULT_DIALOG_TEXT : dialogText,
                    TAKE_PHOTO_PERMISSION, permissions);
        }

    }

    @AfterPermissionGranted(TAKE_PHOTO_PERMISSION)
    private void takePhoto() {
        setupMediaFile(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        String packageName = intent.resolveActivity(activity.getPackageManager()).getPackageName();
        activity.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, TAKE_PHOTO_REQUEST_CODE);
    }

    public void takeVideo(OnContentSelectedListener listener) {
        takeVideo(listener, null);
    }

    public void takeVideo(OnContentSelectedListener listener, @Nullable String dialogText) {
        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        this.contentListener = listener;
        if (EasyPermissions.hasPermissions(activity, permissions)) {
            takeVideo();
        } else {
            requestPermissions(dialogText == null ? DEFAULT_DIALOG_TEXT : dialogText,
                    TAKE_VIDEO_PERMISSION, permissions);
        }
    }

    @AfterPermissionGranted(TAKE_VIDEO_PERMISSION)
    private void takeVideo() {
        setupMediaFile(MediaStore.ACTION_VIDEO_CAPTURE);
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        String packageName = intent.resolveActivity(activity.getPackageManager()).getPackageName();
        activity.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, TAKE_VIDEO_REQUEST_CODE);
    }

    public void recordAudio(OnContentSelectedListener listener) {
        recordAudio(listener, null);
    }

    public void recordAudio(OnContentSelectedListener listener, @Nullable String dialogText) {
        String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        this.contentListener = listener;
        if (EasyPermissions.hasPermissions(activity, permissions)) {
            recordAudio();
        } else {
            requestPermissions(dialogText == null ? DEFAULT_DIALOG_TEXT : dialogText,
                    RECORD_AUDIO_PERMISSION, permissions);
        }
    }

    @AfterPermissionGranted(RECORD_AUDIO_PERMISSION)
    private void recordAudio() {
        Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, RECORD_AUDIO_REQUEST_CODE);
    }

    public void openChooserForImage(OnContentSelectedListener listener, boolean justFromGallery) {
        openChooserForImage(listener, null, justFromGallery);
    }

    public void openChooserForImage(OnContentSelectedListener listener, @Nullable String dialogText, boolean justFromGallery) {
        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        this.contentListener = listener;
        this.justFromGallery = justFromGallery;
        this.type = "image/*";
        if (EasyPermissions.hasPermissions(activity, permissions)) {
            openChooser();
        } else {
            requestPermissions(dialogText == null ? DEFAULT_DIALOG_TEXT : dialogText,
                    OPEN_CHOOSER_PERMISSION, permissions);
        }
    }

    public void openChooserForVideo(OnContentSelectedListener listener, boolean justFromGallery) {
        openChooserForVideo(listener, null, justFromGallery);
    }

    public void openChooserForVideo(OnContentSelectedListener listener, @Nullable String dialogText, boolean justFromGallery) {
        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        this.contentListener = listener;
        this.justFromGallery = justFromGallery;
        this.type = "video/*";
        if (EasyPermissions.hasPermissions(activity, permissions)) {
            openChooser();
        } else {
            requestPermissions(dialogText == null ? DEFAULT_DIALOG_TEXT : dialogText,
                    OPEN_CHOOSER_PERMISSION, permissions);
        }
    }

    @AfterPermissionGranted(OPEN_CHOOSER_PERMISSION)
    private void openChooser() {
        PackageManager pm = activity.getPackageManager();
        Intent galleryIntent = new Intent(justFromGallery ? Intent.ACTION_PICK : Intent.ACTION_GET_CONTENT);
        galleryIntent.setType(type);

        String mediaType = type.equals("video/*") ? MediaStore.ACTION_VIDEO_CAPTURE : MediaStore.ACTION_IMAGE_CAPTURE;
        setupMediaFile(mediaType);
        Intent cameraIntent = new Intent(mediaType);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        List<ResolveInfo> resInfo = pm.queryIntentActivities(cameraIntent, 0);
        List<LabeledIntent> intentList = new ArrayList<>();
        for (int i = 0; i < resInfo.size(); i++) {
            ResolveInfo ri = resInfo.get(i);
            String packageName = ri.activityInfo.packageName;
            Intent intent = new Intent(mediaType);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
            intent.setPackage(packageName);
            activity.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon));
        }

        Intent openInChooser = Intent.createChooser(galleryIntent, "Choose from");
        openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new LabeledIntent[intentList.size()]));
        startActivityForResult(openInChooser, type.equals("video/*") ? OPEN_CHOOSER_VIDEO_REQUEST_CODE : OPEN_CHOOSER_IMAGE_REQUEST_CODE);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(activity, perms)) {
            new AppSettingsDialog.Builder(activity).build().show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode < GET_IMAGE_REQUEST_CODE || requestCode > OPEN_CHOOSER_VIDEO_REQUEST_CODE) {
            return;
        }
        if (resultCode == Activity.RESULT_OK) {
            if (data == null && uri == null) {
                contentListener.onError(new Error(Error.NULL_URI_ERROR, "File uri is null"));
            } else {
                Uri uri;
                if (data != null && data.getData() != null) {
                    uri = data.getData();
                    this.filePath = null;
                } else {
                    uri = this.uri;
                }
                this.uri = null;
                if (uri == null) {
                    contentListener.onError(new Error(Error.NULL_URI_ERROR, "File uri is null"));
                    return;
                }
                String path = filePath == null ? UriUtils.getRealPathFromURI(activity, uri) : filePath;
                this.filePath = null;
                long fileSize;
                if (path == null) {
                    File file = UriUtils.saveFileFromUri(activity, uri);
                    if (file == null) {
                        contentListener.onError(new Error(Error.NULL_PATH_ERROR, "File path is null"));
                        return;
                    }
                    path = file.getAbsolutePath();
                    filePaths.add(path);
                    fileSize = file.length();
                } else {
                    fileSize = new File(path).length();
                }

                String mimeType = getMimeType(path, uri);
                if (mimeType == null) {
                    replyToFileResponse(path, uri, requestCode, fileSize);
                    return;
                }

                if (mimeType.startsWith("image")) {
                    replyToImageResponse(path, uri, fileSize, data);
                } else if (mimeType.startsWith("video")) {
                    replyToVideoResponse(path, uri, fileSize);
                } else if (mimeType.startsWith("audio")) {
                    Content content = new Content(path, null, uri, fileSize, getDuration(path));
                    contentListener.onContentSelected(FileType.TYPE_AUDIO, content);
                } else {
                    replyToFileResponse(path, uri, requestCode, fileSize);
                }
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private String getMimeType(String path, Uri uri) {
        String mimeType;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = activity.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        if (mimeType == null) {
            mimeType = URLConnection.guessContentTypeFromName(path);
        }
        return mimeType;
    }

    private void replyToImageResponse(String path, Uri uri, long fileSize, Intent data) {
        Bitmap bitmap = UriUtils.getOrientedBitmap(activity, uri, path, UriUtils.getOrientation(activity, uri, path));
        if (bitmap == null) {
            bitmap = getImageSecondOption(data);
        }
        Content content = new Content(path, bitmap, uri, fileSize, 0);
        contentListener.onContentSelected(FileType.TYPE_IMAGE, content);
    }

    private void replyToVideoResponse(String path, Uri uri, long fileSize) {
        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.MINI_KIND);
        Content content = new Content(path, thumb, uri, fileSize, getDuration(path));
        contentListener.onContentSelected(FileType.TYPE_VIDEO, content);
    }


    private void replyToFileResponse(String path, Uri uri, int requestCode, long fileSize) {
        Content content = new Content(path, null, uri, fileSize, 0);
        contentListener.onContentSelected(FileType.TYPE_FILE, content);
    }

    private long getDuration(String path) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        if (duration == null) {
            return 0;
        }
        try {
            return Long.parseLong(duration);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void setupMediaFile(String type) {
        String fileName;
        if (type.equals(MediaStore.ACTION_VIDEO_CAPTURE)) {
            fileName = "VID_" + System.currentTimeMillis() + ".mp4";
        } else {
            fileName = "IMG_" + System.currentTimeMillis() + ".jpeg";
        }
        File parentFile = new File(Environment.getExternalStorageDirectory(), getAppName(activity));
        parentFile.mkdir();
        File mediaFile = new File(parentFile, fileName);
        try {
            uri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileProvider", mediaFile);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        filePath = mediaFile.getPath();
    }

    private Bitmap getImageSecondOption(Intent data) {
        if (data == null) {
            return null;
        }
        Bundle extras = data.getExtras();
        Bitmap bitmap = null;
        if (extras != null) {
            bitmap = extras.getParcelable("data");
        }
        return bitmap;
    }

    private String getAppName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = packageManager.getApplicationInfo(context.getApplicationInfo().packageName, 0);
        } catch (final PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return ((String) (applicationInfo != null ? packageManager.getApplicationLabel(applicationInfo) : "FileChooser"))
                .replaceAll(" ", "");
    }

    public void release() {
        for (String path : filePaths) {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    private void startActivityForResult(Intent intent, int requestCode) {
        try {
            if (fragment != null) {
                fragment.startActivityForResult(intent, requestCode);
            } else {
                activity.startActivityForResult(intent, requestCode);
            }
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            contentListener.onError(new Error(Error.NO_ACTIVITY_ERROR, "No activity to handle event"));
        }
    }

    private void requestPermissions(String dialogText, int permissionCode, String... permissions) {
        if (fragment != null) {
            EasyPermissions.requestPermissions(fragment, dialogText, permissionCode, permissions);
        } else {
            EasyPermissions.requestPermissions(activity, dialogText, permissionCode, permissions);
        }
    }
}
