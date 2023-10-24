package com.example.demont118;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class firestore_dtb extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firestore_dtb);

        EditText edit_id = (EditText) findViewById(R.id.edit_id);
        EditText edit_name = (EditText) findViewById(R.id.edit_name);
        Button btn_send = (Button) findViewById(R.id.btn_send);
        Button btn_refresh = (Button) findViewById(R.id.btn_refresh);
        ListView list_item = (ListView) findViewById(R.id.list_item);

        List<String> dataList = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        list_item.setAdapter(adapter);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserTest userTest = new UserTest(edit_id.getText().toString(), edit_name.getText().toString());

                db.collection("users").document(userTest.getId()).set(userTest).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            Toast.makeText(firestore_dtb.this, "Send successes", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(firestore_dtb.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataList.clear();
                db.collection("users")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        //Log.d("Cloud Firestore", document.getId() + " => " + document.getData());
                                        dataList.add(document.getId() + " => " + document.getData());
                                    }
                                    adapter.notifyDataSetChanged();
                                } else {
                                    //Log.w("Cloud Firestore", "Error getting documents.", task.getException());
                                    Toast.makeText(firestore_dtb.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        list_item.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String _id = dataList.get(position);
                _id = _id.substring(0, _id.indexOf(" "));
                db.collection("users").document(_id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(firestore_dtb.this, "Delete successes", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(firestore_dtb.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                return true;
            }
        });
    }
}