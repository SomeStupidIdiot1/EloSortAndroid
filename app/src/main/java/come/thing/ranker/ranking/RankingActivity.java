package come.thing.ranker.ranking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import come.thing.ranker.PlayActivity;
import come.thing.ranker.R;

public class RankingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Uri> filePaths = new ArrayList<>();
        List<String[]> data = new ArrayList<>();
        String path = getIntent().getData().getPath();
        File file = new File(path, PlayActivity.FILE_META_DATA_NAME);
        try {
            JSONObject eloData = new JSONObject(new String(Files.readAllBytes(file.toPath())));
            for (Iterator<String> it = eloData.keys(); it.hasNext(); ) {
                String key = it.next();
                data.add(new String[]{eloData.getInt(key) + "", key});
            }
            data.sort((a, b) ->
                    Integer.compare(Integer.parseInt(b[0]), Integer.parseInt(a[0]))
            );
        } catch (IOException | JSONException e) {
            Log.e("#rank", "Could not read from json file");
        }
        for (String[] item : data) {
            String fileName = item[1];
            filePaths.add(Uri.fromFile(new File(path, fileName)));
        }
        RankingAdapter adapter = new RankingAdapter(filePaths);
        recyclerView.setAdapter(adapter);
    }
}