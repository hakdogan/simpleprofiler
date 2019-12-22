package com.simpleprofiler;

import com.simpleprofiler.exception.MissingArgumentException;
import com.simpleprofiler.exception.ObjectReferenceException;
import com.simpleprofiler.exception.TypeMismatchException;
import com.simpleprofiler.exception.UnexpectedArgumentException;
import com.simpleprofiler.measure.Profiler;
import com.simpleprofiler.mock.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author hakdogan (hakdogan@kodcu.com)
 * Created on 20.12.2019
 **/

public class ProfilerTest
{
    private static final String DETAILED_RETURN_TEXT_PREFIX = "Total execution time of";

    @Test
    public void shortResultTest(){
        Assertions.assertFalse(Profiler.executorByMethodName(new User(), "getShortUserInfo")
                .contains(DETAILED_RETURN_TEXT_PREFIX));
    }

    @Test
    public void detailedResultTest(){
        Assertions.assertTrue(Profiler.executorByMethodName(new User(), "getDetailUserInfo")
                .contains(DETAILED_RETURN_TEXT_PREFIX));
    }

    @Test
    public void missingArgumentExceptionTest(){
        Assertions.assertThrows(MissingArgumentException.class, () -> {
            Profiler.executor(new User());
        });
    }

    @Test
    public void unexpectedArgumentExceptionTest(){
        Assertions.assertThrows(UnexpectedArgumentException.class, () -> {
            Profiler.executor(new User(), "getUserInfoWithId");
        });
    }

    @Test
    public void objectReferenceExceptionTest(){
        Assertions.assertThrows(ObjectReferenceException.class, () -> {
            Profiler.executor(null);
        });
    }

    @Test
    public void typeMismatchExceptionTest(){
        Assertions.assertThrows(TypeMismatchException.class, () -> {
            Profiler.executorByMethodName(new User(), "getUserInfoWithId", "invalidArgument");
        });
    }
}
