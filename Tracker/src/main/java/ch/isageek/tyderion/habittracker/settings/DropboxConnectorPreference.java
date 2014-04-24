package ch.isageek.tyderion.habittracker.settings;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import ch.isageek.tyderion.habittracker.R;

/**
 * Created by tzhnaga1 on 24/04/14.
 */
public class DropboxConnectorPreference extends Preference {

    @InjectView(R.id.dropbox_status) TextView statusView;

    private Context mContext;

    public DropboxConnectorPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setLayoutResource(R.layout.dropbox_preference);
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
        return super.onCreateView(parent);
    }

    public void onClick(Preference v) {
        Toast.makeText(mContext, "Test Callback", Toast.LENGTH_SHORT).show();
    }
}
