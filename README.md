实现分布式锁:  
Redis(高性能,集群可能会出现并发问题)  
ZooKeeper(性能不如redis,但集群基本不会出现并发问题)  
Redlock(类似zookeeper,没有主从节点,牺牲redis高性能,不推荐)

1.synchronize 加锁  
存在问题：只能解决单体中的并发问题，无法解决集群并发  
  
2.redis设置lockKey加锁  
问题与解决方案：  

1）异常导致的死锁  
try catch finally 解决，finally中释放锁  

2）jvm宕机导致的死锁  
设置锁的超时时间  
（要保证锁和超时时间的原子性，否则在加锁与设置超时时间之间宕机，同样会造成死锁）  
redisTemplate.opsForValue().setIfAbsent(lockKey, "liang",10,TimeUnit.SECONDS)  
redis底层保证了存值和设置超时时间的原子性  

3）线程的执行时间大于超时时间  
每个线程对应唯一的锁，只能自己释放自己加的锁（前提是能容忍偶尔的执行时间大于超时时间，或者并发量不高，线程执行时间几乎不可能大于超时时间）  

4）系统压力很大，线程执行时间常常大于超时时间（设置超时时间过长，会导致系统宕机时用户等待时间过长）  
续锁 redisson实现  
// 1.获取锁对象  
RLock redissonLock = redisson.getLock(lockKey);  
// 2.加锁 .setIfAbsent(lockKey, clientId,30,TimeUnit.SECONDS)  
redissonLock.lock();  
// 3.释放锁  
redissonLock.unlock();  
