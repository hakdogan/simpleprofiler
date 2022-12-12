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

class ProfilerTest
{
    private static final String DETAILED_RETURN_TEXT_PREFIX = "start time is";
    private static final User user = new User();

    @Test
    void testToShortResult(){
        Assertions.assertFalse(Profiler.executorByMethodName(new User(), "getShortUserInfo")
                .contains(DETAILED_RETURN_TEXT_PREFIX));
    }

    @Test
    void testToDetailedResult(){
        Assertions.assertTrue(Profiler.executorByMethodName(new User(), "getDetailUserInfo")
                .contains(DETAILED_RETURN_TEXT_PREFIX));
    }

    @Test
    void testToMissingArgumentException(){
        Assertions.assertThrows(MissingArgumentException.class, () -> Profiler.executor(user));
    }

    @Test
    void testToMissingArgumentExceptionTryCatchIdiom(){

        try {
            Profiler.executor(user);
            Assertions.fail("Expected a MissingArgumentException to be thrown");
        } catch (MissingArgumentException ex){}

    }

    @Test
    void testToUnexpectedArgumentException(){
        Assertions.assertThrows(UnexpectedArgumentException.class,
                () -> Profiler.executor(user, "getUserInfoWithId"));
    }

    @Test
    void testToUnexpectedArgumentExceptionTryCatchIdiom(){

        try {
            Profiler.executor(user, "getUserInfoWithId");
            Assertions.fail("Expected an UnexpectedArgumentException to be thrown");
        } catch (UnexpectedArgumentException ex){}

    }

    @Test
    void testToObjectReferenceException(){
        Assertions.assertThrows(ObjectReferenceException.class, () -> Profiler.executor(null));
    }

    @Test
    void testToObjectReferenceExceptionTryCatchIdiom(){

        try {

            Profiler.executor(null);
            Assertions.fail("Expected an ObjectReferenceException to be thrown");
        } catch (ObjectReferenceException ex){}

    }

    @Test
    void testToTypeMismatchException(){
        Assertions.assertThrows(TypeMismatchException.class,
                () -> Profiler.executorByMethodName(user, "getUserInfoWithId", "invalidArgument"));
    }

    @Test
    void testToTypeMismatchExceptionTryCatchIdiom(){

        try {
            Profiler.executorByMethodName(user, "getUserInfoWithId", "invalidArgument");
            Assertions.fail("Expected a TypeMismatchException to be thrown");
        } catch (TypeMismatchException ex){}

    }
}
