package shu.vsco.videoplayback;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    final private String TAG ="TEST";
    LinearLayout view;
    VscoVideoView textureView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = (LinearLayout) findViewById(R.id.videotest);
        displayVideo(360,640);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textureView.playAsync(view.getContext());
            }
        });

    }

    void displayVideo(int width, int height){
        try {
            playVideo(width, height);
        } catch (Exception e) {
            Log.e(TAG, " can not download the file: ", e);

        }
    }

    synchronized private void playVideo(int w, int h) {
        try {
            textureView = new VscoVideoView(view.getContext());
            textureView.setLayoutParams(new ViewGroup.LayoutParams(w, h));

            view.addView(textureView);
        } catch (IOException e) {
            Log.e(TAG, "Can not load the url:", e);
        }
    }



}
