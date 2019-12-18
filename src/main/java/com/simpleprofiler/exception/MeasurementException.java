package com.simpleprofiler.exception;

/**
 * @author hakdogan (hakdogan@kodcu.com)
 * Created on 17.12.2019
 **/
@SuppressWarnings("serial")
public class MeasurementException extends RuntimeException
{
    /**
     *
     * @param message
     */
    public MeasurementException(String message) {
        super(message);
    }

    /**
     *
     * @param message
     * @param cause
     */
    public MeasurementException(String message, Throwable cause) {
        super(message, cause);
    }
}
