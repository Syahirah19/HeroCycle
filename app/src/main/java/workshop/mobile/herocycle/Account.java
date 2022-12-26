package workshop.mobile.herocycle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class Account extends AppCompatActivity {

    EditText edtName;
    EditText edtBirthdate;
    EditText edtEmail;
    EditText edtAddress;
    EditText edtTextPhone;
    Button btnSave;
    ImageView imgArrow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
    }
}