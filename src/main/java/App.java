import org.redisson.Redisson;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RedissonClient;

public class App {

	static final String[] VALUES = new String[200];

	static {
		for (int i = 0; i < VALUES.length; i++) {
			VALUES[i] = "my-val-" + i;
		}
	}

	private static String value(int i) {
		return VALUES[(i % VALUES.length)];
	}

	private static void forceGC() {
		for (int j = 0; j < 100; j++) {
			System.gc();
		}
	}

	public static void main(String[] args) {
		// connects to 127.0.0.1:6379
		RedissonClient redissonClient = Redisson.create();

		LocalCachedMapOptions ops = LocalCachedMapOptions.defaults()
				.cacheSize(0)
				.timeToLive(0)
				.maxIdle(0)
				.reconnectionStrategy(LocalCachedMapOptions.ReconnectionStrategy.CLEAR)
				.syncStrategy(LocalCachedMapOptions.SyncStrategy.INVALIDATE)
				.evictionPolicy(LocalCachedMapOptions.EvictionPolicy.WEAK);


		RLocalCachedMap<String, String> map = redissonClient.getLocalCachedMap("sample-cache", ops);

		for (int i = 0; i < 1_000_000_000; i++) {
			map.fastPut(value(i), value(i));
			if (i % 10_000 == 0) {
				forceGC();
				System.out.println("Puts performed " + i + " free mem: " + Runtime.getRuntime().freeMemory() + "B");
			}
		}

		redissonClient.shutdown();
	}
}
