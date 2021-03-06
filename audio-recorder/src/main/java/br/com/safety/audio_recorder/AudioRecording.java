package br.com.safety.audio_recorder;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * @author netodevel
 */
public class AudioRecording {
    private String outputFile;

    private String mFileName;
    private Context mContext;

    private MediaPlayer mMediaPlayer;
    private AudioListener audioListener;
    private MediaRecorder mRecorder;
    private long mStartingTimeMillis = 0;
    private long mElapsedMillis = 0;

    public AudioRecording(Context context) {
        mRecorder = new MediaRecorder();
        this.mContext = context;
    }

    public AudioRecording() {
        mRecorder = new MediaRecorder();
    }

    public AudioRecording setNameFile(String nameFile) {
        this.mFileName = nameFile;
        return this;
    }

    public AudioRecording start(AudioListener audioListener) {
        this.audioListener = audioListener;

        try {
//..........
//            mRecorder.reset();
//
//            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//            mRecorder.setOutputFile(mContext.getCacheDir() + mFileName);
//            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
//
//            mRecorder.prepare();
//            mRecorder.start();
//            mStartingTimeMillis = System.currentTimeMillis();
            //........saving to mem
            outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/bhuuuuurecording.3gp";
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            mRecorder.setOutputFile(outputFile);
            Toast.makeText(mContext, "successssssss", Toast.LENGTH_SHORT).show();
            mRecorder.prepare();
          mRecorder.start();
          mStartingTimeMillis = System.currentTimeMillis();
        } catch (IOException e) {
            this.audioListener.onError(e);
        }
        return this;
    }

    public void stop(Boolean cancel) {
        try {
            mRecorder.stop();
        } catch (RuntimeException e) {
            deleteOutput();
        }
        mRecorder.release();
        mElapsedMillis = (System.currentTimeMillis() - mStartingTimeMillis);

        RecordingItem recordingItem = new RecordingItem();
        recordingItem.setFilePath(mContext.getCacheDir() + mFileName);
        recordingItem.setName(mFileName);
        recordingItem.setLength((int)mElapsedMillis);
        recordingItem.setTime(System.currentTimeMillis());

        if (cancel == false) {
            audioListener.onStop(recordingItem);
        } else {
            audioListener.onCancel();
        }
    }

    private void deleteOutput() {
        File file = new File(mContext.getCacheDir() + mFileName);
        if (file.exists()) {
            file.delete();
        }
    }

    public void play(RecordingItem recordingItem) {
        try {
            this.mMediaPlayer = new MediaPlayer();
            this.mMediaPlayer.setDataSource(recordingItem.getFilePath());
            this.mMediaPlayer.prepare();
            this.mMediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
