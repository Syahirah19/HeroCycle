package workshop.mobile.herocycle;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Date;

public class Account extends AppCompatActivity {

    EditText edtName, edtEmail, edtAddress, edtTextPhone;
    DatePicker edtBirthdate;
    Button btnSave;
    ImageView imgArrow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        edtName = findViewById(R.id.edtName);
//      edtBirthdate = findViewById(R.id.edtBirthdate);
        edtEmail = findViewById(R.id.edtEmail);
        edtAddress = findViewById(R.id.edtAddress);
        edtTextPhone = findViewById(R.id.edtTextPhone);

        btnSave = findViewById(R.id.btnSave);


    }
}