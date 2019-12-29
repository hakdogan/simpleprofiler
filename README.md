# SIMPLE PROFILER
[![Build Status](https://travis-ci.org/hakdogan/simpleprofiler.svg?branch=master)](https://travis-ci.org/hakdogan/simpleprofiler)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.kodcu%3Asimple-profiler&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.kodcu%3Asimple-profiler)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=com.kodcu%3Asimple-profiler&metric=coverage)](https://sonarcloud.io/dashboard?id=com.kodcu%3Asimple-profiler)

This application demonstrates how to use custom annotations and processing them in around a simple profiling example.

## How to Use

For short information

```java
@Monitor(MonitorPolicy.SHORT)
    private static void getUserInfo(){
        // //Make HTTP request...
    }
```

Result of
```
Total execution time of getShortUserInfo method is 326 ms
```

For detailed information
```java
@Monitor(MonitorPolicy.DETAILED)
    private static void getUserInfo(){
        // //Make HTTP request...
    }
```

Result of
```
The getDetailUserInfo method start time is 2019-12-29 15:43:21 end time is 2019-12-29 15:43:22 , total execution time as milliseconds is 351
```