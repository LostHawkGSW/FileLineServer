package external_clients.mock_redis_server;

import static ai.grakn.redismock.Utils.deserializeObject;

import java.util.HashMap;
import java.util.List;

import ai.grakn.redismock.Response;
import ai.grakn.redismock.Slice;

/**
 * 
 * @author soltanieharya
 * Implementation of hget for redismock (missing in redismock)
 *
 */
public class RO_hget extends AbstractRedisOperation {
	RO_hget(RedisBase base, List<Slice> params) {
	    super(base, params, 2, null, null);
	}

	Slice response() {
		Slice key = params().get(0);
		Slice field = params().get(1);
		Slice data = base().rawGet(key);
		if(data == null) {
			return Response.NULL;
		}
		HashMap<Slice, Slice>  map  = deserializeObject(data);
		if(map == null) {
			return Response.NULL;
		}
		return Response.bulkString(map.get(field));
	}
}




