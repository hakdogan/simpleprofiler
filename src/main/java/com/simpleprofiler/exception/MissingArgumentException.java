package com.simpleprofiler.exception;

/**
 * @author hakdogan (hakdogan@kodcu.com)
 * Created on 17.12.2019
 **/
@SuppressWarnings("serial")
public class MissingArgumentException extends MeasurementException
{
    /**
     *
     * @param message
     */
    public MissingArgumentException(String message) {
        super(message);
    }
}
