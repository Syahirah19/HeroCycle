package workshop.mobile.herocycle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    EditText edtEmail, edtPassword, edtTextPhone;
    TextView txtTerms;
    Button btnContinue;


    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtTextPhone = findViewById(R.id.edtTextPhone);
        btnContinue = findViewById(R.id.btnContinue);
        txtTerms = findViewById(R.id.txtTerms);
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("User");

        myRef.setValue("Hello World");

    //from terms and condition text in register page, move to read the t&d page
        txtTerms.setOnClickListener(
                view -> startActivity(new Intent(Register.this, TermsCondition.class)));
    }

    public void goAccount(View view) {
        startActivity(new Intent(this, Account.class));
    }
}