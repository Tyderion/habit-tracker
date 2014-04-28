package com.github.tyderion.nfcwriter;

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
import android.util.Log;

import java.io.IOException;

/**
 * Most of this code is taken from this tutorial and changed to suit my needs:
 * http://tapintonfc.blogspot.ch/2012/07/the-above-footage-from-our-nfc-workshop.html
 *
 */
public class NFCWriter {

    /**
     * The callback which is called when the tag is written successfully.
     */
    public OnTagWrittenCallback onTagWritten;

    private Notifier notifier;
    private Context context;
    private boolean writeProtect;
    private NdefMessage tagToWrite;
    private PendingIntent pendingIntent;
    private IntentFilter[] filters;

    public interface OnTagWrittenCallback {
        public void tagWritten(NFCWriteStatus status);
    }
    public interface Notifier {
        public void notifiy(NFCWriteStatus status, String message);
    }

    /**
     * Simple way to write protected NFC Records
     * @param context the current context (used to launch intent), needs to implement NFCWriter.Notifier Interface
     * @param records the records to write.
     */
    public static void writeProtectedRecords(Activity context,boolean showToasts, NdefRecord... records) {
        NFCWriterActivity.writeProtectedRecords(context,showToasts, records);
    }

    /**
     * Simple way to write NFC Records
     * @param context the current context (used to launch intent), needs to implement NFCWriter.Notifier Interface
     * @param records the records to write.
     */
    public static void writeRecords(Activity context,boolean showToasts, NdefRecord... records) {
        NFCWriterActivity.writeRecords(context,showToasts, records);
    }

    /**
     * Primary constructor of the NFCWriter. You have to call configure before using it.
     * @param writeProtect true if the written NFC-Tag should be protected
     * @param message the NDefMessage to write
     */
    public NFCWriter(boolean writeProtect, NdefMessage message) {
        this.writeProtect = writeProtect;
        this.tagToWrite = message;
    }


    /**
     * Configures the NFC writer.
     * @param context The context in which the NFC Writer gets the strings
     * @param notifier This callback gets used to notify the user of errors/success
     */
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

    /**
     * PendingIntent for the activity to use
     * @return the pending NFC-Intent
     */
    public PendingIntent getPendingIntent() {
        return pendingIntent;
    }

    /**
     * The intent filters for the activity to filter for
     * @return the array of intent filters
     */
    public IntentFilter[] getIntentFilters() {
        return filters;
    }

    /**
     * Handles the intent which gets filtered by the intentfilters and the pending intent.
     * @param intent
     */
    public void handleIntent(Intent intent) {
        if (context == null || notifier == null) {
            Log.e("NFCWriter", "Please configure NFCWriter before use");
            return;
        }
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            // validate that this tag can be written
            NFCWriteStatus status = NFCWriteStatus.UNSUPPORTED;
            Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if (NFCWriter.supportedTechs(detectedTag.getTechList())) {
                // check if tag is writable (to the extent that we can
                if (writableTag(detectedTag)) {
                    //writeTag here
                    NFCWriter.WriteResponse wr = writeTag(tagToWrite, detectedTag);
//                    String message = context.getString(wr.getStatus() == NFCWriteStatus.WRITE_SUCCESSFUL ? R.string.tag_success :  R.string.tag_failure );
//                    message = String.format(message,  wr.getMessage());
                    status = wr.getStatus();
                    notifier.notifiy(status, wr.getMessage());
                } else {
                    status = NFCWriteStatus.READONLY;
                    notifier.notifiy(status, context.getString(R.string.tag_not_writable));
                }
            } else {
                status = NFCWriteStatus.UNSUPPORTED;
                notifier.notifiy(status, context.getString(R.string.tag_type_not_supported));
            }
            if (this.onTagWritten != null) {
                this.onTagWritten.tagWritten(status);
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
                    return new WriteResponse(NFCWriteStatus.READONLY, context.getString(R.string.tag_read_only));
                }
                if (ndef.getMaxSize() < size) {
                    mess = context.getString(R.string.tag_capacity_too_small);
                    mess = String.format(mess, ndef.getMaxSize(), size);
                    return new WriteResponse(NFCWriteStatus.MESSAGE_TOO_BIG, mess);
                }
                ndef.writeNdefMessage(message);
                if (writeProtect) ndef.makeReadOnly();
                mess = context.getString(R.string.tag_write_success);
                return new WriteResponse(NFCWriteStatus.WRITE_SUCCESSFUL, mess);
            } else {
                NdefFormatable format = NdefFormatable.get(tag);
                if (format != null) {
                    try {
                        format.connect();
                        WriteResponse response;
                        if (format.isConnected()) {
                            format.format(message);
                            mess = context.getString(R.string.tag_format_and_write_success);
                            response = new WriteResponse(NFCWriteStatus.WRITE_SUCCESSFUL, mess);
                        } else{
                            mess = context.getString(R.string.tag_connect_fail);
                            response = new WriteResponse(NFCWriteStatus.CONNECTION_FAILED,mess);
                        }
                        return  response;
                    } catch (IOException e) {
                        mess = context.getString(R.string.tag_format_fail);
                        return new WriteResponse(NFCWriteStatus.FORMAT_FAILED, mess);
                    }
                } else {
                    mess = context.getString(R.string.tag_no_ndef);
                    return new WriteResponse(NFCWriteStatus.UNSUPPORTED, mess);
                }
            }
        } catch (Exception e) {
            mess = context.getString(R.string.tag_write_failed);
            return new WriteResponse(NFCWriteStatus.UNSUPPORTED, mess);
        }
    }
    public enum NFCWriteStatus {
        WRITE_SUCCESSFUL,
        CONNECTION_FAILED,
        FORMAT_FAILED,
        UNSUPPORTED,
        READONLY,
        MESSAGE_TOO_BIG,
        FAILED_TO_READ_TAG
        }


    private boolean writableTag(Tag tag) {
        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();
                boolean writable = ndef.isWritable();
                ndef.close();
                return writable;
            } else {
                NdefFormatable ndefFormatable = NdefFormatable.get(tag);
                if (ndefFormatable != null) {
                    ndefFormatable.connect();
                    ndefFormatable.close();
                    return true;
                }
            }
        } catch (Exception e) {
            notifier.notifiy(NFCWriteStatus.FAILED_TO_READ_TAG, context.getString(R.string.tag_read_failed));
        }
        return false;
    }


    private static class WriteResponse {
        NFCWriteStatus status;
        String message;

        WriteResponse(NFCWriteStatus status, String Message) {
            this.status = status;
            this.message = Message;
        }

        public NFCWriteStatus getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }
}
