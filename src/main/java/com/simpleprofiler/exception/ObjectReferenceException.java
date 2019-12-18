package com.simpleprofiler.exception;

/**
 * @author hakdogan (hakdogan@kodcu.com)
 * Created on 17.12.2019
 **/
@SuppressWarnings("serial")
public class ObjectReferenceException extends MeasurementException
{
    /**
     *
     * @param message
     */
    public ObjectReferenceException(String message) {
        super(message);
    }
}
