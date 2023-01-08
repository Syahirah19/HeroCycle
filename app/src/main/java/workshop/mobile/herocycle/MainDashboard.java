package workshop.mobile.herocycle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import workshop.mobile.herocycle.model.RvItem;
import workshop.mobile.herocycle.model.RvNews;
import workshop.mobile.herocycle.rv.RvItemAdapter;
import workshop.mobile.herocycle.rv.RvNewsAdapter;

public class MainDashboard extends AppCompatActivity {

    RecyclerView rcViewItem, rcViewNews;
    private ImageView imgAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashboard);

        rcViewItem = findViewById(R.id.RcViewItem);
        rcViewNews = findViewById(R.id.RcViewNews);
        imgAccount = findViewById(R.id.imgAccount);

        item();
        news();


        //from image account go to edit profile image
        imgAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainDashboard.this,Account.class);
                startActivity(intent);
            }
        });

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

        item.add(new RvNews("Wholesale Price For New Inkjet Catridge", R.drawable.news1));
        item.add(new RvNews("Causes of Global Warming", R.drawable.news2waste));
        item.add(new RvNews("Let's Recycle Old Newspaper", R.drawable.news2waste));
        item.add(new RvNews("Recycle Your Used Cooking Oil", R.drawable.news2waste));
        item.add(new RvNews("Reduce, Reuse and Recycle", R.drawable.news2waste));

        rcViewNews.setAdapter(new RvNewsAdapter(this,item));
    }
}