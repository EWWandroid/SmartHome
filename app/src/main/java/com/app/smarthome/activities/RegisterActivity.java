package com.app.smarthome.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.app.smarthome.R;
import com.app.smarthome.databinding.ActivityRegisterBinding;
import com.app.smarthome.retrofit.RetrofitClient;
import com.app.smarthome.retrofit.model.main.ModelRegisterResponse;
import com.app.smarthome.util.Constants;
import com.app.smarthome.util.GlobalMethods;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.ResponseBody;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.os.Environment.getExternalStoragePublicDirectory;

@RuntimePermissions
public class RegisterActivity extends AppCompatActivity implements Constants {

    private static final String NAME = RegisterActivity.class.getSimpleName();
    private static final String TAG = COMMON_TAG;
    private ActivityRegisterBinding registerBinding;

    private final int REQUEST_GALLERY = 0, REQUEST_CAMERA = 1;

    private Bitmap profileBitmap;
    private String pathToFile;
    private String firstName;
    private String emailName;
    private String password;
    private Gson gson;


    /*todo: api call to sign up login headers postman and missing icons*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = registerBinding.getRoot();
        setContentView(view);

        init();
    }

    private void init() {

        registerBinding.civSignupPhoto.setOnClickListener(v -> showDialogForImageSelection(RegisterActivity.this));

        registerBinding.btnSignupSignup.setOnClickListener(v -> {
            if (GlobalMethods.isNetworkAvailable(this)) {
                if (validateInputsAndShowError()) {
                    callEmailRegisterApi();
                }
            } else {
                GlobalMethods.showNetworkErrorSnackBar(registerBinding.clRegisterRoot);
            }
        });

        registerBinding.tvSignupSignin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });
    }

    private void callEmailRegisterApi() {
        Log.i(TAG, NAME + "callEmailRegisterApi called");
        GlobalMethods.showProgressBar(registerBinding.pbLogin);
        gson = new Gson();

        Log.i(TAG, NAME + "register parameters:\n" + "\t" + "firstName" + "\t" + firstName + "\t" + "emailName" + "\t" + emailName + "\t" + "password" + "\t" + password);

        Call<ResponseBody> emailLoginCall = RetrofitClient.getServiceApi().register(emailName, password, firstName);

        emailLoginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i(TAG, NAME + "onResponse called" + "response code: " + response.code());

                if (response.code() == 200) {
                    if (response.body() != null) {
                        try {
                            String in = response.body().string();
                            Log.i(TAG, NAME + "onResponse : email register response is \n" + in);
                            ModelRegisterResponse registerResponse = gson.fromJson(in, ModelRegisterResponse.class);

                            if (registerResponse.getStatus()) {
                                //go to login screen
                                Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                //invalid details
                                Log.i(TAG, NAME + "onResponse: message is " + registerResponse.getMessage());
                                Toast.makeText(RegisterActivity.this, registerResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.i(TAG, NAME + "onResponse called" + "exception caught");
                            e.printStackTrace();
                        }
                    }
                } else {
                    Log.i(TAG, NAME + "onResponse: response code: " + response.code());
                    Toast.makeText(RegisterActivity.this, "invalid response code", Toast.LENGTH_SHORT).show();
                }
                GlobalMethods.hideProgressBar(registerBinding.pbLogin);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, NAME + "onFailure called");
                Toast.makeText(RegisterActivity.this, "failure to register user", Toast.LENGTH_SHORT).show();
                GlobalMethods.hideProgressBar(registerBinding.pbLogin);
            }
        });
    }

    private boolean validateInputsAndShowError() {
        Log.i(TAG, NAME + "validateInputAndShowError: called");

        firstName = registerBinding.etLoginFirstname.getText().toString().trim();
        emailName = registerBinding.etLoginEmail.getText().toString().trim();
        password = registerBinding.etLoginPassword.getText().toString().trim();

        if (!isFirstNameValid(firstName)) {
            registerBinding.etLoginFirstname.setError(getResources().getString(R.string.field_cannot_be_empty));
            registerBinding.etLoginFirstname.requestFocus();
            return false;
        }

        if (!isEmailValid(emailName)) {
            registerBinding.etLoginEmail.setError(getResources().getString(R.string.field_cannot_be_empty));
            registerBinding.etLoginEmail.requestFocus();
            return false;
        }

        if (!isPasswordValid(password)) {
            registerBinding.etLoginPassword.setError(getResources().getString(R.string.pass_length_error));
            registerBinding.etLoginPassword.requestFocus();
            return false;
        }
        return true;
    }

    private boolean isPasswordValid(String password) {
        return !TextUtils.isEmpty(password)
                && password.length() > 4;
    }

    private boolean isEmailValid(String emailName) {
        return !TextUtils.isEmpty(emailName)
                && Patterns.EMAIL_ADDRESS.matcher(emailName).matches();
    }

    private boolean isFirstNameValid(String firstName) {
        return !TextUtils.isEmpty(firstName);
    }

    void showDialogForImageSelection(Context context) {
        Log.i(TAG, NAME + "showDialogForImageSelection: called");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select Action");

        String[] options = {"Camera", "Gallery", "Cancel"};
        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0:
                    RegisterActivityPermissionsDispatcher.takePhotoFromCameraWithPermissionCheck(this);
                    break;
                case 1:
                    RegisterActivityPermissionsDispatcher.chooseFromGalleryWithPermissionCheck(this);
                    break;
                case 2:
                    dialog.dismiss();
                    break;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void takePhotoFromCamera() {
        Log.i(TAG, NAME + "takePhotoFromCamera: called");

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        //this checks if there is an app for launching camera or not
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;
            photoFile = getPhotoFile();

            if (photoFile != null) {
                //using this path we set image to our imageview
                pathToFile = photoFile.getAbsolutePath();
                Log.i(TAG, NAME + "takePhotoFromCamera: absolute path is " + pathToFile);
                Uri photoURI = FileProvider.getUriForFile(RegisterActivity.this
                        , "com.app.smarthome.activities.fileprovider"
                        , photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, REQUEST_CAMERA);
            } else {
                Log.i(TAG, NAME + "takePhotoFromCamera: photofile is" + photoFile);
            }
        }
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void chooseFromGallery() {
        Log.i(TAG, NAME + "chooseFromGallery: called");
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, REQUEST_GALLERY);
    }

    private File getPhotoFile() {
        Log.i(TAG, NAME + "getPhotoFile: called");
        String photoName = new SimpleDateFormat("yyyyMMdd HHmm").format(new Date());

        //path to file storage
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        Log.i(TAG, NAME + "getPhotoFile: storage path is" + storageDir);

        File image = null;
        try {
            image = File.createTempFile(photoName, ".jpg", storageDir);
            Log.i(TAG, NAME + "getPhotoFile: image file location is" + image);
        } catch (IOException e) {
            Log.i(TAG, NAME + "getPhotoFile: ");
            e.printStackTrace();
        }
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, NAME + "onActivityResult: requestcode is:" + requestCode);

        if (requestCode == REQUEST_GALLERY) {
            if (data != null) {
                Uri fileProviderURI = data.getData();
                try {
                    profileBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fileProviderURI);
                    if (profileBitmap != null) {
                        registerBinding
                                .civSignupPhoto.setImageBitmap(profileBitmap);
                    } else {
                        Log.i(TAG, NAME + "onActivityResult: profile bitmap is null");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i(TAG, NAME + "onActivityResult: exception is" + e);
                    Toast.makeText(RegisterActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }

        if (requestCode == REQUEST_CAMERA) {
            profileBitmap = BitmapFactory.decodeFile(pathToFile);
            if (profileBitmap != null) {
                registerBinding.civSignupPhoto.setImageBitmap(profileBitmap);
            } else {
                Log.i(TAG, NAME + "onActivityResult: profileBitmap is null");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(TAG, NAME + "onRequestPermissionsResult: called");

        RegisterActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnShowRationale({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void showRationaleForCamera(final PermissionRequest request) {
        Log.i(TAG, NAME + "showRationaleForCamera: called");
        showPermissionAlertDialog("Permission Needed", "Camera permission is required to take photos",
                "Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                },
                "cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();
                    }
                },
                false
        );
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void onNeverAskAgainForCamera() {
        Log.i(TAG, NAME + "onNeverAskAgainForCamera: called");
        showPermissionAlertDialog("Permission not available", "Do you want to allow permission? ",
                "Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        // Go to app settings
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", getPackageName(), null));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                },
                "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }, false);

    }

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void onCameraDenied() {
        Log.i(TAG, NAME + "onCameraDenied:  called");
        Toast.makeText(this, "You cannot access features without giving permission", Toast.LENGTH_SHORT).show();
    }

    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void showRationaleForGallery(final PermissionRequest request) {
        Log.i(TAG, NAME + "showRationaleForGallery:  called");
        showPermissionAlertDialog("Permission Needed", "External storage permission is required to select photos",
                "Ok",
                (dialog, which) -> request.proceed(),
                "cancel",
                (dialog, which) -> request.cancel(),
                false
        );

    }

    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void onNeverAskAgainForGallery() {
        Log.i(TAG, NAME + "onNeverAskAgainForGallery:  called");
        showPermissionAlertDialog("Permission not available", "Do you want to allow permission? ",
                "Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        // Go to app settings
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", getPackageName(), null));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                },
                "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }, false);
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void onGalleryDenied() {
        Log.i(TAG, NAME + "onGalleryDenied: called");
        Toast.makeText(this, "You cannot access features without giving permission", Toast.LENGTH_SHORT).show();
    }

    /**
     * @param title           permission titile
     * @param msg             permission msg
     * @param positiveLabel   positive label text
     * @param positiveOnClick
     * @param negativeLabel   negative label text
     * @param negativeOnClick
     * @param isCancelAble    back button disabled
     */
    public void showPermissionAlertDialog(String title, String msg, String positiveLabel,
                                          DialogInterface.OnClickListener positiveOnClick,
                                          String negativeLabel, DialogInterface.OnClickListener negativeOnClick,
                                          boolean isCancelAble) {
        Log.i(TAG, NAME + "showPermissionAlertDialog: ");
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(positiveLabel, positiveOnClick);
        builder.setNegativeButton(negativeLabel, negativeOnClick);
        builder.setCancelable(isCancelAble);

        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
