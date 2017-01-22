package com.mdl.parser;

import java.util.UUID;

public class GenerateICU {

    /**
     * Constructeur
     */
    public GenerateICU() {

    }

    public String ICUGenColis() {

        String icu = UUID.randomUUID().toString();
        icu = "0" + icu;
        icu = icu.toUpperCase();
        icu = icu.replaceAll( "-", "" );
        icu = trimUUID( icu.toString() );

        return icu;

    }

    public String ICUGenChariot() {

        String icu = UUID.randomUUID().toString();
        icu = "1" + icu;
        icu = icu.toUpperCase();
        icu = icu.replaceAll( "-", "" );
        icu = trimUUID( icu.toString() );

        return icu;

    }

    private static String trimUUID( String uuid ) {
        char[] uuidChars = uuid.toCharArray();
        String newUUID = "";
        for ( int i = 0; i < 10; i++ ) {
            newUUID += uuidChars[i];
        }

        return newUUID;

    }
}
