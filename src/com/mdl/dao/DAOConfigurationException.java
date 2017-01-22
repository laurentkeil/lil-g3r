package com.mdl.dao;

public class DAOConfigurationException extends RuntimeException {   
	/**
	 * Constructeur du DAOConfigurationException et leurs surcharges
	 * @param message
	 * @author CrespeigneRomain
	 */
    public DAOConfigurationException( String message ) {
        super( message );
    }

    public DAOConfigurationException( String message, Throwable cause ) {
        super( message, cause );
    }

    public DAOConfigurationException( Throwable cause ) {
        super( cause );
    }
}
