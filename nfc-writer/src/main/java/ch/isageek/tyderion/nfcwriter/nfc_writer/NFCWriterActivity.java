package ch.isageek.tyderion.nfcwriter.nfc_writer;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Most of this code is taken from:
 * http://tapintonfc.blogspot.ch/2012/07/the-above-footage-from-our-nfc-workshop.html
 */
public class NFCWriterActivity extends Activity {
    private NfcAdapter mNfcAdapter;
    private IntentFilter[] mWriteTagFilters;
    private PendingIntent mNfcPendingIntent;
    private Context context;

    private static NdefRecord[] recordsToWrite;
    private static boolean writeProtect = false;

    public static void writeProtectedRecords(Context context, NdefRecord... records) {
        writeRecords(context, true, records);
    }
    public static void writeRecords(Context context, NdefRecord... records) {
        writeRecords(context, false, records);
    }

    private static void writeRecords(Context context, boolean writeProtection,  NdefRecord... records) {
        recordsToWrite = records;
        writeProtect = writeProtection;
        context.startActivity(new Intent(context, NFCWriterActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (recordsToWrite == null || recordsToWrite.length == 0) {
            Toast.makeText(this, getString(R.string.no_records_to_write), Toast.LENGTH_SHORT).show();
            finish();
        }

        setContentView(R.layout.activity_nfcwriter);
        context = getApplicationContext();
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mNfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TOP), 0);
        IntentFilter discovery = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        mWriteTagFilters = new IntentFilter[]{discovery};
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mNfcAdapter != null) mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            // validate that this tag can be written
            Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if (supportedTechs(detectedTag.getTechList())) {
                // check if tag is writable (to the extent that we can
                if (writableTag(detectedTag)) {
                    //writeTag here
                    WriteResponse wr = writeTag(getTagAsNdef(), detectedTag);
                    String message = (wr.getStatus() == 1 ? "Success: " : "Failed: ") + wr.getMessage();
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, getString(R.string.tag_not_writable), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, getString(R.string.tag_type_not_supported), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public WriteResponse writeTag(NdefMessage message, Tag tag) {
        int size = message.toByteArray().length;
        String mess;
        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();
                if (!ndef.isWritable()) {
                    return new WriteResponse(0, getString(R.string.tag_read_only));
                }
                if (ndef.getMaxSize() < size) {
                    mess = getString(R.string.tag_capacity_too_small);
                    mess = String.format(mess, ndef.getMaxSize(), size);
                    return new WriteResponse(0, mess);
                }
                ndef.writeNdefMessage(message);
                if (writeProtect) ndef.makeReadOnly();
                mess = getString(R.string.tag_write_success);
                return new WriteResponse(1, mess);
            } else {
                NdefFormatable format = NdefFormatable.get(tag);
                if (format != null) {
                    try {
                        format.connect();
                        format.format(message);
                        mess = getString(R.string.tag_format_and_write_success);
                        return new WriteResponse(1, mess);
                    } catch (IOException e) {
                        mess = getString(R.string.tag_format_fail);
                        return new WriteResponse(0, mess);
                    }
                } else {
                    mess = getString(R.string.tag_no_ndef);
                    return new WriteResponse(0, mess);
                }
            }
        } catch (Exception e) {
            mess = getString(R.string.tag_write_failed);
            return new WriteResponse(0, mess);
        }
    }

    private class WriteResponse {
        int status;
        String message;

        WriteResponse(int Status, String Message) {
            this.status = Status;
            this.message = Message;
        }

        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }

    public static boolean supportedTechs(String[] techs) {
        boolean ultralight = false;
        boolean nfcA = false;
        boolean ndef = false;
        for (String tech : techs) {
            if (tech.equals("android.nfc.tech.MifareUltralight")) {
                ultralight = true;
            } else if (tech.equals("android.nfc.tech.NfcA")) {
                nfcA = true;
            } else if (tech.equals("android.nfc.tech.Ndef") || tech.equals("android.nfc.tech.NdefFormatable")) {
                ndef = true;
            }
        }
        return ultralight && nfcA && ndef;
    }

    private boolean writableTag(Tag tag) {
        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();
                if (!ndef.isWritable()) {
                    Toast.makeText(context, getString(R.string.tag_read_only), Toast.LENGTH_SHORT).show();
                    ndef.close();
                    return false;
                }
                ndef.close();
                return true;
            }
        } catch (Exception e) {
            Toast.makeText(context, getString(R.string.tag_read_failed), Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private NdefMessage getTagAsNdef() {
        return new NdefMessage(recordsToWrite);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mNfcAdapter != null) {
            if (!mNfcAdapter.isEnabled()) {
                Toast.makeText(context, getString(R.string.enable_nfc), Toast.LENGTH_SHORT).show();
            } else {
                mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mWriteTagFilters, null);
            }
        } else {
            Toast.makeText(context, getString(R.string.no_nfc_found), Toast.LENGTH_SHORT).show();
        }
    }
}
