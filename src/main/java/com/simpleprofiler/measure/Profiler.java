package com.simpleprofiler.measure;

import ch.qos.logback.classic.Logger;
import com.simpleprofiler.annotation.Monitor;
import com.simpleprofiler.annotation.MonitorPolicy;
import com.simpleprofiler.exception.TypeMismatchException;
import com.simpleprofiler.exception.MissingArgumentException;
import com.simpleprofiler.exception.ObjectReferenceException;
import com.simpleprofiler.exception.UnexpectedArgumentException;
import org.slf4j.LoggerFactory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

/**
 * @author hakdogan (hakdogan@kodcu.com)
 * Created on 17.12.2019
 **/
public class Profiler
{
    private static final Logger logger = (Logger) LoggerFactory.getLogger(Profiler.class);

    private Profiler() {}

    /**
     *
     * @param object
     * @param passedParameters
     */
    public static String executor(final Object object, final Object... passedParameters) {
        checkObjectReference(object);
        final StringBuilder result = new StringBuilder();
        final Method[] methods = object.getClass().getDeclaredMethods();
        for(Method method :methods){
            if(method.isAnnotationPresent(com.simpleprofiler.annotation.Monitor.class)){
                if(passedParameters.length > 0){
                    result.append(invoker(method, object, passedParameters));
                } else {
                    result.append(invoker(method, object));
                }
            }
        }
        return result.toString();
    }

    /**
     *
     * @param object
     * @param methodName
     * @param passedParameters
     *
     * @return
     */
    public static String executorByMethodName(final Object object, final String methodName, final Object... passedParameters) {
        checkObjectReference(object);
        final StringBuilder result = new StringBuilder();
        final Method[] methods = object.getClass().getDeclaredMethods();
        for(Method method :methods){
            if(method.getName().equals(methodName) && method.isAnnotationPresent(Monitor.class)){
                if(passedParameters.length > 0){
                    result.append(invoker(method, object, passedParameters));
                } else {
                    result.append(invoker(method, object));
                }
            }
        }
        return result.toString();
    }

    /**
     *
     * @param method
     * @param object
     * @param passedParameters
     *
     * @return
     */
    private static String invoker(final Method method, Object object, Object... passedParameters) {

        method.setAccessible(true);
        final MonitorPolicy policy = method.getAnnotation(Monitor.class).value();
        long start = System.currentTimeMillis();

        try {
            if (passedParameters.length > 0) {
                checkTypeMismatch(method.getName(), method.getParameters(), passedParameters);
                method.invoke(object, passedParameters);
            } else if(method.getGenericParameterTypes().length > 0){
                throw new MissingArgumentException("The parameter(s) of the " + method.getName() + " method are missing");
            } else {
                method.invoke(object);
            }

        } catch (InvocationTargetException ite){
            logger.error("InvocationTargetException", ite);
        } catch (IllegalAccessException iae){
            logger.error("IllegalAccessException", iae);
        } catch (IllegalArgumentException iare){
            StringBuilder exMessage = new StringBuilder().append("Type mismatch. Expected type ");
            for(Parameter p :method.getParameters()){
                exMessage.append(p.getName()).append(" ").append(p.getType().getName()).append(" ");
            }
            throw new TypeMismatchException(exMessage.toString());
        }

        return printOfExecutionTime(start, System.currentTimeMillis(), method.getName(), policy);
    }

    /**
     *
     * @param object
     */
    private static void checkObjectReference(final Object object){
        if(Objects.isNull(object)){
            throw new ObjectReferenceException("Object reference can't be null");
        }
    }

    /**
     *
     * @param methodName
     * @param methodParameters
     * @param passedParameters
     */
    private static void checkTypeMismatch(final String methodName, final Parameter[] methodParameters,
                                          final Object... passedParameters){
        if(methodParameters.length == 0 || methodParameters.length != passedParameters.length){
            StringBuilder exMessage = new StringBuilder().append("Unexpected or missing argument in ")
                    .append(methodName).append(" method. Expected type ");
            for(Parameter p :methodParameters){
                exMessage.append(p.getName()).append(" ").append(p.getType().getName()).append(" ");
            }
            throw new UnexpectedArgumentException(exMessage.toString());
        }
    }

    /**
     *
     * @param startTime
     * @param endTime
     * @param methodName
     * @param policy
     *
     * @return
     */
    private static String printOfExecutionTime(final long startTime, final long endTime,
                                     final String methodName, final MonitorPolicy policy){
        final StringBuilder result = new StringBuilder();
        if(policy.equals(MonitorPolicy.SHORT)){
            result.append("Total execution time of ")
                    .append(methodName)
                    .append(" method is ")
                    .append(endTime - startTime)
                    .append(" ms");
            logger.info( "Total execution time of {} method is {} ms", methodName, (endTime - startTime));
        } else {
            final String startDateTime = convertToStringForPrint(startTime);
            final String endDateTime = convertToStringForPrint(endTime);
            result.append("The ")
                    .append(methodName)
                    .append(" method start time is ")
                    .append(startDateTime)
                    .append(" end time is ")
                    .append(endDateTime)
                    .append(", total execution time as milliseconds is ")
                    .append(endTime - startTime);
            logger.info("The {} method start time is {} end time is {} " +
                    ", total execution time as milliseconds is {}",  methodName, startDateTime,
                    endDateTime, (endTime - startTime));
        }

        return result.toString();
    }

    /**
     *
     * @param millisecond
     * @return
     */
    private static String convertToStringForPrint(final long millisecond){
        LocalDateTime dateTime =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(millisecond), ZoneId.systemDefault());

        return new StringBuilder().append(dateTime.getYear())
                .append("-")
                .append(dateTime.getMonthValue())
                .append("-")
                .append(dateTime.getDayOfMonth())
                .append(" ")
                .append(dateTime.getHour())
                .append(":")
                .append(dateTime.getMinute())
                .append(":")
                .append(dateTime.getSecond())
                .toString();
    }

}
