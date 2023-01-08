package workshop.mobile.herocycle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import workshop.mobile.herocycle.model.User;

public class StartPage extends AppCompatActivity {

    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);

        new Handler().postDelayed(() -> {
            SharedPreferences prefs = getSharedPreferences("UserPreferences", MODE_PRIVATE);
            String username = prefs.getString("email", user.getEmail());
            Intent intent = null;

            if (username != null){
                intent = new Intent(this,MainDashboard.class);
            } else {
                intent = new Intent(this,loginsignup.class);
            }

            startActivity(intent);
            finish();
        },2500);
    }

    public void goToLogin(View view) {

        Intent intent = new Intent(this,loginsignup.class);

        startActivity(intent);
        finish();
    }
}