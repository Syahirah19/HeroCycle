package workshop.mobile.herocycle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LocationDelivery extends AppCompatActivity {

    TextView txtPrice, txtCategory;
    EditText edtWeight;
    ImageView imgWaste;
    Spinner spinnerMethod;
    Spinner sp_parent, sp_child;

    ArrayList<String> arrayList_parent, arrayList_method;
    ArrayAdapter<String> arrayAdapter_parent, arrayAdapter_child, arrayAdapter_method;

    String[] arrayList_KL;
    String[] arrayList_Putrajaya;
    String[] arrayList_Penang;
    String[] arrayList_Selangor;
    String[] arrayList_NegeriSembilan;
    String[] arrayList_Melaka;
    String[] arrayList_Johor;
    String[] arrayList_Pahang;
    String[] arrayList_Terengganu;
    String[] arrayList_Kelantan;
    String[] arrayList_Sabah;
    String[] arrayList_Sarawak;
    String[] arrayList_Labuan;

    String clcID, itemID, itemPrice;
    float price;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_delivery);

        Bundle extras = getIntent().getExtras();
        clcID = extras.getString("clcID","");

        imgWaste = findViewById(R.id.imgWaste);
        txtCategory = findViewById(R.id.txtCategory);
        txtPrice = findViewById(R.id.txtPrice);

        edtWeight = findViewById(R.id.edtWeight);

        sp_parent=(Spinner)findViewById(R.id.spinnerState);
        sp_child=(Spinner)findViewById(R.id.spinnerRegion);

        spinnerMethod = (Spinner) findViewById(R.id.spinnerMethod);

        db.collection("Collection").document(clcID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot.exists()){

                    itemID = documentSnapshot.get("itemID").toString();

                    db.collection("Item").document(itemID).get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()){
                            DocumentSnapshot documentSnapshot1 = task1.getResult();
                            if (documentSnapshot1.exists()){
                                txtCategory.setText(documentSnapshot1.get("itemName").toString());
                                itemPrice = documentSnapshot1.get("itemPrice").toString();
                            } else {
                                Toast.makeText(LocationDelivery.this,"Data is not available",Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LocationDelivery.this,"Error: "+task1.getException(),Toast.LENGTH_SHORT).show();
                        }
                    });

                    String link = documentSnapshot.getData().get("itemImage").toString();
                    Picasso.get().load(link).into(imgWaste);
                } else {
                    Toast.makeText(LocationDelivery.this,"Data is not available",Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(LocationDelivery.this,"Error: "+task.getException(),Toast.LENGTH_SHORT).show();
            }
        });

        stateRegionList();
        methodList();

        edtWeight.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i== EditorInfo.IME_ACTION_DONE){
                    price = Float.parseFloat(edtWeight.getText().toString()) * Float.parseFloat(itemPrice);
                    txtPrice.setText(Float.toString(price));
                }
                return false;
            }
        });
    }

    private void stateRegionList() {
        arrayList_parent = new ArrayList<>();
        arrayList_parent.add("Kuala Lumpur");
        arrayList_parent.add("Putrajaya");
        arrayList_parent.add("Pulau Pinang");
        arrayList_parent.add("Selangor");
        arrayList_parent.add("Negeri Sembilan");
        arrayList_parent.add("Melaka");
        arrayList_parent.add("Johor");
        arrayList_parent.add("Pahang");
        arrayList_parent.add("Terengganu");
        arrayList_parent.add("Kelantan");
        arrayList_parent.add("Sabah");
        arrayList_parent.add("Serawak");
        arrayList_parent.add("Labuan");
        arrayAdapter_parent = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList_parent);

        sp_parent.setAdapter(arrayAdapter_parent);

        arrayList_KL = getResources().getStringArray(R.array.RegionKualaLumpur);
        arrayList_Putrajaya = getResources().getStringArray(R.array.RegionPutrajaya);
        arrayList_Penang = getResources().getStringArray(R.array.RegionPulauPinang);
        arrayList_Selangor = getResources().getStringArray(R.array.RegionSelangor);
        arrayList_NegeriSembilan = getResources().getStringArray(R.array.RegionNegeriSembilan);
        arrayList_Melaka = getResources().getStringArray(R.array.RegionMelaka);
        arrayList_Johor = getResources().getStringArray(R.array.RegionJohor);
        arrayList_Pahang = getResources().getStringArray(R.array.RegionPahang);
        arrayList_Terengganu = getResources().getStringArray(R.array.RegionTerengganu);
        arrayList_Kelantan = getResources().getStringArray(R.array.RegionKelantan);
        arrayList_Sabah = getResources().getStringArray(R.array.RegionSabah);
        arrayList_Sarawak = getResources().getStringArray(R.array.RegionSarawak);
        arrayList_Labuan = getResources().getStringArray(R.array.RegionLabuan);

        sp_parent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0)
                {
                    arrayAdapter_child = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList_KL);
                }
                if (i == 1)
                {
                    arrayAdapter_child = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList_Putrajaya);
                }
                if (i == 2)
                {
                    arrayAdapter_child = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList_Penang);
                }
                if (i == 3)
                {
                    arrayAdapter_child = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList_Selangor);
                }
                if (i == 4)
                {
                    arrayAdapter_child = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList_NegeriSembilan);
                }
                if (i == 5)
                {
                    arrayAdapter_child = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList_Melaka);
                }
                if (i == 6)
                {
                    arrayAdapter_child = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList_Johor);
                }
                if (i == 7)
                {
                    arrayAdapter_child = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList_Pahang);
                }
                if (i == 8)
                {
                    arrayAdapter_child = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList_Terengganu);
                }
                if (i == 9)
                {
                    arrayAdapter_child = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList_Kelantan);
                }
                if (i == 10)
                {
                    arrayAdapter_child = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList_Sabah);
                }
                if (i == 11)
                {
                    arrayAdapter_child = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList_Sarawak);
                }
                if (i == 12)
                {
                    arrayAdapter_child = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList_Labuan);
                }

                sp_child.setAdapter(arrayAdapter_child);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void methodList() {
        arrayList_method = new ArrayList<>();
        arrayList_method.add("Pickup");
        arrayList_method.add("Delivery");
        arrayAdapter_method = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList_method);

        spinnerMethod.setAdapter(arrayAdapter_method);
    }

    private boolean validateWeight() {
        String val = edtWeight.getText().toString();
        if(val.isEmpty()){
            edtWeight.setError("Field cannot be empty");
            return false;
        } else {
            edtWeight.setError(null);
            return true;
        }
    }

    private boolean validateState() {
        String val = sp_parent.getSelectedItem().toString();
        if(val.isEmpty()){
            TextView errorText = (TextView)sp_parent.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Field cannot be empty");
            return false;
        } else {
            TextView errorText = (TextView)sp_parent.getSelectedView();
            errorText.setError(null);
            return true;
        }
    }

    private boolean validateRegion() {
        String val = sp_child.getSelectedItem().toString();
        if(val.isEmpty()){
            TextView errorText = (TextView)sp_child.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Field cannot be empty");
            return false;
        } else {
            TextView errorText = (TextView)sp_child.getSelectedView();
            errorText.setError(null);
            return true;
        }
    }

    private boolean validateMethod() {
        String val = spinnerMethod.getSelectedItem().toString();
        if(val.isEmpty()){
            TextView errorText = (TextView)spinnerMethod.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Field cannot be empty");
            return false;
        } else {
            TextView errorText = (TextView)spinnerMethod.getSelectedView();
            errorText.setError(null);
            return true;
        }
    }

    public void doNext(View view) {

        String state = sp_parent.getSelectedItem().toString();
        String region = sp_child.getSelectedItem().toString();

        if(validateWeight() && validateState() && validateRegion() && validateMethod()){
            DocumentReference documentReference = db.collection("Collection").document(clcID);
            documentReference.update(
                    "clcPrice", Float.toString(price),
                    "weight", edtWeight.getText().toString()
            );

            Intent intent = new Intent(LocationDelivery.this,deliveryDetails.class);
            intent.putExtra("clcID",clcID);
            intent.putExtra("state",state);
            intent.putExtra("region",region);

            startActivity(intent);

        }
    }
}