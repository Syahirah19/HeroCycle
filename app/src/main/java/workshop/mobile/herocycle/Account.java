package workshop.mobile.herocycle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import workshop.mobile.herocycle.model.User;

public class Account extends AppCompatActivity {

    TextView txtName, txtPoint, txtBirthdate, txtEmail, txtTextPhone;
    Button btnHistory,btnEdit;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storage;
    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        SharedPreferences prefs = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        String userID = prefs.getString("uid","");

        txtName = findViewById(R.id.txtName);
        txtPoint = findViewById(R.id.txtPoint);
        txtBirthdate = findViewById(R.id.txtBirthdate);
        txtEmail = findViewById(R.id.txtEmail);
        txtTextPhone = findViewById(R.id.txtTextPhone);

        btnHistory = findViewById(R.id.btnHistory);
        btnEdit = findViewById(R.id.btnEdit);

        db.collection("User").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                     DocumentSnapshot documentSnapshot = task.getResult();
                     if (documentSnapshot.exists()){
                         txtName.setText(documentSnapshot.get("fullname").toString());
                         txtPoint.setText(documentSnapshot.get("point").toString()+" points");
                         txtBirthdate.setText(documentSnapshot.get("birthdate").toString());
                         txtEmail.setText(documentSnapshot.get("email").toString());
                         txtTextPhone.setText(documentSnapshot.get("mobile").toString());
                     } else {
                         Toast.makeText(Account.this,"Data is not available",Toast.LENGTH_SHORT).show();
                     }
                } else {
                    Toast.makeText(Account.this,"Error: "+task.getException(),Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnHistory.setOnClickListener(
                view -> startActivity(new Intent(Account.this, RecycleHistory.class)));

        btnEdit.setOnClickListener(
                view -> startActivity(new Intent(Account.this, editAccount.class)));

    }}