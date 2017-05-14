package com.adelmo.raid.domain.fdfs.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigestUtil {
    private static char[] base64EncodeChars = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
    private static byte[] base64DecodeChars = new byte[]{(byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)62, (byte)-1, (byte)-1, (byte)-1, (byte)63, (byte)52, (byte)53, (byte)54, (byte)55, (byte)56, (byte)57, (byte)58, (byte)59, (byte)60, (byte)61, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)0, (byte)1, (byte)2, (byte)3, (byte)4, (byte)5, (byte)6, (byte)7, (byte)8, (byte)9, (byte)10, (byte)11, (byte)12, (byte)13, (byte)14, (byte)15, (byte)16, (byte)17, (byte)18, (byte)19, (byte)20, (byte)21, (byte)22, (byte)23, (byte)24, (byte)25, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)26, (byte)27, (byte)28, (byte)29, (byte)30, (byte)31, (byte)32, (byte)33, (byte)34, (byte)35, (byte)36, (byte)37, (byte)38, (byte)39, (byte)40, (byte)41, (byte)42, (byte)43, (byte)44, (byte)45, (byte)46, (byte)47, (byte)48, (byte)49, (byte)50, (byte)51, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1};
    static char[] hexdigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public DigestUtil() {
    }

    public static String getMd5Digest(String cleanText) {
        MessageDigest digest = null;

        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException var5) {
            return null;
        }

        Object hashBytes = null;

        byte[] hashBytes1;
        try {
            digest.update(cleanText.getBytes());
            hashBytes1 = digest.digest();
        } catch (Exception var4) {
            return null;
        }

        String result = encodeHex(hashBytes1);
        return result;
    }

    public static String getMd5DigestFileByReadFile(String filePath) {
        MessageDigest digest = null;
        String str = "";
        String data = "";

        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException var6) {
            return null;
        }

        Object hashBytes = null;

        byte[] hashBytes1;
        try {
            BufferedReader result = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));

            while(true) {
                if((data = result.readLine()) == null) {
                    digest.update(str.getBytes());
                    hashBytes1 = digest.digest();
                    break;
                }

                str = str + data;
            }
        } catch (Exception var7) {
            return null;
        }

        String result1 = encodeHex(hashBytes1);
        return result1;
    }

    public static String getMd5DigestFile(String filePath) {
        FileInputStream fis = null;

        Object buffer;
        try {
            MessageDigest ex = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(new File(filePath));
            byte[] buffer1 = new byte[2048];
            boolean ex1 = true;
            long s = System.currentTimeMillis();

            int ex2;
            while((ex2 = fis.read(buffer1)) != -1) {
                ex.update(buffer1, 0, ex2);
            }

            byte[] b = ex.digest();
            String var8 = byteToHexString(b);
            return var8;
        } catch (Exception var18) {
            var18.printStackTrace();
            buffer = null;
        } finally {
            try {
                fis.close();
            } catch (IOException var17) {
                var17.printStackTrace();
            }

        }

        return (String)buffer;
    }

    private static String byteToHexString(byte[] tmp) {
        char[] str = new char[32];
        int k = 0;

        for(int i = 0; i < 16; ++i) {
            byte byte0 = tmp[i];
            str[k++] = hexdigits[byte0 >>> 4 & 15];
            str[k++] = hexdigits[byte0 & 15];
        }

        String s = new String(str);
        return s;
    }

    private static String encodeHex(byte[] bytes) {
        StringBuffer hex = new StringBuffer(bytes.length * 2);

        for(int i = 0; i < bytes.length; ++i) {
            if((bytes[i] & 255) < 16) {
                hex.append("0");
            }

            hex.append(Integer.toString(bytes[i] & 255, 16));
        }

        return hex.toString().toUpperCase();
    }

    public static String encode(byte[] data) {
        StringBuffer sb = new StringBuffer();
        int len = data.length;
        int i = 0;

        while(i < len) {
            int b1 = data[i++] & 255;
            if(i == len) {
                sb.append(base64EncodeChars[b1 >>> 2]);
                sb.append(base64EncodeChars[(b1 & 3) << 4]);
                sb.append("==");
                break;
            }

            int b2 = data[i++] & 255;
            if(i == len) {
                sb.append(base64EncodeChars[b1 >>> 2]);
                sb.append(base64EncodeChars[(b1 & 3) << 4 | (b2 & 240) >>> 4]);
                sb.append(base64EncodeChars[(b2 & 15) << 2]);
                sb.append("=");
                break;
            }

            int b3 = data[i++] & 255;
            sb.append(base64EncodeChars[b1 >>> 2]);
            sb.append(base64EncodeChars[(b1 & 3) << 4 | (b2 & 240) >>> 4]);
            sb.append(base64EncodeChars[(b2 & 15) << 2 | (b3 & 192) >>> 6]);
            sb.append(base64EncodeChars[b3 & 63]);
        }

        return sb.toString();
    }

    public static byte[] decode(String str) throws UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer();
        byte[] data = str.getBytes("US-ASCII");
        int len = data.length;
        int i = 0;

        while(i < len) {
            byte b1;
            do {
                b1 = base64DecodeChars[data[i++]];
            } while(i < len && b1 == -1);

            if(b1 == -1) {
                break;
            }

            byte b2;
            do {
                b2 = base64DecodeChars[data[i++]];
            } while(i < len && b2 == -1);

            if(b2 == -1) {
                break;
            }

            sb.append((char)(b1 << 2 | (b2 & 48) >>> 4));

            byte b3;
            do {
                b3 = data[i++];
                if(b3 == 61) {
                    return sb.toString().getBytes("iso8859-1");
                }

                b3 = base64DecodeChars[b3];
            } while(i < len && b3 == -1);

            if(b3 == -1) {
                break;
            }

            sb.append((char)((b2 & 15) << 4 | (b3 & 60) >>> 2));

            byte b4;
            do {
                b4 = data[i++];
                if(b4 == 61) {
                    return sb.toString().getBytes("iso8859-1");
                }

                b4 = base64DecodeChars[b4];
            } while(i < len && b4 == -1);

            if(b4 == -1) {
                break;
            }

            sb.append((char)((b3 & 3) << 6 | b4));
        }

        return sb.toString().getBytes("iso8859-1");
    }

    public static void main(String[] args) {
        String abc = getMd5DigestFile("d:/1.vx2");
        System.out.print(abc);
    }
}
