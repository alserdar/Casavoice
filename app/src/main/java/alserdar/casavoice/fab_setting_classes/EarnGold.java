package alserdar.casavoice.fab_setting_classes;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import alserdar.casavoice.R;

public class EarnGold extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earn_gold);
        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.hide();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
