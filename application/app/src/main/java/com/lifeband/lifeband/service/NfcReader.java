package com.lifeband.lifeband.service;

import android.content.Context;
import android.content.Intent;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.util.Log;

import com.lifeband.lifeband.exception.NfcException;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class NfcReader {

    private static final String TAG = NfcReader.class.getSimpleName();

    public static NfcAdapter getNfcAdapter(Context context) throws NfcException {
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(context);
        if(nfcAdapter == null) {
            throw new NfcException(NfcException.Reason.NOT_SUPPORTED);
        }
        else if(!nfcAdapter.isEnabled()) {
            throw new NfcException(NfcException.Reason.NOT_ENABLED);
        }
        return nfcAdapter;
    }

    public static String readTagFromIntent(Intent intent) throws NfcException {
        String action = intent.getAction();
        Log.d(TAG, "Received intent with action [" + action + "]");

        // No tag when you startup the application
        if(action.equals("android.intent.action.MAIN")) {
            Log.d(TAG, "Startup Intent detected - no action taken");
            return null;
        }

        switch(action) {
            case NfcAdapter.ACTION_NDEF_DISCOVERED:
                if(intent.getType().equals("text/plain")) {
                    return readParcelableTag((Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG));
                }
                else {
                    throw new NfcException(
                            NfcException.Reason.INVALID_CONTENT_TYPE
                    );
                }
            case NfcAdapter.ACTION_TECH_DISCOVERED:
                Tag parcelableTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                for(String techType : parcelableTag.getTechList()) {
                    if (techType.equals(Ndef.class.getName())) {
                        return readParcelableTag(parcelableTag);
                    }
                }
            default:
                throw new NfcException(NfcException.Reason.INVALID_TAG_TYPE);
        }
    }

    private static String readParcelableTag(Tag parcelableTag) throws NfcException {
        Ndef ndefTag = Ndef.get(parcelableTag);
        if(ndefTag == null) {
            throw new NfcException(NfcException.Reason.INVALID_TAG_TYPE);
        }
        // Unformatted empty tag
        else if (ndefTag.getCachedNdefMessage() == null) {
            return "";
        }

        NdefRecord extractedRecord = null;
        for(NdefRecord ndefRecord : ndefTag.getCachedNdefMessage().getRecords()) {
            if(ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN &&
                    Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                extractedRecord = ndefRecord;
                break;
            }
        }
        if(extractedRecord == null) {
            return "";
        }

        return readTagText(extractedRecord);
    }

    /**
     * See NFC forum specification for Text Record Type Definition at 3.2.1
     * http://www.nfc-forum.org/specs
     *
     * @param ndefRecord
     * @return
     * @throws NfcException
     */
    private static String readTagText(NdefRecord ndefRecord) throws NfcException {
        Log.d(TAG, "Reading NDEF record from tag payload");

        byte[] payload = ndefRecord.getPayload();
        int languageCodeLength = payload[0] & 0063;
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

        String data;
        try {
            data = new String(
                    payload,
                    languageCodeLength + 1,
                    payload.length - languageCodeLength - 1,
                    textEncoding
            );
        }
        catch(UnsupportedEncodingException e) {
            throw new NfcException(NfcException.Reason.INVALID_ENCODING);
        }

        Log.d(TAG, "Parsed tag data: [" + data + "]");
        return data;
    }

}