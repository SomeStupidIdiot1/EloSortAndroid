package come.thing.ranker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class EditList extends AppCompatActivity {

    private final int PICK_IMAGE_CODE = 1;
    public final static int IMAGE_WIDTH = 500;
    public final static int IMAGE_COMPRESS_QUALITY = 60;
    public final static String PICTURES_DIRECTORY_NAME = "created_pictures";
    private Path tempDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list);
        tempDir = Paths.get(getFilesDir().getPath(), PICTURES_DIRECTORY_NAME, getString(R.string.unfinished_folder_name) + UUID.randomUUID().toString());
        tempDir.toFile().mkdirs();
    }

    public void selectPictures(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGE_CODE);
    }

    public void done(View view) {
        String listName = ((TextView) findViewById(R.id.listNameText)).getText().toString().trim();
        File newFile = Paths.get(getFilesDir().getPath(), PICTURES_DIRECTORY_NAME, listName).toFile();
        if (!newFile.exists()) {
            boolean success = tempDir.toFile().renameTo(newFile);
            if (!success)
                Toast.makeText(this, R.string.something_bad, Toast.LENGTH_LONG).show();
            else {
                Intent intent = new Intent(this, PlayActivity.class);
                intent.setData(Uri.fromFile(newFile));
                startActivity(intent);
            }
        } else
            Toast.makeText(this, R.string.list_name_exists, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int request, int result, Intent data) {
        try {
            if (request == PICK_IMAGE_CODE && result == RESULT_OK && data != null) {
                if (data.getData() != null)
                    imageDataToFile(data.getData());
                else if (data.getClipData() != null) {
                    ClipData clipData = data.getClipData();
                    for (int i = 0; i < clipData.getItemCount(); i++)
                        imageDataToFile(clipData.getItemAt(i).getUri());
                }
            } else
                Toast.makeText(this, R.string.nothing_selected, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, R.string.something_bad, Toast.LENGTH_LONG).show();
            //noinspection ConstantConditions
            Log.e("IO", e.getMessage());
        }
        super.onActivityResult(request, result, data);
    }

    private void imageDataToFile(Uri data) throws IOException {
        InputStream imageStream = getContentResolver().openInputStream(data);
        Bitmap src = BitmapFactory.decodeStream(imageStream);
        File file = new File(tempDir.toFile(), UUID.randomUUID().toString() + ".jpeg");
        FileOutputStream outputStream = new FileOutputStream(file);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(src, IMAGE_WIDTH,
                IMAGE_WIDTH * src.getHeight() / src.getWidth(), true);
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, IMAGE_COMPRESS_QUALITY, outputStream);

        outputStream.flush();
        outputStream.close();
        resizedBitmap.recycle();
        src.recycle();
    }

}