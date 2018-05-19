# redisson-mem-leak
Redisson (3.7.0) memory leak when using ```EvictionPolicy.WEAK``` for RLocalCachedMap


### Steps to reproduce
* Run Redis locally
``` 
docker run --rm -p 6379:6379 redis 
```
* Start the app
``` 
gradle run 
```

* Wait ca 2 minutes. After around 50k puts you should get
```
java.lang.OutOfMemoryError: GC overhead limit exceeded
java.lang.OutOfMemoryError: Java heap space
```

### Tested with
* Java(TM) SE Runtime Environment (build 1.8.0_162-b12)
* Java(TM) SE Runtime Environment (build 9.0.4+11)
