package ch.isageek.tyderion.habittracker.settings;

import android.content.Context;

import com.dropbox.sync.android.DbxAccount;
import com.dropbox.sync.android.DbxAccountManager;

/**
 * Created by tzhnaga1 on 25/04/14.
 */
public class DropboxHelper {

    public static String APP_KEY = "";
    public static String APP_SECRET = "";

    private DbxAccountManager mAccountManager;
    private DbxAccount mAccount;

    private static DropboxHelper instance;

    public static DropboxHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DropboxHelper(context, APP_KEY, APP_SECRET);
        }
        return instance;
    }

    public DbxAccount getAccount() {
        if (mAccountManager.hasLinkedAccount()) {
            mAccount = mAccountManager.getLinkedAccount();
        } else {
            mAccount = null;
        }
        return mAccount;
    }

    public DbxAccountManager getAccountManager() {
        return mAccountManager;
    }

    public void unlink() {
        getAccount().unlink();
    }


    public DropboxHelper(Context context, String appKey, String appSecret) {
        if (APP_KEY.length() > 0 && APP_SECRET.length() > 0) {
            mAccountManager = DbxAccountManager.getInstance(context.getApplicationContext(), APP_KEY, APP_SECRET);

        }
    }

    public void addAccountChangeListener(DbxAccount.Listener listener) {
        if (getAccount() != null) {
            getAccount().addListener(listener);
        }
    }






}
