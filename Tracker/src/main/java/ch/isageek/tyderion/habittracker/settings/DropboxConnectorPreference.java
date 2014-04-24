package ch.isageek.tyderion.habittracker.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.sync.android.DbxAccount;
import com.dropbox.sync.android.DbxAccountInfo;
import com.dropbox.sync.android.DbxAccountManager;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ch.isageek.tyderion.habittracker.R;

/**
 * Created by tzhnaga1 on 24/04/14.
 */
public class DropboxConnectorPreference extends Preference {

    @InjectView(R.id.dropbox_status) TextView statusView;

    static final int REQUEST_LINK_TO_DBX = 0;  // This value is up to you

    private String APP_KEY = "";
    private String APP_SECRET = "";

    private DbxAccountManager mAccountManager;
    private DbxAccount mAccount;

    private String currentStatus;


    private Context mContext;

    private static DropboxConnectorPreference instance;

    public static DropboxConnectorPreference getInstance() {
        return instance;
    }



    public DropboxConnectorPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        instance = this;
        mContext = context;
        APP_KEY = attrs.getAttributeValue(null, "dropboxAppKey");
        APP_SECRET = attrs.getAttributeValue(null, "dropboxAppSecret");
        setLayoutResource(R.layout.dropbox_preference);
        init();
    }

    public void init() {
        if (APP_KEY.length() > 0 && APP_SECRET.length() > 0) {
            mAccountManager = DbxAccountManager.getInstance(mContext.getApplicationContext(), APP_KEY, APP_SECRET);
            if (mAccountManager.hasLinkedAccount()) {
                mAccount = mAccountManager.getLinkedAccount();
            }
        }
        updateDisplay();
    }

    private void updateDisplay() {
        if (mAccount != null) {
            DbxAccountInfo info = mAccount.getAccountInfo();
            String account = info != null ? info.displayName : "";
            currentStatus = mContext.getResources().getString(R.string.dropbox_connected, account);
        } else {
            currentStatus = mContext.getResources().getString(R.string.dropbox_disconnected);
        }
        if (statusView != null) {
            statusView.setText(currentStatus);
        }
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        View superView = super.onCreateView(parent);
        ButterKnife.inject(this, superView);

        setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                onClick(preference);
                return true;
            }
        });
        updateDisplay();
        statusView.setText("BLABLABLALBALBALBJALBJAå");
        return super.onCreateView(parent);
    }

    public void onClick(Preference v) {
        if (mAccount != null) {

            AlertDialog.Builder alert = new AlertDialog.Builder(mContext);

            alert.setTitle("Do you want to disconnect?");
            alert.setMessage("Press OK to disconnect from Dropbox");
            alert.setPositiveButton(mContext.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mAccount.unlink();
                }
            });
            alert.setNegativeButton(mContext.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            alert.show();
        } else {
            mAccountManager.startLink((Activity)mContext, REQUEST_LINK_TO_DBX);
        }
    }



    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        updateDisplay();;
    }



}
