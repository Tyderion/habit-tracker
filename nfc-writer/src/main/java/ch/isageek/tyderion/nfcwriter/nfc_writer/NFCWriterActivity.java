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
import android.view.View;
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
    private static NFCWriter writer;

    public static void writeProtectedRecords(Context context, NdefRecord... records) {
        writeRecords(context, true, records);
    }
    public static void writeRecords(Context context, NdefRecord... records) {
        writeRecords(context, false, records);
    }

    private static void writeRecords(Context context, boolean writeProtection,  NdefRecord... records) {
        if (records.length == 0) {
            notifyNoRecords(context);
        }
        writer = new NFCWriter(context, writeProtection, new NdefMessage(records));
        context.startActivity(new Intent(context, NFCWriterActivity.class));
    }

    private static void notifyNoRecords(Context context) {
        Toast.makeText(context, context.getString(R.string.no_records_to_write), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (writer == null) {
            notifyNoRecords(this);
            finish();
        }
        setContentView(R.layout.activity_nfcwriter);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mNfcAdapter != null) mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        writer.handleIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mNfcAdapter != null) {
            if (!mNfcAdapter.isEnabled()) {
                Toast.makeText(this, getString(R.string.enable_nfc), Toast.LENGTH_SHORT).show();
            } else {
                mNfcAdapter.enableForegroundDispatch(this, writer.getPendingIntent(), writer.getIntentFilters(), null);
            }
        } else {
            Toast.makeText(this, getString(R.string.no_nfc_found), Toast.LENGTH_SHORT).show();
        }
    }

    public void closeDialog(View view) {
        finish();
    }
}
