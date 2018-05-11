package external_clients.mock_redis_server;

import static ai.grakn.redismock.Utils.deserializeObject;
import static ai.grakn.redismock.Utils.serializeObject;

import java.util.HashMap;
import java.util.List;

import ai.grakn.redismock.Response;
import ai.grakn.redismock.Slice;

/**
 * 
 * @author soltanieharya
 * Implementation of hset for redismock (missing in redismock)
 *
 */
public class RO_hset extends AbstractRedisOperation {
	RO_hset(RedisBase base, List<Slice> params) {
        super(base, params, 3, null, null);
    }

    Slice response() {
    	Slice key = params().get(0);
    	Slice field = params().get(1);
    	Slice value = params().get(2);
    	Slice data = base().rawGet(key);
    	HashMap<Slice, Slice>  map = null;
    	if(data != null && data.length() > 0) {
    		map  = deserializeObject(data);
    	}
    	if(map == null) {
    		map = new HashMap<Slice, Slice>();
    	}
    	if(map.containsKey(field)) {
    		map.put(field, value);
        	base().rawPut(key, serializeObject(map), -1L);
        	return Response.integer(0);
    	} else {
    		map.put(field, value);
        	base().rawPut(key, serializeObject(map), -1L);
        	return Response.integer(1);
    	}
    }
}
