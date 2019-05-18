package freedom.com.freedom_e_learning;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;


public class AboutUsActivity extends AppCompatActivity {
    private Toolbar AUtoolbar;
    GridLayout grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        setControl();
        gridclick(grid);
    }

    private void setControl() {
        AUtoolbar = findViewById(R.id.AboutUsToolbar);
        AUtoolbar.setTitle(String.format("About Us"));
        setSupportActionBar(AUtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        grid = (GridLayout) findViewById(R.id.grid);
    }

    private void gridclick(GridLayout grid) {
        for (int i = 0; i < grid.getChildCount(); i++) {
            CardView cardView = (CardView) grid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(AboutUsActivity.this, "clicked at index" + finalI, Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
