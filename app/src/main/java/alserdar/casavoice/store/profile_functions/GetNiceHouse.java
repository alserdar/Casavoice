package alserdar.casavoice.store.profile_functions;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import alserdar.casavoice.R;

public class GetNiceHouse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_nice_house);
        ActionBar actionBar =getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
