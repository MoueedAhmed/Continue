package com.amoueed.continueapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.os.Bundle;
import android.os.Environment;

import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class TestActivity extends AppCompatActivity {

    private FirebaseStorage storage;
    private StorageReference storageRef;
    private StorageReference dirReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        String dirLocationFirebase = "initial_content/1";
        // Create a reference with an initial file path and name
        dirReference = storageRef.child(dirLocationFirebase);

        final File directory = getStorageDir(TestActivity.this, "content");

        dirReference.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference item : listResult.getItems()) {
                            // All the items under listRef.
                            File file = null;
                            try {
                                file = new File(directory, item.getName());
                            } catch (Exception e) {
                                Toast.makeText(TestActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            item.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Toast.makeText(TestActivity.this, "Downloading: "
                                            +taskSnapshot.getStorage().getName(), Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Toast.makeText(TestActivity.this, "Failed downloading", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TestActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public File getStorageDir(Context context, String dirName) {
        // Get the directory
        File dir = new File(context.getFilesDir(), dirName);
        if (!dir.mkdirs()) {
            Toast.makeText(TestActivity.this,
                    "Failed creating directory " + dirName, Toast.LENGTH_SHORT).show();
        }
        return dir;
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}

