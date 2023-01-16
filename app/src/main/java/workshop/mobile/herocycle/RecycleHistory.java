package workshop.mobile.herocycle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import workshop.mobile.herocycle.model.RvHistory;
import workshop.mobile.herocycle.rv.RvHistoryAdapter;

public class RecycleHistory extends AppCompatActivity {
    RecyclerView rcViewHistory;

    String userID;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_history);

        SharedPreferences prefs = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        userID = prefs.getString("uid","");

        rcViewHistory = findViewById(R.id.RcViewHistory);

        history();
    }

    private void history() {
        rcViewHistory.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));

        ArrayList<RvHistory> item = new ArrayList<>();

        db.collection("Collection")
                .whereEqualTo("userID",userID)
                .whereEqualTo("clcStatus","collected")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot document: task.getResult()){
                            item.add(new RvHistory(document.get("reqDate").toString(),document.get("clcDate").toString(),document.get("clcPrice").toString()));
                        }
                        rcViewHistory.setAdapter(new RvHistoryAdapter(this,item));
                    }
                });

    }
}