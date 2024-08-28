package com.klodnicki.watermeter.utils;

public class ChecksumUtil {
    public static String calculateChecksum(String payload) {

        int checksum = 0;
        for (int i = 0; i < payload.length(); i += 2) {
            String byteString = payload.substring(i, i + 2);
            int byteValue = Integer.parseInt(byteString, 16);
            checksum = (checksum + byteValue) % 256;
        }
        return String.format("%02X", checksum);
    }

}

