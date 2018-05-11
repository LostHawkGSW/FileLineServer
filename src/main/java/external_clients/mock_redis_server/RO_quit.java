package external_clients.mock_redis_server;
import java.util.List;

import ai.grakn.redismock.Response;
import ai.grakn.redismock.Slice;

class RO_quit extends AbstractRedisOperation {
    private final RedisClient client;

    RO_quit(RedisBase base, RedisClient client, List<Slice> params) {
        super(base, params,0, null, null);
        this.client = client;
    }

    Slice response() {
        client.sendResponse(Response.clientResponse("quit", Response.OK), "quit");
        client.close();

        return Response.SKIP;
    }
}
