package workshop.mobile.herocycle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class loginsignup extends AppCompatActivity {

    EditText edtEmail, edtPassword;
    TextView txtRegister, txtSkip;
    Button btn_Login, btn_skip;
    ImageView imgEyeClose;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginsignup);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);

        txtRegister = findViewById(R.id.txtRegister);

        txtSkip = findViewById(R.id.txtSkip);

        //    kalau nak gerak ke page lain
        txtRegister.setOnClickListener(
                view -> startActivity(new Intent(loginsignup.this, Register.class)));

        txtSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(loginsignup.this,MainDashboard.class));
            }
        });

    }

    public void goValidate(View view) {
        String email, password;

        email = String.valueOf(edtEmail.getText());
        password = String.valueOf(edtPassword.getText());

        db.collection("User")
                .whereEqualTo("email",email)
                .whereEqualTo("password",password)
                .get()
                .addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()){
                                for (QueryDocumentSnapshot document: task.getResult()){

                                    SharedPreferences.Editor editor = getSharedPreferences("UserPreferences",MODE_PRIVATE).edit();
                                    editor.putString("email",email);
                                    editor.apply();

                                    Intent intent = new Intent(this, MainDashboard.class);
                                    startActivity(intent);
                                    finish();
                                }
                                if (task.getResult().isEmpty()){
                                    Toast.makeText(loginsignup.this,"Email and Password is incorrect",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
    }
}