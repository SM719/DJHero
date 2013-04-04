package com.group15.djhero;

import java.io.File;
import java.io.IOException;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Share extends Activity {
	public static final String TAG = "soundcloud-intent-sharing-example";

    private boolean mStarted;
    private MediaRecorder mRecorder;
    private File mArtwork;

    private static boolean AAC_SUPPORTED  = Build.VERSION.SDK_INT >= 10;
    private static final int PICK_ARTWORK = 1;
    private static final int SHARE_SOUND  = 2;

    private static final File RECORDING = new File(
    		Environment.getExternalStorageDirectory(),
            "MIX.wav");

    private static final Uri MARKET_URI = Uri.parse("market://details?id=com.soundcloud.android");
    private static final int DIALOG_NOT_INSTALLED = 0;

    // Replace with the client id of your registered app!
    // see http://soundcloud.com/you/apps/
    private static final String CLIENT_ID = "036f84bc406051d663d7f9723afa5cf2";
    
    EditText name_text, location_text, description_text, tag_text, genre_text;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);


        if (!Environment.MEDIA_MOUNTED.equals(
            Environment.getExternalStorageState())) {
            Toast.makeText(this, R.string.need_external_storage, Toast.LENGTH_LONG).show();
            finish();
        }

        setContentView(R.layout.activity_share);

        final Button share_btn = (Button) findViewById(R.id.share_share_btn);
        final Button artwork_btn = (Button) findViewById(R.id.share_artwork_btn);
        name_text = (EditText) findViewById(R.id.share_title_text); 
        location_text = (EditText) findViewById(R.id.share_location_text); 
        description_text = (EditText) findViewById(R.id.share_description_text);
        tag_text = (EditText) findViewById(R.id.share_tags_text);
        genre_text = (EditText) findViewById(R.id.share_genre_text); 
        
        share_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shareSound();
            }
        });

        artwork_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"), PICK_ARTWORK);
            }
        });

        if (!isCompatibleSoundCloudInstalled(this)) {
            showDialog(DIALOG_NOT_INSTALLED);
        }
    }
    
    public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case android.R.id.home:
			super.onBackPressed();
			return true;
		default:
		return super.onOptionsItemSelected(item);
		}
	}

    private void play(MediaPlayer.OnCompletionListener onCompletion) {
        MediaPlayer player = new MediaPlayer();
        try {
            player.setDataSource(RECORDING.getAbsolutePath());
            player.prepare();
            player.setOnCompletionListener(onCompletion);
            player.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    // the actual sharing happens here
    private void shareSound() {
    	String title = (!name_text.getText().toString().isEmpty()) ? name_text.getText().toString() : "Default Title";
    	String location = (!location_text.getText().toString().isEmpty()) ? location_text.getText().toString() : "Default Location";
    	String genre = (!genre_text.getText().toString().isEmpty()) ? genre_text.getText().toString() : "Default Genre";
    	String description = (!description_text.getText().toString().isEmpty()) ? description_text.getText().toString() : "Default Song Description";
    	String[] tags = tag_text.getText().toString().split("\\s+");
    	if (tags.length == 0) {
    		tags = new String[]{"Default Tag"};
    	}
    	Intent intent = new Intent("com.soundcloud.android.SHARE")
                .putExtra(Intent.EXTRA_STREAM, Uri.fromFile(RECORDING))
                // here you can set metadata for the track to be uploaded
                .putExtra("com.soundcloud.android.extra.title", title)
                .putExtra("com.soundcloud.android.extra.where", location)
                .putExtra("com.soundcloud.android.extra.description", description)
                .putExtra("com.soundcloud.android.extra.public", true)
                .putExtra("com.soundcloud.android.extra.tags", tags)
                .putExtra("com.soundcloud.android.extra.genre", genre)
                .putExtra("com.soundcloud.android.extra.location", getLocation());

        // attach artwork if user has picked one
        if (mArtwork != null) {
            intent.putExtra("com.soundcloud.android.extra.artwork", Uri.fromFile(mArtwork));
        }

        try {
            startActivityForResult(intent, SHARE_SOUND);
        } catch (ActivityNotFoundException notFound) {
            // use doesn't have SoundCloud app installed, show a dialog box
            showDialog(DIALOG_NOT_INSTALLED);
        }
    }


    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SHARE_SOUND:
                // callback gets executed when the SoundCloud app returns
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, R.string.shared_ok, Toast.LENGTH_SHORT).show();
                } else {
                    // canceled
                    Toast.makeText(this, R.string.shared_canceled, Toast.LENGTH_SHORT).show();
                }
                break;

            case PICK_ARTWORK:
                if (resultCode == RESULT_OK) {
                    mArtwork = getFromMediaUri(getContentResolver(), data.getData());
                }
                break;
        }
    }

    private MediaRecorder getRecorder(File path, boolean useAAC) {
        MediaRecorder recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);

         if (useAAC) {
             recorder.setAudioSamplingRate(44100);
             recorder.setAudioEncodingBitRate(96000);
             recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
             recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
         } else {
             // older version of Android, use crappy sounding voice codec
             recorder.setAudioSamplingRate(8000);
             recorder.setAudioEncodingBitRate(12200);
             recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
             recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
         }

        recorder.setOutputFile(path.getAbsolutePath());

        try {
            recorder.prepare();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return recorder;
    }

    // just get the last known location from the passive provider - not terribly
    // accurate but it's a demo app.
    private Location getLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
    }

    @Override public Share getLastNonConfigurationInstance() {
        return (Share) super.getLastNonConfigurationInstance();
    }

    @Override public Share onRetainNonConfigurationInstance() {
        return this;
    }

    // Helper method to get file from a content uri
    private static File getFromMediaUri(ContentResolver resolver, Uri uri) {
        if ("file".equals(uri.getScheme())) {
            return new File(uri.getPath());
        } else if ("content".equals(uri.getScheme())) {
            String[] filePathColumn = {MediaStore.MediaColumns.DATA};
            Cursor cursor = resolver.query(uri, filePathColumn, null, null, null);
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String filePath = cursor.getString(columnIndex);
                        return new File(filePath);
                    }
                } finally {
                    cursor.close();
                }
            }
        }
        return null;
    }


    private static boolean isCompatibleSoundCloudInstalled(Context context) {
        try {
            PackageInfo info = context.getPackageManager()
                                      .getPackageInfo("com.soundcloud.android",
                    PackageManager.GET_META_DATA);

            // intent sharing only got introduced with version 22
            return info != null && info.versionCode >= 22;
        } catch (PackageManager.NameNotFoundException e) {
            // not installed at all
            return false;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id, Bundle data) {
        if (DIALOG_NOT_INSTALLED == id) {
            return new AlertDialog.Builder(this)
                    .setTitle(R.string.sc_app_not_found)
                    .setMessage(R.string.sc_app_not_found_message)
                    .setPositiveButton(android.R.string.yes, new Dialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent market = new Intent(Intent.ACTION_VIEW, MARKET_URI);
                            startActivity(market);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).create();
        } else {
            return null;
        }
    }
}
