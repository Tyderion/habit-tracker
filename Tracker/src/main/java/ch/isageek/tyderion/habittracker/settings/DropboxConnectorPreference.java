package ch.isageek.tyderion.habittracker.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.preference.Preference;
import android.preference.TwoStatePreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.sync.android.DbxAccount;
import com.dropbox.sync.android.DbxAccountInfo;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;
import ch.isageek.tyderion.habittracker.R;

/**
 * Created by tzhnaga1 on 24/04/14.
 */
public class DropboxConnectorPreference extends TwoStatePreference implements DbxAccount.Listener {

    @Optional @InjectView(R.id.dropbox_title) TextView titleView;
    @Optional @InjectView(R.id.dropbox_title) TextView statusView ;

    static final int REQUEST_LINK_TO_DBX = 6668;  // This value is up to you


    private DropboxHelper helper;

    private String currentStatus;


    private Context mContext;

    private static DropboxConnectorPreference instance;


    public DropboxConnectorPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        instance = this;
        mContext = context;
        DropboxHelper.APP_KEY = attrs.getAttributeValue(null, "dropboxAppKey");
        DropboxHelper.APP_SECRET = attrs.getAttributeValue(null, "dropboxAppSecret");
//        setLayoutResource(R.layout.dropbox_preference);
        setTitle(R.string.dropbox_title);
        helper = DropboxHelper.getInstance(context);

        setDisableDependentsState(true);
    }

    public static DropboxConnectorPreference getInstance() {
        return instance;
    }


    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LINK_TO_DBX) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(mContext, "Dropbox Link established", Toast.LENGTH_SHORT).show();
                helper.addAccountChangeListener(this);
                updateDisplay();
            } else {
                Toast.makeText(mContext, "Dropbox Link failed", Toast.LENGTH_SHORT).show();
            }
            init();
        }
    }

    @Override
    public void onAccountChange(DbxAccount dbxAccount) {
        updateDisplay();
    }

    public void init() {
       helper.addAccountChangeListener(this);
       updateDisplay();
    }

    private void updateDisplay() {
        if (helper.getAccount() != null) {
            DbxAccountInfo info = helper.getAccount().getAccountInfo();
            String account = info != null ? info.displayName : "";
            currentStatus = mContext.getResources().getString(R.string.dropbox_connected, account);
        } else {
            currentStatus = mContext.getResources().getString(R.string.dropbox_disconnected);
        }
        setChecked(helper.getAccount() == null);
        setSummary(currentStatus);
        notifyChanged();
    }


    @Override
    public void setSummary(CharSequence summary) {
        super.setSummary(summary);
        if (statusView != null) {
            statusView.setText(summary);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        if (titleView != null) {
            titleView.setText(title);
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
        init();
        updateDisplay();
        return superView;
    }



    public void onClick(Preference v) {
        if (helper.getAccount() != null) {

            AlertDialog.Builder alert = new AlertDialog.Builder(mContext);

            alert.setTitle("Do you want to disconnect?");
            alert.setMessage("Press OK to disconnect from Dropbox");
            alert.setPositiveButton(mContext.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    helper.unlink();
                    updateDisplay();
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
            helper.getAccountManager().startLink((Activity) mContext, REQUEST_LINK_TO_DBX);
        }
    }


    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        updateDisplay();;
    }






}
