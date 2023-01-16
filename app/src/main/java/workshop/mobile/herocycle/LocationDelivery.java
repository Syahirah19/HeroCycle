package workshop.mobile.herocycle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class LocationDelivery extends AppCompatActivity {

    TextView TxtPrice, TxtWeight, txtCategory;
    Spinner spinnerState, spinnerRegion, spinnerMethod;
    Button btnNext;
    ImageView imgPeople;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_delivery);

        spinnerState=(Spinner)findViewById(R.id.spinnerState);
        spinnerRegion=(Spinner)findViewById(R.id.spinnerRegion);
        btnNext = findViewById(R.id.btnNext);


        btnNext.setOnClickListener(
                view1 -> startActivity(new Intent(LocationDelivery.this, deliveryDetails.class)));
    }
}