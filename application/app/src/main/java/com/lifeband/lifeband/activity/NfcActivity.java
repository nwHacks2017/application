package com.lifeband.lifeband.activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.lifeband.lifeband.NfcReader;
import com.lifeband.lifeband.R;
import com.lifeband.lifeband.exception.NfcException;

public class NfcActivity extends AppCompatActivity {
    private static final float ROTATE_FROM = 0.0f;
    private static final float ROTATE_TO = -10.0f * 360.0f;// 3.141592654f * 32.0f;

    private NfcAdapter nfcAdapter;

    private PendingIntent pendingIntent;

    private IntentFilter[] intentFilters;

    private String[][] nfcTechList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            nfcAdapter = NfcReader.getNfcAdapter(this);
        }
        catch(NfcException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
        );

        IntentFilter intentFilter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            intentFilter.addDataType("*/*");
        }
        catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Failed to read proper mime-type", e);
        }
        intentFilters = new IntentFilter[]{intentFilter};

        nfcTechList = new String[][]{
                new String[]{Ndef.class.getName()},
                new String[]{NfcA.class.getName()},
                new String[]{NfcB.class.getName()}
        };


        setContentView(R.layout.activity_nfc);
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
    public void onPause() {
        super.onPause();
        if(nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, nfcTechList);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        String tagData;
        try {
            tagData = NfcReader.readTagFromIntent(intent);
        }
        catch(NfcException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }
        if(tagData == null) {
            Toast.makeText(this, "No tag data to read.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent newIntent = new Intent(this, MainActivity.class);
        Log.d("nfcActivity", "Sending Extra tagData: " + tagData);
        newIntent.putExtra("tagData", tagData);
        startActivity(newIntent);
    }

}
