package com.simpleprofiler.exception;

/**
 * @author hakdogan (hakdogan@kodcu.com)
 * Created on 17.12.2019
 **/
@SuppressWarnings("serial")
public class TypeMismatchException extends MeasurementException
{
    /**
     *
     * @param message
     */
    public TypeMismatchException(String message) {
        super(message);
    }
}
