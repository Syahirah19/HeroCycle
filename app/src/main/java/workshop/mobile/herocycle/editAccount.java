package workshop.mobile.herocycle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class editAccount extends AppCompatActivity {

    EditText edtName, edtEmail, edtAddress, edtTextPhone, edtBirthdate;
    Button btnSave, btnPayment, btnPoint;
    ImageView imgUser;

    String uid;
    Calendar calendar = Calendar.getInstance();
    final int year = calendar.get(Calendar.YEAR);
    final int month = calendar.get(Calendar.MONTH);
    final int day = calendar.get(Calendar.DAY_OF_MONTH);

    private Uri mImageUri;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        SharedPreferences prefs = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        String UserEmail = prefs.getString("email","");

        storage = FirebaseStorage.getInstance().getReference("Users/");

        imgUser = findViewById(R.id.imgUser);

        edtName = findViewById(R.id.edtName);
        edtBirthdate = findViewById(R.id.edtBirthdate);
        edtEmail = findViewById(R.id.edtEmail);
        edtAddress = findViewById(R.id.edtAddress);
        edtTextPhone = findViewById(R.id.edtTextPhone);

        btnSave = findViewById(R.id.btnSave);

        imgUser.setOnClickListener(view -> goUpload());

        edtBirthdate.setInputType(0);
        edtBirthdate.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(editAccount.this, (datePicker, year, month, day) -> {
                month = month+1;
                String date = day + "/" + month + "/" + year;
                edtBirthdate.setText(date);
            }, year, month, day);
            datePickerDialog.show();
        });

        db.collection("User")
                .whereEqualTo("email",UserEmail)
                .get()
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()){
                                for (QueryDocumentSnapshot document: task.getResult()){
                                    uid = document.getId();
                                    edtName.setText(document.get("fullname").toString());
                                    edtBirthdate.setText(document.get("birthdate").toString());
                                    edtAddress.setText(document.get("homeAddress").toString());
                                    edtEmail.setText(document.get("email").toString());
                                    edtTextPhone.setText(document.get("mobile").toString());

                                    String link = document.getData().get("imageURL").toString();
                                    Picasso.get().load(link).into(imgUser);
                                }
                            }
                        }
                );

    }

    public void goUpload() {
        Intent photo = new Intent();
        photo.setType("image/*");
        photo.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(photo,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).fit().centerCrop().into(imgUser);
        }
    }

    public boolean validateName(){
        String val = edtName.getText().toString();
        if(val.isEmpty()){
            edtName.setError("Field cannot be empty");
            return false;
        } else {
            edtName.setError(null);
            return true;
        }
    }

    public boolean validateAddress(){
        String val = edtAddress.getText().toString();
        if(val.isEmpty()){
            edtAddress.setError("Field cannot be empty");
            return false;
        } else {
            edtAddress.setError(null);
            return true;
        }
    }

    public boolean validateEmail(){
        String val = edtEmail.getText().toString();
        if(val.isEmpty()){
            edtEmail.setError("Field cannot be empty");
            return false;
        } else {
            edtEmail.setError(null);
            return true;
        }
    }

    public boolean validatePhone(){
        String val = edtTextPhone.getText().toString();
        if(val.isEmpty()){
            edtTextPhone.setError("Field cannot be empty");
            return false;
        } else {
            edtTextPhone.setError(null);
            return true;
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public void goSave(View view) {

        if (validateName() && validateAddress() && validateEmail() && validatePhone()){
            if(mImageUri != null){
                StorageReference fileReference = storage.child(uid+"."+getFileExtension(mImageUri));

                // Update commands
                StorageTask<UploadTask.TaskSnapshot> mUploadTask = fileReference.putFile(mImageUri)
                        .addOnSuccessListener(taskSnapshot -> taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(task -> {
                            // Update commands
                            DocumentReference documentReference = db.collection("User").document(uid);
                            documentReference.update(
                                    "fullname", edtName.getText().toString(),
                                    "birthdate", edtBirthdate.getText().toString(),
                                    "homeAddress", edtAddress.getText().toString(),
                                    "email", edtEmail.getText().toString(),
                                    "mobile", edtTextPhone.getText().toString(),
                                    "imageURL", task.getResult().toString()
                            );

                            AlertDialog.Builder dialog = new AlertDialog.Builder(editAccount.this);
                            dialog.setMessage("Data have been saved");
                            dialog.setTitle("Success");
                            dialog.setCancelable(true);
                            AlertDialog alertDialog = dialog.create();
                            alertDialog.show();
                        }));
            } else {
                // Update commands
                DocumentReference documentReference = db.collection("User").document(uid);
                documentReference.update(
                        "fullname",edtName.getText().toString(),
                        "birthdate",edtBirthdate.getText().toString(),
                        "homeAddress",edtAddress.getText().toString(),
                        "email",edtEmail.getText().toString(),
                        "mobile",edtTextPhone.getText().toString()
                );

                AlertDialog.Builder dialog = new AlertDialog.Builder(editAccount.this);
                dialog.setMessage("Data have been saved");
                dialog.setTitle("Success");
                dialog.setCancelable(true);
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }


        }

    }

}