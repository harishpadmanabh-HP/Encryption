package com.example.lenovo.encryption;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;


import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

@SuppressLint("ValidFragment")
class OneFragment extends Fragment {
    byte[] byteArray;ImageView imageView; ImageView enimg;
    Bitmap selectedImage,newbitmap;

    ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.fragment_menu_1, container, false);
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setTitle("ENCRYPTING");
        Button choose = (Button) view.findViewById(R.id.chooseencrypt);
        Button encrypt = (Button) view.findViewById(R.id.encryptbutton);
          enimg = (ImageView) view.findViewById(R.id.encryptimg);
        ImageView imageView;
         //Bitmap newbitmap;

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
      encrypt.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              ProgressDialog dialog = ProgressDialog.show(getActivity(), "Loading...", "Please wait...", true);

//              progressDialog.show();
              newbitmap=selectedImage;
              convertimagetobyteonclick(newbitmap);
              KeyGenerator keyGenerator = null;
              try {
                  keyGenerator = KeyGenerator.getInstance("AES");
                  keyGenerator.init(128);
                  Key key = keyGenerator.generateKey();
                  System.out.println(key);

                  byte[] content =         convertimagetobyteonclick(newbitmap);

                  System.out.println(content);

                  byte[] encrypted = encryptPdfFile(key, content);
                  System.out.println(encrypted);
//progressDialog.dismiss();
                  dialog.dismiss();
                  byte[] decrypted = decryptPdfFile(key, encrypted);
                  System.out.println(decrypted);

                  saveFile(encrypted);
                  Toast.makeText(getContext(), "File saved in file manasger", Toast.LENGTH_SHORT).show();
                  System.out.println("Done");
              } catch (NoSuchAlgorithmException e) {
                  e.printStackTrace();
              } catch (IOException e) {
                  e.printStackTrace();
              }




          }
      });

        return view;

    }


    public static byte[] encryptPdfFile(Key key, byte[] content) {
        Cipher cipher;
        byte[] encrypted = null;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            encrypted = cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encrypted;

    }

    public static byte[] decryptPdfFile(Key key, byte[] textCryp) {
        Cipher cipher;
        byte[] decrypted = null;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            decrypted = cipher.doFinal(textCryp);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return decrypted;
    }

    public static void saveFile(byte[] bytes) throws IOException {

        FileOutputStream fos = new FileOutputStream("/sdcard/harishnew.jpg");
        fos.write(bytes);
        fos.close();

    }


    private void selectImage() {

        Intent galleryintent = new Intent();
        galleryintent.setType("image/*");
        galleryintent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryintent, "SELECT IMAGE"), 100);
    }

    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream =getContext(). getContentResolver().openInputStream(imageUri);
                selectedImage = BitmapFactory.decodeStream(imageStream);
                byte[] newtest=      convertimagetobyteonclick(selectedImage);
                enimg.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(getContext(), "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }


    @SuppressWarnings("deprecation")
    public String getPath(Uri uri, Activity activity) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);

    }
    private byte[] convertimagetobyteonclick(Bitmap newbitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        newbitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageInByte = baos.toByteArray();
        return imageInByte;


    }


}






