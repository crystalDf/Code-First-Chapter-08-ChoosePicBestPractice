package com.star.choosepicbestpractice;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private static final int TAKE_PHOTO = 0;
    private static final int CROP_PHOTO = 1;
    private static final int PICK_PHOTO = 2;

    private static final String FILE_NAME = "tempImage.jpg";

    private Button mTakePhotoButton;
    private Button mChooseFromAlbumButton;
    private ImageView mPictureImageView;

    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTakePhotoButton = (Button) findViewById(R.id.take_photo);
        mChooseFromAlbumButton = (Button) findViewById(R.id.choose_from_album);
        mPictureImageView = (ImageView) findViewById(R.id.picture);

        mTakePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File outputImage = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES
                ), FILE_NAME);

                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }

                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mImageUri = Uri.fromFile(outputImage);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                startActivityForResult(intent, TAKE_PHOTO);
            }
        });

        mChooseFromAlbumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File outputImage = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES
                ), FILE_NAME);

                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }

                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mImageUri = Uri.fromFile(outputImage);

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_PHOTO);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(mImageUri, "image/*");
                    intent.putExtra("scale", true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);

                    startActivityForResult(intent, CROP_PHOTO);
                }
                break;
            case CROP_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(
                                getContentResolver().openInputStream(mImageUri)
                        );
                        mPictureImageView.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case PICK_PHOTO:
                if (resultCode == RESULT_OK) {
                    Uri fromImageUri = null;
                    if (data != null) {
                        fromImageUri = data.getData();
                    }
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(fromImageUri, "image/*");
                    intent.putExtra("scale", true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);

                    startActivityForResult(intent, CROP_PHOTO);
                }
                break;
            default:
                break;
        }
    }
}
