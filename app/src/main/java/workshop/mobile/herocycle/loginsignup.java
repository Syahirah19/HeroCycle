package workshop.mobile.herocycle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class loginsignup extends AppCompatActivity {

    EditText edtEmail, edtPassword;
    TextView txtRegister;
    Button btn_Login;
    ImageView imgEyeClose;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginsignup);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);

        txtRegister = findViewById(R.id.txtRegister);

        //    kalau nak gerak ke page lain
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(loginsignup.this, Register.class));
            }
        });

    }
}