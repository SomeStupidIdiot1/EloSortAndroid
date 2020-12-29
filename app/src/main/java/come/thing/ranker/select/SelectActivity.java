package come.thing.ranker.select;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.nio.file.Paths;


import come.thing.ranker.EditListActivity;
import come.thing.ranker.R;

public class SelectActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        File file = Paths.get(getFilesDir().getPath(), EditListActivity.PICTURES_DIRECTORY_NAME).toFile();

        SelectAdapter adapter = new SelectAdapter(file.listFiles());
        recyclerView.setAdapter(adapter);
    }
}