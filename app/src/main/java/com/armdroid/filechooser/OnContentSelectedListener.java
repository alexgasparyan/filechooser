package com.armdroid.filechooser;


public interface OnContentSelectedListener {

    void onContentSelected(int fileType, Content content);

    void onError(Error error);
}