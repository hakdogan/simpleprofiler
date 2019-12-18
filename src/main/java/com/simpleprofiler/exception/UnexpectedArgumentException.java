package com.simpleprofiler.exception;

/**
 * @author hakdogan (hakdogan@kodcu.com)
 * Created on 17.12.2019
 **/
@SuppressWarnings("serial")
public class UnexpectedArgumentException extends MeasurementException
{
    /**
     *
     * @param message
     */
    public UnexpectedArgumentException(String message) {
        super(message);
    }
}
