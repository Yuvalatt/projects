
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.json.JSONObject;

public class CommandFactory<K, D, T> {	
	private static volatile CommandFactory<String, JSONObject, CRUD<String, Integer>> factory;
	private Map<K, Function<D, ? extends T>> map =  new HashMap<>();
	
	private CommandFactory() {}

	public void add(K key, Function<D, ? extends T> func) {
		map.put(key,func);
	}

	public T create(K key, D data) {		
		return map.get(key).apply(data);
	}
	
	public static CommandFactory<String, JSONObject, CRUD<String, Integer>> getFactory() {
		if (null == factory) {
			synchronized (CommandFactory.class) {
				if (null == factory) {
					factory = new CommandFactory<>();
				}
			}
		}
		return factory;
	}
}