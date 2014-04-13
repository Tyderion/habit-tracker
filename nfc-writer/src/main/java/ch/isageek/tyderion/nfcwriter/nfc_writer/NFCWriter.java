package ch.isageek.tyderion.nfcwriter.nfc_writer;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.util.Log;

import java.io.IOException;

/**
 * Most of this code is taken from:
 * http://tapintonfc.blogspot.ch/2012/07/the-above-footage-from-our-nfc-workshop.html
 */
public class NFCWriter {

    public OnTagWrittenCallback onTagWritten;

    private Notifier notifier;
    private Context context;
    private boolean writeProtect;
    private NdefMessage tagToWrite;
    private PendingIntent pendingIntent;
    private IntentFilter[] filters;

    public interface OnTagWrittenCallback {
        public void tagWritten();
    }
    public interface Notifier {
        public void notifiy(String msg);
    }

    public NFCWriter(boolean writeProtect, NdefMessage message) {
        this.writeProtect = writeProtect;
        this.tagToWrite = message;
    }

    public void configure(Context context, Notifier notifier) {
        this.context = context;
        this.notifier = notifier;
        this.filters = this.createIntentFilters();
    }

    private IntentFilter[] createIntentFilters() {
        pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context,
                context.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TOP), 0);
        IntentFilter discovery = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        return new IntentFilter[]{discovery};
    }

    public PendingIntent getPendingIntent() {
        return pendingIntent;
    }

    public IntentFilter[] getIntentFilters() {
        return filters;
    }

    public void handleIntent(Intent intent) {
        if (context == null || notifier == null) {
            Log.e("NFCWriter", "Please configure NFCWriter before use");
            return;
        }
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            // validate that this tag can be written
            Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if (NFCWriter.supportedTechs(detectedTag.getTechList())) {
                // check if tag is writable (to the extent that we can
                if (writableTag(detectedTag)) {
                    //writeTag here
                    NFCWriter.WriteResponse wr = writeTag(tagToWrite, detectedTag);
                    String message = context.getString(wr.getStatus() == 1 ? R.string.tag_success :  R.string.tag_failure );
                    message = String.format(message,  wr.getMessage());
                    notifier.notifiy(message);
                } else {
                    notifier.notifiy(context.getString(R.string.tag_not_writable));
                }
            } else {
                notifier.notifiy(context.getString(R.string.tag_type_not_supported));
            }
            if (this.onTagWritten != null) {
                this.onTagWritten.tagWritten();
            }
        }
    }

    private static boolean supportedTechs(String[] techs) {
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

    private WriteResponse writeTag(NdefMessage message, Tag tag) {
        int size = message.toByteArray().length;
        String mess;
        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();
                if (!ndef.isWritable()) {
                    return new WriteResponse(0, context.getString(R.string.tag_read_only));
                }
                if (ndef.getMaxSize() < size) {
                    mess = context.getString(R.string.tag_capacity_too_small);
                    mess = String.format(mess, ndef.getMaxSize(), size);
                    return new WriteResponse(0, mess);
                }
                ndef.writeNdefMessage(message);
                if (writeProtect) ndef.makeReadOnly();
                mess = context.getString(R.string.tag_write_success);
                return new WriteResponse(1, mess);
            } else {
                NdefFormatable format = NdefFormatable.get(tag);
                if (format != null) {
                    try {
                        format.connect();
                        format.format(message);
                        mess = context.getString(R.string.tag_format_and_write_success);
                        return new WriteResponse(1, mess);
                    } catch (IOException e) {
                        mess = context.getString(R.string.tag_format_fail);
                        return new WriteResponse(0, mess);
                    }
                } else {
                    mess = context.getString(R.string.tag_no_ndef);
                    return new WriteResponse(0, mess);
                }
            }
        } catch (Exception e) {
            mess = context.getString(R.string.tag_write_failed);
            return new WriteResponse(0, mess);
        }
    }

    private boolean writableTag(Tag tag) {
        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();
                if (!ndef.isWritable()) {
                    notifier.notifiy(context.getString(R.string.tag_read_only));
                    ndef.close();
                    return false;
                }
                ndef.close();
                return true;
            }
        } catch (Exception e) {
            notifier.notifiy(context.getString(R.string.tag_read_failed));
        }
        return false;
    }

    private static class WriteResponse {
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
}
