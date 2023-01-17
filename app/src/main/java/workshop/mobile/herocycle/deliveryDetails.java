package workshop.mobile.herocycle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

import workshop.mobile.herocycle.model.RvHistory;
import workshop.mobile.herocycle.rv.RvHistoryAdapter;

public class deliveryDetails extends AppCompatActivity {

    ImageView imgWaste;
    TextView txtPrice, txtWeight, txtCategory, ChooseDate;
    Spinner spinnerCenter;

    String clcID, itemID, region, state;
    ArrayList<String> arrayListCenter;
    ArrayAdapter<String> arrayAdapterCenter;

    Calendar calendar = Calendar.getInstance();
    final int year = calendar.get(Calendar.YEAR);
    final int month = calendar.get(Calendar.MONTH);
    final int day = calendar.get(Calendar.DAY_OF_MONTH);

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_details);

        Bundle extras = getIntent().getExtras();
        clcID = extras.getString("clcID","");
        region = extras.getString("region","");
        state = extras.getString("state","");

        imgWaste = findViewById(R.id.imgWaste);
        txtCategory = findViewById(R.id.txtCategory);
        txtWeight = findViewById(R.id.txtWeight);
        txtPrice = findViewById(R.id.txtPrice);

        spinnerCenter = (Spinner) findViewById(R.id.spinnerCenter);

        ChooseDate = findViewById(R.id.ChooseDate);

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
                            } else {
                                Toast.makeText(deliveryDetails.this,"Data is not available",Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(deliveryDetails.this,"Error: "+task1.getException(),Toast.LENGTH_SHORT).show();
                        }
                    });

                    txtWeight.setText(documentSnapshot.get("weight").toString());
                    txtPrice.setText(documentSnapshot.get("clcPrice").toString());

                    String link = documentSnapshot.getData().get("itemImage").toString();
                    Picasso.get().load(link).into(imgWaste);
                } else {
                    Toast.makeText(deliveryDetails.this,"Data is not available",Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(deliveryDetails.this,"Error: "+task.getException(),Toast.LENGTH_SHORT).show();
            }
        });

        centerList();

        ChooseDate.setInputType(0);
        ChooseDate.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(deliveryDetails.this, (datePicker, year, month, day) -> {
                month = month+1;
                String date = day + "/" + month + "/" + year;
                ChooseDate.setText(date);
            }, year, month, day);
            datePickerDialog.show();
        });
    }

    private void centerList() {
        Log.d("State",state);
        Log.d("Region",region);
        db.collection("Centre")
                .whereEqualTo("State",state)
                .whereEqualTo("Region",region)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        if (task.getResult().isEmpty()){
                            AlertDialog.Builder dialog = new AlertDialog.Builder(deliveryDetails.this);
                            dialog.setMessage("Recycle Centre in this area is not available.");
                            dialog.setTitle("Error");
                            dialog.setCancelable(true);
                            AlertDialog alertDialog = dialog.create();
                            alertDialog.show();
                        } else {
                            arrayListCenter = new ArrayList<>();
                            for (QueryDocumentSnapshot document: task.getResult()){
                                arrayListCenter.add(document.get("ctrName").toString());
                            }
                            arrayAdapterCenter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayListCenter);
                            spinnerCenter.setAdapter(arrayAdapterCenter);
                        }
                    }
                });
    }

    public void doConfirm(View view) {
        DocumentReference documentReference = db.collection("Collection").document(clcID);
        documentReference.update(
                "reqDate", ChooseDate.getText().toString()
        );

        AlertDialog.Builder dialog = new AlertDialog.Builder(deliveryDetails.this);
        dialog.setMessage("Data have been saved");
        dialog.setTitle("Success");
        dialog.setCancelable(true);
        dialog.setPositiveButton("Done", (dialogInterface, i) -> {
            Intent intent = new Intent(deliveryDetails.this,MainDashboard.class);
            startActivity(intent);
            finish();
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();

    }
}