package com.mdl.dao;

public class DAOException extends RuntimeException {
	/**
	 * Constructeur du DAOException et leurs surcharges
	 * @param message
	 * @author CrespeigneRomain
	 */
    public DAOException( String message ) {
        super( message );
    }

    public DAOException( String message, Throwable cause ) {
        super( message, cause );
    }

    public DAOException( Throwable cause ) {
        super( cause );
    }
}
