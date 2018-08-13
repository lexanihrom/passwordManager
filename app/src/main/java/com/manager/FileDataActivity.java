package com.manager;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class FileDataActivity extends AppCompatActivity {

    private EditText mFile;
    String DIR_SD = "passwordManager";
    String FILENAME_SD = "passwords";
    String FileNameAdd = "newCriptoFile";
    private Button mSaveButton;
    String fileName;
    Uri fileNameUri;
    boolean isGoogleFile;
    FingerprintManagerCompat.AuthenticationResult result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filedata);
        mFile = (EditText) findViewById(R.id.file);
        mSaveButton = (Button) findViewById(R.id.buttonSave);
        Intent intent = getIntent();
        fileName = intent.getStringExtra("filename");
        fileNameUri = Uri.parse(fileName);
        String[] subString = fileName.split("/");
        if (subString[subString.length - 1].contains(":")) {
            String [] d = subString[subString.length - 1].split(":");
            FILENAME_SD = d[d.length - 1];
        } else {
            FILENAME_SD = subString[subString.length - 1];
        }
        subString = fileName.split(":");
        subString = subString[subString.length - 1].split("/");
        DIR_SD = "";
        if (subString.length > 1) {
            for (int i = 0; i <= subString.length - 2; i++) {
                DIR_SD += subString[i];
            }
        }
        if (FILENAME_SD.equalsIgnoreCase("googleDriveFile")) {
            isGoogleFile = true;
            mSaveButton.setText("Back");
            mFile.setFocusable(false);
            mFile.setClickable(true);
        } else {
            isGoogleFile = false;
            mSaveButton.setText("Save");
            mFile.setFocusable(true);
            mFile.setClickable(false);

        }
        readData();
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (isGoogleFile) {
                        File file = new File(Environment.getExternalStorageDirectory()+"/passwordManager/googleDriveFile");
                        file.delete();
                    } else {
                        encrypt();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                }
                goToMainActivity();
            }
        });
    }

    @Override
    public void onBackPressed() {
        try {
            if (isGoogleFile) {
                File file = new File(Environment.getExternalStorageDirectory()+"/passwordManager/googleDriveFile");
                file.delete();
            } else {
                encrypt();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        goToMainActivity();
    }

    public void goToMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void readData() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return;
        }
        try {
            decrypt();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        File sdPath = Environment.getExternalStorageDirectory();
        String firstFileName = sdPath + "/" + DIR_SD + "/";
        File sdFile = new File(firstFileName, FILENAME_SD);
        StringBuilder stringBuilder = new StringBuilder();
        String str = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(sdFile));
            while ((str = br.readLine()) != null) {
                stringBuilder.append(str + "\n");
            }
            String www = stringBuilder.toString();
            mFile.setText(www);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void encrypt() throws IOException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException {
        File sdPath = Environment.getExternalStorageDirectory();
        String firstFileName = sdPath + "/" + DIR_SD + "/";
        sdPath = new File(firstFileName);
        sdPath.mkdirs();
        File sdFile = new File(sdPath, FILENAME_SD);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
            bw.write(mFile.getText().toString());
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        File extStore = Environment.getExternalStorageDirectory();
        firstFileName = extStore + "/" + DIR_SD + "/" + FILENAME_SD;
        String newFileName = extStore + "/"+ DIR_SD + "/" + FileNameAdd;
        FileInputStream fis = new FileInputStream(firstFileName);
        FileOutputStream fos = new FileOutputStream(newFileName);
        SecretKeySpec sks = new SecretKeySpec("MyDifficultPassw".getBytes(),
                "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, sks);
        CipherOutputStream cos = new CipherOutputStream(fos, cipher);
        int b;
        byte[] d = new byte[8];
        while ((b = fis.read(d)) != -1) {
            cos.write(d, 0, b);
        }
        cos.flush();
        cos.close();
        fis.close();
        File file = new File(firstFileName);
        if(file.delete()) {
            System.out.println(firstFileName);
        }
        File file2 = new File(newFileName);
        if(file2.exists()) {
            file2.renameTo(new File(firstFileName));
        }
    }

    void decrypt() throws IOException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException {
        File extStore = Environment.getExternalStorageDirectory();
        String firstFileName = extStore + "/" + DIR_SD + "/" + FILENAME_SD;
        String newFileName = extStore + "/"+ DIR_SD + "/" + FileNameAdd;
        FileInputStream fis = new FileInputStream(firstFileName);
        FileOutputStream fos = new FileOutputStream(newFileName);
        SecretKeySpec sks = new SecretKeySpec("MyDifficultPassw".getBytes(),
                "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, sks);
        CipherInputStream cis = new CipherInputStream(fis, cipher);
        int b;
        byte[] d = new byte[8];
        while ((b = cis.read(d)) != -1) {
            fos.write(d, 0, b);
        }
        fos.flush();
        fos.close();
        cis.close();
        File file = new File(firstFileName);
        if(file.delete()) {
            System.out.println(firstFileName);
        }
        File file2 = new File(newFileName);
        if(file2.exists()) {
            file2.renameTo(new File(firstFileName));
        }
    }
}
