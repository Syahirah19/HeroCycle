package workshop.mobile.herocycle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

import workshop.mobile.herocycle.model.RvItem;
import workshop.mobile.herocycle.model.RvNews;
import workshop.mobile.herocycle.rv.RvItemAdapter;
import workshop.mobile.herocycle.rv.RvNewsAdapter;

public class MainDashboard extends AppCompatActivity {

    RecyclerView rcViewItem, rcViewNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashboard);

        rcViewItem = findViewById(R.id.RcViewItem);
        rcViewNews = findViewById(R.id.RcViewNews);

        item();
        news();

    }

    private void item() {
        rcViewItem.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        ArrayList<RvItem> item = new ArrayList<>();

        item.add(new RvItem("Plastic", R.drawable.recycleplasticicon));
        item.add(new RvItem("Glass", R.drawable.recycleglassicon));
        item.add(new RvItem("Paper", R.drawable.recyclepapericon));
        item.add(new RvItem("Others", R.drawable.recycleicon));

        rcViewItem.setAdapter(new RvItemAdapter(this,item));
    }

    private void news() {
        rcViewNews.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        ArrayList<RvNews> item = new ArrayList<>();

        item.add(new RvNews("News 1","Hello"));

        rcViewItem.setAdapter(new RvNewsAdapter(this,item));
    }
}