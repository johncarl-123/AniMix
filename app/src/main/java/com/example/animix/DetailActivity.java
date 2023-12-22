package com.example.animix;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "DetailActivity";

    TextView detailDesc, detailTopic, detailLang;
    ImageView detailImageURL;
    com.github.clans.fab.FloatingActionButton deleteButton, editButton;
    String key = "";
    String imageURL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailDesc = findViewById(R.id.detailDesc);
        detailImageURL = findViewById(R.id.detailImageURL);
        detailTopic = findViewById(R.id.detailTopic);
        deleteButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);
        detailLang = findViewById(R.id.detailLang);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            detailDesc.setText(bundle.getString("Description"));
            detailTopic.setText(bundle.getString("Title"));
            detailLang.setText(bundle.getString("Genre"));
            key = bundle.getString("Key");
            imageURL = bundle.getString("Image");
            Glide.with(this).load(bundle.getString("Image")).into(detailImageURL);
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEntry();
            }
        });
    }

    private void deleteEntry() {
        if (imageURL == null || imageURL.isEmpty()) {
            Log.e(TAG, "Image URL is null or empty");
            Toast.makeText(this, "Image URL is null or empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Deleting entry from the Firebase Realtime Database (key: " + key + ")");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Android Tutorials");
        reference.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Entry deleted from the Firebase Realtime Database");

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReferenceFromUrl(imageURL);

                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "File deleted from Firebase Storage");
                        Toast.makeText(DetailActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity2.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Failed to delete file from Firebase Storage", e);
                        Toast.makeText(DetailActivity.this, "Failed to delete file", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Failed to delete entry from the Firebase Realtime Database", e);
                Toast.makeText(DetailActivity.this, "Failed to delete entry", Toast.LENGTH_SHORT).show();
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, UploadActivity.class)
                        .putExtra("Title", detailTopic.getText().toString())
                        .putExtra("Description", detailDesc.getText().toString())
                        .putExtra("Episodes", detailLang.getText().toString())
                        .putExtra("Image", imageURL)
                        .putExtra("key", key);
                startActivity(intent);

            }
        });
    }
}
