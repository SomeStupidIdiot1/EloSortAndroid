package come.thing.ranker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

import come.thing.ranker.gallery.GalleryActivity;

public class EditListActivity extends AppCompatActivity {

    private final int PICK_IMAGE_CODE = 0;
    private final static int REQUEST_IMAGE_CAPTURE = 1;
    final int MAX_IMAGE_WIDTH = 1000;
    final static int IMAGE_COMPRESS_QUALITY = 60;
    public final static String PICTURES_DIRECTORY_NAME = "created_pictures";
    private Path tempDir;
    private File recentImgTaken;
    private Uri data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list);

        data = getIntent().getData();
        if (data == null) {
            tempDir = Paths.get(getFilesDir().getPath(), PICTURES_DIRECTORY_NAME, getString(R.string.unfinished_folder_name) + UUID.randomUUID().toString());
            setTitle(R.string.title_new_list);
        } else {
            TextView textView = findViewById(R.id.listNameText);
            tempDir = Paths.get(Objects.requireNonNull(data.getPath()));
            textView.setText(tempDir.getFileName().toString());
            textView.setEnabled(false);
            setTitle(R.string.title_edit);

        }
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
        if (data != null || !newFile.exists()) {
            boolean success = tempDir.toFile().renameTo(newFile);
            if (!success)
                Toast.makeText(this, R.string.something_bad, Toast.LENGTH_LONG).show();
            else {
                Intent intent = new Intent(this, PlayActivity.class);
                intent.setData(Uri.fromFile(newFile));
                startActivity(intent);
            }
        } else {
            if (listName.isEmpty())
                Toast.makeText(this, R.string.list_name_empty, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this, R.string.list_name_exists, Toast.LENGTH_LONG).show();

        }
    }

    public void takePicture(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            recentImgTaken = new File(tempDir.toFile(), UUID.randomUUID().toString() + ".jpeg");
            Uri uri = FileProvider.getUriForFile(this,
                    "come.thing.ranker.fileprovider",
                    recentImgTaken);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                    uri);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void lookAtGallery(View view) {
        Intent intent = new Intent(this, GalleryActivity.class);
        intent.setData(Uri.fromFile(tempDir.toFile()));
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int request, int result, Intent data) {
        try {
            if (result == RESULT_OK) {
                if (request == PICK_IMAGE_CODE && data != null) {
                    // After picture(s) selected
                    if (data.getData() != null)
                        imageDataToFile(data.getData());
                    else if (data.getClipData() != null) {
                        ClipData clipData = data.getClipData();
                        for (int i = 0; i < clipData.getItemCount(); i++)
                            imageDataToFile(clipData.getItemAt(i).getUri());
                    }
                } else if (request == REQUEST_IMAGE_CAPTURE) {
                    imageDataToFile(Uri.fromFile(recentImgTaken));
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
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        final int IMAGE_WIDTH = Math.min(dm.widthPixels, MAX_IMAGE_WIDTH);

        InputStream imageStream = getContentResolver().openInputStream(data);
        Bitmap src = BitmapFactory.decodeStream(imageStream);
        File file = new File(tempDir.toFile(), UUID.randomUUID().toString() + ".jpeg");
        FileOutputStream outputStream = new FileOutputStream(file);
        // Resize
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(src, IMAGE_WIDTH,
                IMAGE_WIDTH * src.getHeight() / src.getWidth(), true);
        // Output
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, IMAGE_COMPRESS_QUALITY, outputStream);
        // Clean up
        outputStream.flush();
        outputStream.close();
        resizedBitmap.recycle();
        src.recycle();
    }
}