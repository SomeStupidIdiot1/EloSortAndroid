package come.thing.ranker.gallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import come.thing.ranker.R;

public class GalleryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Uri> filePaths = new ArrayList<>();
        Uri data = getIntent().getData();
        if (data != null)
            for (File file : new File(data.getPath()).listFiles())
                filePaths.add(Uri.fromFile(file));
        GalleryAdapter adapter = new GalleryAdapter(filePaths);
        recyclerView.setAdapter(adapter);
    }
}