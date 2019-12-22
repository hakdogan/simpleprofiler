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
        final StringBuilder result = new StringBuilder();
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

            final long end = System.currentTimeMillis();
            if(policy.equals(MonitorPolicy.SHORT)){
                result.append(end - start).append(" ms");
                logger.info( "{} ms", (end - start));
            } else {
                result.append("Total execution time of ")
                        .append(method.getName())
                        .append(" method is ")
                        .append(end - start)
                        .append(" ms");
                logger.info("Total execution time of {} method is {} ms",  method.getName(), (end - start));
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

        return result.toString();
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
    private static void checkTypeMismatch(final String methodName, final Parameter[] methodParameters, final Object... passedParameters){
        if(methodParameters.length == 0 || methodParameters.length != passedParameters.length){
            StringBuilder exMessage = new StringBuilder().append("Unexpected or missing argument in ")
                    .append(methodName).append(" method. Expected type ");
            for(Parameter p :methodParameters){
                exMessage.append(p.getName()).append(" ").append(p.getType().getName()).append(" ");
            }
            throw new UnexpectedArgumentException(exMessage.toString());
        }
    }
}
