package come.thing.ranker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.ImageCapture;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private ImageCapture imageCapture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}