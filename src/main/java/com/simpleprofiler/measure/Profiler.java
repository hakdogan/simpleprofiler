package com.simpleprofiler.measure;

import ch.qos.logback.classic.Logger;
import com.simpleprofiler.annotation.Monitor;
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
     * @param o
     * @param parameter
     */
    public static void executor(final Object o, final Object... parameter) {

        checkObjectReference(o);
        final Method[] methods = o.getClass().getDeclaredMethods();
        for(Method m :methods){
            if(m.isAnnotationPresent(com.simpleprofiler.annotation.Monitor.class)){
                if(parameter.length > 0){
                    invoker(m, o, parameter);
                } else {
                    invoker(m, o);
                }
            }
        }
    }

    /**
     *
     * @param o
     * @param name
     * @param parameter
     */
    public static void executor(final Object o, final String name, final Object... parameter) {
        checkObjectReference(o);
        final Method[] methods = o.getClass().getDeclaredMethods();
        for(Method m :methods){
            if(m.getName().equals(name) && m.isAnnotationPresent(Monitor.class)){
                if(parameter.length > 0){
                    invoker(m, o, parameter);
                } else {
                    invoker(m, o);
                }
            }
        }
    }

    /**
     *
     * @param method
     * @param o
     * @param parameters
     */
    private static void invoker(final Method method, Object o, Object... parameters) {

        method.setAccessible(true);
        final String value = method.getAnnotation(Monitor.class).value();
        long start = System.currentTimeMillis();

        try {

            if (parameters.length > 0) {
                checkTypeMismatch(method.getName(), method.getParameters(), parameters);
                method.invoke(o, parameters);
            } else if(method.getGenericParameterTypes().length > 0){
                throw new MissingArgumentException("The parameter(s) of the " + method.getName() + " method are missing");
            } else {
                method.invoke(o);
            }

            final long end = System.currentTimeMillis();
            if(value.equals("shortly")){
                logger.info( "{} ms", (end - start));
            } else {
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
     * @param name
     * @param parameters
     * @param arguments
     */
    private static void checkTypeMismatch(final String name, final Parameter[] parameters, final Object... arguments){
        if(parameters.length == 0 || parameters.length != arguments.length){
            StringBuilder exMessage = new StringBuilder().append("Unexpected or missing argument in ")
                    .append(name).append(" method. Expected type ");
            for(Parameter p :parameters){
                exMessage.append(p.getName()).append(" ").append(p.getType().getName()).append(" ");
            }
            throw new UnexpectedArgumentException(exMessage.toString());
        }

    }
}
