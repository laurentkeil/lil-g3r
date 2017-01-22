package com.mdl.parser;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Convertion {

    public static String DateToCalendar( Date date ) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime( date );
        int annee = calendar.get( Calendar.YEAR );
        int mois = calendar.get( Calendar.MONTH ) + 1;
        int jour = calendar.get( Calendar.DAY_OF_MONTH );
        int heure = calendar.get( Calendar.HOUR_OF_DAY );
        int minute = calendar.get( Calendar.MINUTE );
        int seconde = calendar.get( Calendar.SECOND );
        String affichageCorrect;
        if ( minute < 10 && seconde < 10 ) {
            affichageCorrect = jour + " / " + mois + " / " + annee + " à " + heure + ":0" + minute + ":0" + seconde;
        } else if ( minute > 9 && seconde < 10 ) {
            affichageCorrect = jour + " / " + mois + " / " + annee + " à " + heure + ":" + minute + ":0" + seconde;
        } else if ( minute < 10 && seconde > 9 ) {
            affichageCorrect = jour + " / " + mois + " / " + annee + " à " + heure + ":0" + minute + ":" + seconde;
        } else {
            affichageCorrect = jour + " / " + mois + " / " + annee + " à " + heure + ":" + minute + ":" + seconde;
        }
        return affichageCorrect;
    }

    public static String nl2br( String string ) {
        return ( string != null ) ? string.replace( "\n", "<br/>" ) : null;
    }
}
