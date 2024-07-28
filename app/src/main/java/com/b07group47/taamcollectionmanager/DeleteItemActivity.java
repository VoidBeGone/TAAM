package com.b07group47.taamcollectionmanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class DeleteItemActivity extends BaseActivity {
    private int lot;
    private FirebaseFirestore db;
    private Button buttonDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        getLotFromintent();
        initButtons();
    }


    private void initButtons(){
        buttonDelete = findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(v -> deleteItemByTitle());
    }


    private void getLotFromintent(){
        lot = getIntent().getIntExtra("LOT", -1);
    }


    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_delete_item;
    }


    private void backtoMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void deleteItemByTitle() {
        if (this.lot == -1) {
            Toast.makeText(DeleteItemActivity.this, "Invalid lot ID", Toast.LENGTH_SHORT).show();
            backtoMain();
            return;
        }

        Query query = db.collection("artifactData").whereEqualTo("lot", this.lot);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                // Assuming there is only one document with the given lot value
                task.getResult().getDocuments().get(0).getReference().delete()
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(DeleteItemActivity.this, "Item deleted successfully", Toast.LENGTH_SHORT).show();
                            backtoMain();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(DeleteItemActivity.this, "Error deleting item", Toast.LENGTH_SHORT).show();
                            backtoMain();
                        });
            } else {
                Toast.makeText(DeleteItemActivity.this, "Item not found", Toast.LENGTH_SHORT).show();
                backtoMain();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(DeleteItemActivity.this, "Error querying item", Toast.LENGTH_SHORT).show();
            backtoMain();
        });
    }

}