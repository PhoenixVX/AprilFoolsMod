package me.zero.twentywfourteeninfinite.util;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class DimHash {
    private static String lastPasshphrase = "";

    public static int getHash(String string) {
        lastPasshphrase = string;
        return Hashing.sha256().hashString(string + ":why_so_salty#LazyCrypto", StandardCharsets.UTF_8).asInt() & Integer.MAX_VALUE;
    }

    public static String getLastPasshphrase() {
        return lastPasshphrase;
    }
}