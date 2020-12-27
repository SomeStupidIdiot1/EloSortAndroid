package come.thing.ranker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;

public class PlayActivity extends AppCompatActivity {
    public static final String FILE_META_DATA_NAME = "data.json";
    public static final int DEFAULT_ELO = 1000;
    private Uri data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        data = getIntent().getData();
        if (data == null)
            startActivity(new Intent(this, EditListActivity.class));
        else {
            ensureEloDataExists();
        }
    }

    @Override
    public void onBackPressed() {
    }

    public void editList(View view) {
        Intent intent = new Intent(this, EditListActivity.class);
        intent.setData(data);
        startActivity(intent);
    }

    public void showRank(View view) {
        Intent intent = new Intent(this, RankingActivity.class);
        intent.setData(data);
        startActivity(intent);
    }

    public void sortList(View view) {
        Intent intent = new Intent(this, SortingActivity.class);
        intent.setData(data);
        startActivity(intent);
    }

    private void ensureEloDataExists() {
        File file = Paths.get(data.getPath(), FILE_META_DATA_NAME).toFile();
        JSONObject eloData = new JSONObject();
        if (!file.exists()) {
            try {
                for (File f : Paths.get(data.getPath()).toFile().listFiles()) {
                    if (!f.getName().equals(FILE_META_DATA_NAME))
                        eloData.put(f.getName(), DEFAULT_ELO);
                }

            } catch (JSONException e) {
                Log.e("#elo", "Error in making new json file");
            }
        } else {
            try {
                eloData = new JSONObject(new String(Files.readAllBytes(file.toPath())));
                HashSet<String> fileNames = new HashSet<>();
                for (File f : Paths.get(data.getPath()).toFile().listFiles()) {
                    if (!f.getName().equals(FILE_META_DATA_NAME))
                        fileNames.add(f.getName());
                }
                for (Iterator<String> it = eloData.keys(); it.hasNext(); ) {
                    String key = it.next();
                    if (!fileNames.contains(key))
                        it.remove();
                }
                for (String fileName : fileNames)
                    if (!eloData.has(fileName)) {
                        eloData.put(fileName, DEFAULT_ELO);
                    }
            } catch (IOException | JSONException e) {
                Log.e("#elo", "Could not read from json file");
            }
        }
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(eloData.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            Log.e("#elo", "Could not write to json file");
        }
    }
}