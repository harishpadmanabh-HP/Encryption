package com.example.lenovo.encryption;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 */

public class Enco {
    private static FileOutputStream output;
    private static File outputF;
    private static KeyGenerator kgen;
    private static SecretKey secretKey;
    private static Cipher cipher;
    private static CipherOutputStream cos;
    private static String algorithm = "AES";
    private static String SECRET_KEY = "secret1234567890"; // 16 lenght - secret

    /***  initilising **/
    public Enco() {
        try {
            kgen = KeyGenerator.getInstance(algorithm);
            kgen.init(256);
            secretKey = new SecretKeySpec(SECRET_KEY.getBytes(),algorithm);
            cipher = Cipher.getInstance(algorithm);
        } catch (Exception e) {
        }
    }

    /** set output path for encrypted file saving **/
    private void setOutputFilePath(File outputFile) {
        try {
            outputF = outputFile;
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(new byte[cipher.getBlockSize()]));
            output = new FileOutputStream(outputFile);
            cos = new CipherOutputStream(output, cipher);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }  catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    /** get cipher output stream for saving and **/
    private CipherOutputStream getCipherOutput() {
        return cos;
    }
    /** closing cipher */
    private void closeCipherOutput() {
        try {
            cos.close();
            cos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /** encoding file **/
    public void encodeFile(File inputFile, File outputFile){
        try {
            setOutputFilePath(outputFile);
            FileInputStream fis =new FileInputStream(inputFile);
            CipherOutputStream fos=getCipherOutput();
            int b;
            byte[] d = new byte[4096];
            while ((b = fis.read(d)) != -1) {
                fos.write(d, 0, b);
            }

            closeCipherOutput();
            fis.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    /** encoding bitmap **/


    public void encodeBitmap(Bitmap bitmap,File outputFile){
        try {
            setOutputFilePath(outputFile);
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
            CipherOutputStream fos=getCipherOutput();
            fos.write(baos.toByteArray());
            closeCipherOutput();
            baos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /** decode file **/

    public void decodFile(File inputFile, File outputFile){
        try {
            FileInputStream fis = new FileInputStream(inputFile);
            FileOutputStream fos = new FileOutputStream(outputFile);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(new byte[cipher.getBlockSize()]));

            CipherInputStream cis = new CipherInputStream(fis, cipher);
            int b;
            byte[] d = new byte[4096];
            while ((b = cis.read(d)) != -1) {
                fos.write(d, 0, b);
            }

            fos.flush();
            fos.close();
            cis.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public Bitmap decodeFile(File inputFile){
        byte[] result = new byte[4096];
        Bitmap bitmap=null;
        try {
            FileInputStream fis = new FileInputStream(inputFile);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(new byte[cipher.getBlockSize()]));

            CipherInputStream cis = new CipherInputStream(fis, cipher);
            int b;
            byte[] d = new byte[4096];
            while ((b = cis.read(d)) != -1) {
                result=d;
                bitmap= BitmapFactory.decodeByteArray(result, 0, result.length);

            }
            cis.close();
            return bitmap;
        }catch (Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }

    public File decodeFile(Context context,File input){
        File temFile=null;
        try {
            temFile = File.createTempFile("TCL", ".png", context.getCacheDir());
            temFile.deleteOnExit();

            FileInputStream fis = new FileInputStream(input);
            FileOutputStream fos = new FileOutputStream(temFile);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(new byte[cipher.getBlockSize()]));

            CipherInputStream cis = new CipherInputStream(fis, cipher);
            int b;
            byte[] d = new byte[4096];
            while ((b = cis.read(d)) != -1) {
                fos.write(d, 0, b);
            }

            fos.flush();
            fos.close();
            cis.close();
        }catch (Exception e){

        }
        return temFile;
    }


    public File decodeFile(Context context,boolean fileDecy,File file) {
        byte[] decrypted = null;
        File temFile=null;
        try {

            temFile = File.createTempFile("TCL", ".png", context.getCacheDir());
            temFile.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(temFile);

            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(new byte[cipher.getBlockSize()]));
            decrypted = cipher.doFinal(readFile(file));

            fos.write(decrypted);
            fos.close();

        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return temFile;
    }


    private byte[] readFile(File file) {
        byte[] contents = null;

        int size = (int) file.length();
        contents = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            try {
                buf.read(contents);
                buf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return contents;
    }

    public static SecretKey generateKey(char[] passphraseOrPin, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Number of PBKDF2 hardening rounds to use. Larger values increase
        // computation time. You should select a value that causes computation
        // to take >100ms.
        final int iterations = 1000;

        // Generate a 256-bit key
        final int outputKeyLength = 256;

        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec keySpec = new PBEKeySpec(passphraseOrPin, salt, iterations, outputKeyLength);
        SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
        return secretKey;
    }
}