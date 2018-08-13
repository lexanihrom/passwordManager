package com.manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import android.os.Environment;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class LoginActivity extends AppCompatActivity {

    private static final String PIN = "pin";
    private EditText mEditText;
    private EditText mEditText2;
    private EditText mEditText3;
    final String DIR_SD = "passwordManager";
    String FILENAME_SD = "passwords.pmf";
    String FileNameAdd = "newCriptoFile.pmf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEditText = (EditText) findViewById(R.id.editText);
        mEditText2 = (EditText) findViewById(R.id.editText2);
        mEditText3 = (EditText) findViewById(R.id.editText3);
        mEditText3.setText("passwords");
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEditText3.getText().length() != 0 && mEditText2.getText().length() != 0 && mEditText.getText().length() != 0) {
                    writeFileSD();
                    toMainActivity();
                } else {
                    toastLog();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private  void toastLog() {
        Toast.makeText(this, "write all data", Toast.LENGTH_SHORT).show();
    }
    private void toMainActivity() {
        Toast.makeText(this, "passwordSaved", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class));
    }

    void writeFileSD() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return;
        }
        final String pin = mEditText.getText().toString();
        final String pinName = mEditText2.getText().toString();
        FILENAME_SD = mEditText3.getText().toString();
        String pass = pin + " - " + pinName + "\n";
        File sdPath = Environment.getExternalStorageDirectory();
        String firstFileName = sdPath + "/" + DIR_SD + "/";
        sdPath = new File(firstFileName);
        if (sdPath.exists()) {
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
        }
        sdPath.mkdirs();
        File sdFile = new File(sdPath, FILENAME_SD);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile, true));
            bw.append(pass);
            bw.close();
            encrypt();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void encrypt() throws IOException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException {
        File extStore = Environment.getExternalStorageDirectory();
        String firstFileName = extStore + "/" + DIR_SD + "/" + FILENAME_SD;
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
