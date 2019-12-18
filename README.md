# SIMPLE PROFILER

This application demonstrates how to use custom annotations and processing them in around a simple profiling example.

## How to use

For short information

```java
@Monitor(Monitor.SHORTLY)
    private static void getUserInfo(){
        // //Make HTTP request...
    }
```

Result of
```
0 ms
```

For detailed information
```java
@Monitor(Monitor.DETAILED)
    private static void getUserInfo(){
        // //Make HTTP request...
    }
```

Result of
```
Total execution time of getUserInfo method is 1459 ms
```