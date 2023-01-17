package workshop.mobile.herocycle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import workshop.mobile.herocycle.model.RvCentre;
import workshop.mobile.herocycle.model.RvItem;
import workshop.mobile.herocycle.rv.RvCenterAdapter;
import workshop.mobile.herocycle.rv.RvItemAdapter;

public class centre extends AppCompatActivity {

    Spinner spinnerCenter;
    RecyclerView RcViewCentre;

    String states;

    ArrayList<String> arrayList_state;
    ArrayAdapter<String> arrayAdapter_state;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centre);

        spinnerCenter = findViewById(R.id.spinnerCenter);
        RcViewCentre = findViewById(R.id.RcViewCentre);
        
        stateList();
        listCenter();

    }

    private void stateList() {
        arrayList_state = new ArrayList<>();
        arrayList_state.add("Kuala Lumpur");
        arrayList_state.add("Putrajaya");
        arrayList_state.add("Pulau Pinang");
        arrayList_state.add("Selangor");
        arrayList_state.add("Negeri Sembilan");
        arrayList_state.add("Melaka");
        arrayList_state.add("Johor");
        arrayList_state.add("Pahang");
        arrayList_state.add("Terengganu");
        arrayList_state.add("Kelantan");
        arrayList_state.add("Sabah");
        arrayList_state.add("Serawak");
        arrayList_state.add("Labuan");
        arrayAdapter_state = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList_state);

        spinnerCenter.setAdapter(arrayAdapter_state);
    }

    private void listCenter() {

        spinnerCenter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0)
                {
                    states = "Kuala Lumpur";
                }
                if (i == 1)
                {
                    states = "Putrajaya";
                }
                if (i == 2)
                {
                    states = "Pulau Pinang";
                }
                if (i == 3)
                {
                    states = "Selangor";
                }
                if (i == 4)
                {
                    states = "Negeri Sembilan";
                }
                if (i == 5)
                {
                    states = "Melaka";
                }
                if (i == 6)
                {
                    states = "Johor";
                }
                if (i == 7)
                {
                    states = "Pahang";
                }
                if (i == 8)
                {
                    states = "Terengganu";
                }
                if (i == 9)
                {
                    states = "Kelantan";
                }
                if (i == 10)
                {
                    states = "Sabah";
                }
                if (i == 11)
                {
                    states = "Sarawak";
                }
                if (i == 12)
                {
                    states = "Labuan";
                }

                toDatabase(states);
            }

            private void toDatabase(String states) {
                db.collection("Centre")
                        .whereEqualTo("State",states)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                RcViewCentre.setLayoutManager(new LinearLayoutManager(centre.this,LinearLayoutManager.VERTICAL,false));

                                ArrayList<RvCentre> item = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()){
                                    Log.d("Data",document.get("Region").toString());
                                    item.add(new RvCentre(document.get("FullAdd").toString(),document.get("Region").toString(),document.get("State").toString(),document.get("ctrName").toString()));
                                }
                                RcViewCentre.setAdapter(new RvCenterAdapter(centre.this,item));
                            }
                        });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}