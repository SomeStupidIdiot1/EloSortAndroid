package come.thing.ranker.gallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import come.thing.ranker.R;

public class GalleryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<GalleryItem> filePaths = new ArrayList<>();
        Uri data = getIntent().getData();
        if (data != null)
            for (File file : Objects.requireNonNull(new File(data.getPath()).listFiles()))
                filePaths.add(new GalleryItem(Uri.fromFile(file)));
        ImageButton delButton = findViewById(R.id.imageButton2);
        GalleryAdapter adapter = new GalleryAdapter(filePaths, delButton);
        recyclerView.setAdapter(adapter);
        delButton.setOnClickListener(view -> {
            int count = adapter.getSelectedIndicesReverse().size();
            for (int index : adapter.getSelectedIndicesReverse()) {
                GalleryItem item = filePaths.get(index);
                new File(item.getUri().getPath()).delete();
                filePaths.remove(index);
                adapter.notifyItemRemoved(index);
                adapter.notifyItemRangeChanged(index, filePaths.size());
            }

            String txt = count + " " + getResources().getString(R.string.items_deleted_desc);
            Toast.makeText(this, txt, Toast.LENGTH_LONG).show();
        });
    }
}