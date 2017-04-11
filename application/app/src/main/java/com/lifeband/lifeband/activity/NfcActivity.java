package com.lifeband.lifeband.activity;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lifeband.lifeband.NfcReader;
import com.lifeband.lifeband.R;
import com.lifeband.lifeband.exception.NfcException;

public class NfcActivity extends AppCompatActivity {
    private static final float ROTATE_FROM = 0.0f;
    private static final float ROTATE_TO = -10.0f * 360.0f;// 3.141592654f * 32.0f;

    private TextView textview;

    public NfcActivity(){
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            NfcReader.instantiateNfcAdapter(this);
        }
        catch(NfcException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            if(e.getReason().equals(NfcException.Reason.NOT_SUPPORTED)) {
                finish();
            }
            return;
        }
        

        setContentView(R.layout.activity_nfc);
        textview = (TextView) findViewById(R.id.testingNfc);
/*        RotateAnimation anim =  new RotateAnimation(30, 90,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(2000);

// Start animating the image
        final ImageView splash = (ImageView) findViewById(R.id.rotator);
        splash.startAnimation(anim);

// Later.. stop the animation
        splash.setAnimation(null);*/
    }

    @Override
    protected void onNewIntent(Intent intent) {
        readTagDataTo(textview);
        // TODO: Fill in code here
        //Intent i = new Intent(<ResultActivity>
        //
    }

    private void readTagDataTo(TextView textView) {
        String data;
        try {
            data = NfcReader.readTagFromIntent(getIntent());
        }
        catch(NfcException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }
        if(data != null) {
            textView.setText("Tag data: " + data);
        }
    }
}
