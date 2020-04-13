package com.app.smarthome.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.app.smarthome.R;
import com.app.smarthome.util.Constants;
import com.app.smarthome.util.GlobalMethods;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class QrScannerActivity extends AppCompatActivity implements Constants {

    private static final String NAME = QrScannerActivity.class.getSimpleName();
    private static final String TAG = COMMON_TAG;
    private CodeScanner mCodeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, NAME + "onCreate called:  ");
        setContentView(R.layout.activity_qr_scanner);
        QrScannerActivityPermissionsDispatcher.initWithPermissionCheck(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i(TAG, NAME + "onRequestPermissionsResult called:  ");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        QrScannerActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    public void init() {
        Log.i(TAG, NAME + "init called:  ");
        CodeScannerView scannerView = findViewById(R.id.qr_scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(result -> runOnUiThread(() -> Toast.makeText(QrScannerActivity.this, result.getText(), Toast.LENGTH_SHORT).show()));
        scannerView.setOnClickListener(view -> mCodeScanner.startPreview());
    }

    @Override
    protected void onResume() {
        Log.i(TAG, NAME + "onResume called:  ");
        super.onResume();
        if (mCodeScanner != null) {
            mCodeScanner.startPreview();
        }
    }

    @Override
    protected void onPause() {
        Log.i(TAG, NAME + "onPause called:  ");
        super.onPause();
        if (mCodeScanner != null) {
            mCodeScanner.releaseResources();
        }
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    public void onNeverCamera() {
        Log.i(TAG, NAME + "onNeverCamera called:  ");
        GlobalMethods.showPermissionAlertDialog(this,
                "Permission not available",
                "Do you want to allow permission? ",
                "Yes",
                (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    // Go to app settings
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", getPackageName(), null));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                },
                "No", (dialogInterface, i) -> dialogInterface.dismiss(), false);

    }

    @OnShowRationale(Manifest.permission.CAMERA)
    public void onShowRationaleCamera(final PermissionRequest request) {
        Log.i(TAG, NAME + "onShowRationaleCamera: called");
        GlobalMethods.showPermissionAlertDialog(this,
                "Permission Needed",
                "Camera permission is required to scan qr code",
                "Ok",
                (dialog, which) -> request.proceed()
                , "cancel",
                (dialog, which) -> finish(),
                false);
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    public void onPermissionDeniedCamera() {
        Log.i(TAG, NAME + "onPermissionDeniedCamera: called");
        QrScannerActivityPermissionsDispatcher.initWithPermissionCheck(this);
    }

}
