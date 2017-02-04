package com.couchbase.todolite;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.couchbase.lite.Attachment;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ImageActivity extends AppCompatActivity {
    public static final String INTENT_TASK_DOC_ID = "image";

    private String mTaskDocId;

    //private Firebase firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState != null)
            mTaskDocId = savedInstanceState.getString(INTENT_TASK_DOC_ID);
        else
            mTaskDocId = getIntent().getStringExtra(INTENT_TASK_DOC_ID);

        Application application = (Application) getApplication();
        Database database = application.getDatabase();
        Document document = database.getDocument(mTaskDocId);
        Attachment attachment = document.getCurrentRevision().getAttachment("image");

        if (attachment == null)
            return;

        Bitmap image = null;
        InputStream is = null;
        try {
            is = attachment.getContent();
            image = BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            Log.e(Application.TAG, "Cannot display the attached image", e);
        } finally {
            if (is != null) try { is.close(); } catch (IOException e) { }
        }

        ImageView imageView = (ImageView) findViewById(R.id.image);
        imageView.setImageBitmap(image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // TODO: AMS add image here
    public void onSaveInstanceState(Bundle savedInstanceState, Bitmap image) {
        savedInstanceState.putString(INTENT_TASK_DOC_ID, mTaskDocId);
        super.onSaveInstanceState(savedInstanceState);

//        // Create a storage reference from our app
//        StorageReference storageRef = storage.getReferenceFromUrl("gs://barlyzer.appspot.com");
//        // Create a reference to "mountains.jpg"
//        StorageReference mountainsRef = storageRef.child("barley.jpg");
//        // Create a reference to 'images/mountains.jpg'
//        StorageReference mountainImagesRef = storageRef.child("images/barley.jpg");
//        // While the file names are the same, the references point to different files
//        mountainsRef.getName().equals(mountainImagesRef.getName());    // true
//        mountainsRef.getPath().equals(mountainImagesRef.getPath());    // false
//
//        // Part 2
//
//        Uri file = Uri.fromFile(new File(image));
//        StorageReference riversRef = storageRef.child("images/"+file.getLastPathSegment());
//        uploadTask = riversRef.putFile(file);
//
//        // Register observers to listen for when the download is done or if it fails
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle unsuccessful uploads
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
//                Uri downloadUrl = taskSnapshot.getDownloadUrl();
//            }
//        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
