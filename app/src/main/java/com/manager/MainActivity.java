package com.manager;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private Button mButtonCreate;
    private Button mButtonOpenFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButtonCreate = (Button) findViewById(R.id.buttonCreate);
        mButtonOpenFile = (Button) findViewById(R.id.buttonOpenFile);
        mButtonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createLogin();
            }
        });
        mButtonOpenFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFile();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    private void createLogin() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void openFile() {
        Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
        intent1.setType("application/*");
        Toast.makeText(this, "default files: SD/passwordManager/", Toast.LENGTH_SHORT).show();
        startActivityForResult(Intent.createChooser(intent1, "Select file"),1);
    }

    private String readTextFromUri(Uri uri) throws IOException {
        InputStream fis = getContentResolver().openInputStream(uri);
        File extStore = Environment.getExternalStorageDirectory();
        String newFileName = extStore + "/passwordManager/googleDriveFile";
        FileOutputStream fos = new FileOutputStream(newFileName);
        int b;
        byte[] d = new byte[8];
        while ((b = fis.read(d)) != -1) {
            fos.write(d, 0, b);
        }
        fos.close();
        fis.close();
        return "/document/primary:passwordManager/googleDriveFile";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        String name = data.getData().getPath();
        Uri returnUri = data.getData();
        if (name.contains(";")) {
            try {
                name = readTextFromUri(returnUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (name.contains(".")){
            Toast.makeText(this, "unsupported file", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, OpenActivity.class);
        intent.putExtra("fname", name);
        startActivity(intent);
    }
}
