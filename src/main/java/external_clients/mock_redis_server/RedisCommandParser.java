package external_clients.mock_redis_server;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;

import ai.grakn.redismock.SliceParser;
import ai.grakn.redismock.exception.EOFException;
import ai.grakn.redismock.exception.ParseErrorException;

/**
 * Created by Xiaolu on 2015/4/20.
 */
public class RedisCommandParser {


    @VisibleForTesting
    static RedisCommand parse(String stringInput) throws ParseErrorException, EOFException {
        Preconditions.checkNotNull(stringInput);

        return parse(new ByteArrayInputStream(stringInput.getBytes()));
    }

    static RedisCommand parse(InputStream messageInput) throws ParseErrorException, EOFException {
        Preconditions.checkNotNull(messageInput);

        long count = SliceParser.consumeCount(messageInput);
        if (count == 0) {
            throw new ParseErrorException();
        }
        RedisCommand command = new RedisCommand();
        for (long i = 0; i < count; i++) {
            command.addParameter(SliceParser.consumeParameter(messageInput));
        }
        return command;
    }
}
