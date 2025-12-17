package com.portalpolinema.backend.shared;

public class SlugUtil {

    public static String slugify(String text) {
        if (text == null)
            return "";
        // ke huruf kecil
        String result = text.toLowerCase();
        // ganti spasi dan karakter non-alfanumerik dengan "-"
        result = result.replaceAll("[^a-z0-9]+", "-");
        // hapus "-" di depan/belakang
        result = result.replaceAll("^-+|-+$", "");
        return result;
    }

}