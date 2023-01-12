package workshop.mobile.herocycle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.Date;

import workshop.mobile.herocycle.model.User;

public class Account extends AppCompatActivity {

    EditText edtName, edtEmail, edtAddress, edtTextPhone;
    DatePicker edtBirthdate;
    Button btnSave;
    ImageView imgPic;
    TextView txtLogout;

    String uid;
    Uri mImageUri;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        SharedPreferences prefs = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        String UserEmail = prefs.getString("email","");

        imgPic = findViewById(R.id.imgUser);

        edtName = findViewById(R.id.edtName);
//      edtBirthdate = findViewById(R.id.edtBirthdate);
        edtEmail = findViewById(R.id.edtEmail);
        edtAddress = findViewById(R.id.edtAddress);
        edtTextPhone = findViewById(R.id.edtTextPhone);

        btnSave = findViewById(R.id.btnSave);
        txtLogout = findViewById(R.id.txtLogout);

        db.collection("User")
                .whereEqualTo("email",UserEmail)
                .get()
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()){
                                for (QueryDocumentSnapshot document: task.getResult()){
                                    uid = document.getId();
                                    edtName.setText(document.get("fullname").toString());
                                    edtAddress.setText(document.get("homeAddress").toString());
                                    edtEmail.setText(document.get("email").toString());
                                    edtTextPhone.setText(document.get("mobile").toString());
                                }
                            }
                        }
                );

    }

    public void goUpload(View view) {
        Intent photo = new Intent();
        photo.setType("image/*");
        photo.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(photo,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && requestCode == RESULT_OK && data != null && data.getData() != null){
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(imgPic);
        }
    }

    public void goSave(View view) {
        // Update commands
        DocumentReference documentReference = db.collection("User").document(uid);
        documentReference.update(
                "fullname",edtName.getText().toString(),
                "homeAddress",edtAddress.getText().toString(),
                "email",edtEmail.getText().toString(),
                "mobile",edtTextPhone.getText().toString()
        );

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Data have been saved");
        dialog.setTitle("Success");
        dialog.setCancelable(true);
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();

    }

    public void goLogout(View view) {

        SharedPreferences.Editor editor = getSharedPreferences("UserPreferences",MODE_PRIVATE).edit();
        editor.clear().apply();

        Intent intent = new Intent(Account.this, loginsignup.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}