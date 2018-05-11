package external_clients.mock_redis_server;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import ai.grakn.redismock.Response;
import ai.grakn.redismock.Slice;
import ai.grakn.redismock.commands.RedisOperation;
import ai.grakn.redismock.exception.WrongNumberOfArgumentsException;
import ai.grakn.redismock.exception.WrongValueTypeException;

/**
 * Created by Xiaolu on 2015/4/20.
 */
public class RedisOperationExecutor {
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(RedisOperationExecutor.class);
    private final RedisClient owner;
    private final RedisBase base;
    private boolean transactionModeOn;
    private List<RedisOperation> transaction;

    public RedisOperationExecutor(RedisBase base, RedisClient owner) {
        this.base = base;
        this.owner = owner;
        transactionModeOn = false;
        transaction = new ArrayList<>();
    }

    private RedisOperation buildSimpleOperation(String name, List<Slice> params){
        switch(name){
            case "get":
                return new RO_get(base, params);
            case "del":
                return new RO_del(base, params);
            case "exists":
                return new RO_exists(base, params);
            case "hset":
            	return new RO_hset(base, params);
            case "hget":
            	return new RO_hget(base, params);
            case "ping":
            	return new RO_ping(base, params);
            case "quit":
            	return new RO_quit(base, owner, params);
            default:
                throw new UnsupportedOperationException(String.format("Unsupported operation '%s'", name));
        }
    }

    public synchronized Slice execCommand(RedisCommand command) {
        Preconditions.checkArgument(command.getParameters().size() > 0);

        List<Slice> params = command.getParameters();
        List<Slice> commandParams = params.subList(1, params.size());
        String name = new String(params.get(0).data()).toLowerCase();

        try {
            //Transaction handling
            if(name.equals("multi")){
                newTransaction();
                return Response.clientResponse(name, Response.OK);
            }

            //Checking if we mutating the transaction or the base
            RedisOperation redisOperation = buildSimpleOperation(name, commandParams);
            if(transactionModeOn){
                transaction.add(redisOperation);
            } else {
                return Response.clientResponse(name, redisOperation.execute());
            }

            return Response.clientResponse(name, Response.OK);
        } catch(UnsupportedOperationException | WrongValueTypeException e){
            LOG.error("Malformed request", e);
            return Response.error(e.getMessage());
        } catch (WrongNumberOfArgumentsException e){
            LOG.error("Malformed request", e);
            return Response.error(String.format("ERR wrong number of arguments for '%s' command", name));
        }
    }

    private void newTransaction(){
        if(transactionModeOn) throw new RuntimeException("Redis mock does not support more than one transaction");
        transactionModeOn = true;
    }
}
