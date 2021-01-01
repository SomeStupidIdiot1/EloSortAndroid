package come.thing.ranker.gallery;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import come.thing.ranker.EditListActivity;
import come.thing.ranker.R;

public class GalleryActivity extends AppCompatActivity {
    private GalleryAdapter adapter;
    private List<GalleryItem> filePaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        filePaths = new ArrayList<>();
        Uri data = getIntent().getData();
        if (data != null)
            for (File file : Objects.requireNonNull(new File(data.getPath()).listFiles()))
                filePaths.add(new GalleryItem(Uri.fromFile(file)));
        adapter = new GalleryAdapter(filePaths);
        recyclerView.setAdapter(adapter);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, EditListActivity.class)
                        .setData(getIntent().getData()));
                return true;
            case R.id.action_delete:
                int count = adapter.getSelectedIndicesReverse().size();
                for (int index : adapter.getSelectedIndicesReverse()) {
                    GalleryItem galleryItem = filePaths.get(index);
                    new File(galleryItem.getUri().getPath()).delete();
                    filePaths.remove(index);
                    adapter.notifyItemRemoved(index);
                    adapter.notifyItemRangeChanged(index, filePaths.size());
                }
                String txt = count + " " + getResources().getString(R.string.items_deleted_desc);
                Toast.makeText(this, txt, Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }
}
