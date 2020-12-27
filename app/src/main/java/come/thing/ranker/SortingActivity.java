package come.thing.ranker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SortingActivity extends AppCompatActivity {

    private File file;
    private JSONObject eloData;
    private String path;
    private List<String> fileNames = new ArrayList<>();
    private String firstImage;
    private String secondImage;
    private static final int JSON_READ_FAILURE = 1;
    private static final int NOT_ENOUGH_ITEMS = 2;
    private String exitMessage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorting);
        path = getIntent().getData().getPath();
        file = new File(path, PlayActivity.FILE_META_DATA_NAME);
        try {
            eloData = new JSONObject(new String(Files.readAllBytes(file.toPath())));
            if (eloData.length() <= 1) exitActivity(NOT_ENOUGH_ITEMS);
            else {
                eloData.keys().forEachRemaining(fileNames::add);
                makeRandomImages();
            }
        } catch (IOException | JSONException e) {
            Log.e("#rank", "Could not read from json file");
            exitActivity(JSON_READ_FAILURE);
        }
    }

    public void clickFirstImage(View view) {

    }

    public void clickSecondImage(View view) {
    }

    private void makeRandomImages() {
        Random random = new Random();
        firstImage = fileNames.get(random.nextInt(fileNames.size()));
        do {
            secondImage = fileNames.get(random.nextInt(fileNames.size()));
        } while (secondImage.equals(firstImage));
        ImageButton button1 = findViewById(R.id.imageButton3);
        ImageButton button2 = findViewById(R.id.imageButton);
        button1.setImageURI(Uri.fromFile(Paths.get(path, firstImage).toFile()));
        button2.setImageURI(Uri.fromFile(Paths.get(path, secondImage).toFile()));
    }

    public void done(View view) {
    }

    public void skip(View view) {
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!exitMessage.isEmpty()) {
            Toast.makeText(this,
                    exitMessage,
                    Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, PlayActivity.class);
            intent.setData(getIntent().getData());
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(3500);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        }
    }

    private void exitActivity(int code) {
        switch (code) {
            case JSON_READ_FAILURE:
                exitMessage = getResources().getString(R.string.something_bad);
                break;
            case NOT_ENOUGH_ITEMS:
                exitMessage = getResources().getString(R.string.need_more_than_one_item);
                break;
        }
    }
}