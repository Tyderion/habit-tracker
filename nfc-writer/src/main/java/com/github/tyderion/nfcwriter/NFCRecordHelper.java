package com.github.tyderion.nfcwriter;

import android.nfc.NdefRecord;
import java.nio.charset.Charset;

/**
 * Created by Archie on 13.04.2014.
 */
public class NFCRecordHelper {
    public static NdefRecord createMime(String mimeType, String data) {
        return NdefRecord.createMime(mimeType,
                data.getBytes(Charset.forName("US-ASCII")));
    }

    public static NdefRecord applicationRecord(String application) {
        return NdefRecord.createApplicationRecord(application);
    }
}
