package shu.vsco.videoplayback;


import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.util.Log;
import android.util.LruCache;
import android.view.Surface;
import android.view.TextureView;
import java.io.File;
import java.io.IOException;

class VscoVideoView extends TextureView implements TextureView.SurfaceTextureListener {
    private static final String TAG = VscoVideoView.class.getSimpleName();
    final static private LruCache<String, File> cache = new LruCache<String, File>(10) {
        @Override
        protected void entryRemoved(boolean evicted, String key, File oldValue, File newValue) {
            super.entryRemoved(evicted, key, oldValue, newValue);
            oldValue.delete();
        }
    };

    private MediaPlayer mediaPlayer;
    private Surface surface = null;

    public VscoVideoView(final Context context) throws IOException {
        super(context);
        setSurfaceTextureListener(this);
    }

    public void playAsync(final Context context){
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    synchronized (VscoVideoView.this) {
                        if (surface != null) {
                            play(context);
                        }else{
                            Log.e(TAG, "surface is null");
                        }
                    }
                } catch (Throwable e) {
                    Log.e(TAG, "Can not load the video into a file:", e);
                }
            }
        }).start();

    }

    final synchronized private void play(Context context) {
        Log.d(TAG, "Play video.");
        try {
            if (mediaPlayer != null) {
                Log.d(TAG, "Play video only once.");
                return;
            }
            mediaPlayer = MediaPlayer.create(context,R.raw.grass);
            mediaPlayer.setSurface(surface);

            mediaPlayer.setLooping(true);
            mediaPlayer.start();


        } catch (Throwable e) {
            Log.e(TAG, "Can not start the MediaPlayer", e);
            Log.e(TAG, "Can not start the MediaPlayer", e);
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Log.d(TAG, "onSurfaceTextureAvailable:" + width + "," + height);
        synchronized (this) {
            this.surface = new Surface(surface);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        Log.d(TAG, "onSurfaceTextureDestroyed:");
        release();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }

    private void release() {
        try {
            synchronized (this) {
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                    Log.d(TAG, "mediaPayer = null;");
                    surface = null;
                }
            }
        } catch (Throwable e) {
            Log.w(TAG, "can not release the media player.", e);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        release();
    }
}
