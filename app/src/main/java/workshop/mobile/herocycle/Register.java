package workshop.mobile.herocycle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import workshop.mobile.herocycle.model.User;

public class Register extends AppCompatActivity {

    EditText edtEmail, edtPassword, edtTextPhone;
    TextView txtTerms;
    Button btnContinue;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtTextPhone = findViewById(R.id.edtTextPhone);
        btnContinue = findViewById(R.id.btnContinue);
        txtTerms = findViewById(R.id.txtTerms);

        //from terms and condition text in register page, move to read the t&d page
        txtTerms.setOnClickListener(
                view -> startActivity(new Intent(Register.this, TermsCondition.class))
        );
    }

    private boolean validateEmail() {
        String val = edtEmail.getText().toString();
        String regex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()){
            edtEmail.setError("field cannot be empty");
            return false;
        }
        else if (!val.matches(regex)){
            edtEmail.setError("Invalid email address");
            return false;
        }
        else {
            edtEmail.setError(null);
            return true;
        }
    }

    public boolean validatePassword(){
        String val = edtPassword.getText().toString();
        if (val.isEmpty()){
            edtPassword.setError("field cannot be empty");
            return false;
        }
        else if (!(val.length() ==8)){
            edtPassword.setError("password length is 8 character");
            return false;
        }
        else {
            edtPassword.setError(null);
            return true;
        }
    }

    private boolean validatePhone() {
        String val = edtTextPhone.getText().toString();
        if (val.isEmpty()){
            edtTextPhone.setError("field cannot be empty");
            return false;
        }
        else {
            edtTextPhone.setError(null);
            return true;
        }
    }

    public void goAccount(View view) {
        if(validateEmail() && validatePassword() && validatePhone()){
            RegisterUser();
        }
    }

    private void RegisterUser() {
        user.setEmail(edtEmail.getText().toString());
        user.setMobile(edtTextPhone.getText().toString());
        user.setPassword(edtPassword.getText().toString());
        user.setFullname("");
        user.setHomeAddress("");
        user.setBirthdate("");
        user.setImageURL("https://firebasestorage.googleapis.com/v0/b/herocycle-f74c8.appspot.com/o/Users%2FuserProfile.jpg?alt=media&token=a4b3543d-201f-4769-a10b-296a9428959a");

        db.collection("User")
                .whereEqualTo("email",user.getEmail())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot document: task.getResult()){
                            // To make pop up at bottom
                            Toast.makeText(Register.this,"Email Already Register", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        db.collection("User").add(user).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                Toast.makeText(Register.this,"Successfully Registered", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Register.this, MainDashboard.class));
                            }
                        });
                    }
                });
    }
}