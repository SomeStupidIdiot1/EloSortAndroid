package come.thing.ranker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import come.thing.ranker.select.SelectActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onCreateList(View view){
        Intent intent = new Intent(this, EditListActivity.class);
        startActivity(intent);
    }
    public void onOpenExistingList(View view){
        Intent intent = new Intent(this, SelectActivity.class);
        startActivity(intent);
    }
}