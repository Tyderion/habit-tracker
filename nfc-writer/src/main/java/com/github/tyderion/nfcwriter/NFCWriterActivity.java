package com.github.tyderion.nfcwriter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * Most of this code is taken from:
 * http://tapintonfc.blogspot.ch/2012/07/the-above-footage-from-our-nfc-workshop.html
 */
public class NFCWriterActivity extends Activity {
    private NfcAdapter mNfcAdapter;
    private static NFCWriter writer;
    public static int REQUEST_NFC_WRITE = 555;
    public static String NFC_WRITE_STATUS = "nfc_write_status";
    private static boolean showToasts;
    private NFCWriter.Notifier toastNotifier = new NFCWriter.Notifier() {
        @Override
        public void notifiy(NFCWriter.NFCWriteStatus status, String msg) {
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    };
    private NFCWriter.Notifier emptyNotifier = new NFCWriter.Notifier() {
        @Override
        public void notifiy(NFCWriter.NFCWriteStatus status, String message) {
            /* do nothing */
        }
    };

    /**
     * Simple way to write protected NFC Records
     * @param context the current context (used to launch intent), needs to implement NFCWriter.Notifier Interface
     * @param records the records to write.
     */
    public static void writeProtectedRecords(Activity context,boolean showToasts, NdefRecord... records) {
        writeRecords(context, true,showToasts, records);
    }

    /**
     * Simple way to write NFC Records
     * @param context the current context (used to launch intent), needs to implement NFCWriter.Notifier Interface
     * @param records the records to write.
     */
    public static void writeRecords(Activity context,boolean showToasts, NdefRecord... records) {
        writeRecords(context, false,showToasts, records);
    }

    private static void writeRecords(Activity context, boolean writeProtection,boolean showToast,  NdefRecord... records) {
        if (records.length == 0) {
            notifyNoRecords(context);
        }
        showToasts = showToast;
        writer = new NFCWriter(writeProtection, new NdefMessage(records));
        context.startActivityForResult(new Intent(context, NFCWriterActivity.class), REQUEST_NFC_WRITE);
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
        writer.configure(this, showToasts ? toastNotifier : emptyNotifier);
        writer.onTagWritten = new NFCWriter.OnTagWrittenCallback() {
            @Override
            public void tagWritten(NFCWriter.NFCWriteStatus status) {
                int resultStatus = status == NFCWriter.NFCWriteStatus.WRITE_SUCCESSFUL ? RESULT_OK : RESULT_CANCELED;
                Intent result = new Intent();
                result.putExtra(NFC_WRITE_STATUS, status);
                setResult(resultStatus, result);
                finish();
            }
        };
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
            finish();
        }
    }

    public void closeDialog(View view) {
        finish();
    }
}
