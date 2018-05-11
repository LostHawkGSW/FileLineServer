# FileLineServer
Serve lines from a file to network clients

○ How does your system work? (if not addressed in comments in source)
- The system will look at the config file to determine where the remote file is located (please provide a URL, ex: an s3 file).
- The reason I chose to use a remote file rather than a local file is it will make moving to a distributed system much easier.
- Also, in the future we would add an admin interface to add new files/remove files.
- When the system starts up it will download the file, parse its contents into cache, which can then be served.

○ What do we need to build your system?
- Java 8
- Maven
- Redis running on "localhost" and port 6379 (can reconfigure in the config.yml)
- Internet connection
- **Optionally** Can also run this using a local in memory cache (no redis required) as well by changing the config file cacheStrategy to "local".

○ How will your system perform with a 1 GB file? a 10 GB file? a 100 GB file?
- This is dependent on the memory available on the system this is being run on, as well as the configuration for memory usage by redis. If a file is larger than the memory allowed, currently it will not run as designed since we are caching the file contents into memory. This can be changed if redesigned to be a distributed system.

○ How will your system perform with 100 users? 10000 users? 1000000 users?
- By default this system can support 1024 max connections, dependent on the machine it is running on it can be reconfigured to do more or less.
- If this system is redesigned to be a distributed system this could scale out infinitely based on resources.

○ What documentation, websites, papers, etc did you consult in doing this
assignment?
- Since I have not used java or dropwizard in some time, had to consult the documentation for dropwizard (including the git repo) as well as review some things on stack overflow

○ What third-party libraries or other tools does the system use? How did you
choose each library or framework you used?
- Dropwizard framework, Maven. These are quite standard and high performant for each of their use cases. Dropwizard is an excellent web framework for java, it takes a minimalist approach and you can configure maven to only pull what you really need.

○ How long did you spend on this exercise? If you had unlimited more time to
spend on this, how would you spend it and how would you prioritize each item?
- ~11 hours total time spent
- If I had more time to spend I would do the following (in order):
- Configure to use AWS (S3/Elasticache/EC2)
- Separate population of cache into it's own app
- Add admin interface to add/remove cached files.
- Rewrite the cache layer to be additionally backed by a relation db
- Add docker funtionality
- Add kubernetes functionality (And move system to use Amazon EKS)
- Rewrite the cache/db layer to be able to shard the data for extra large files
Additionally:
- Would create a pull request on redismock and move the changes in externalclients.mock_redis_server to there.

○ If you were to critique your code, what would you have to say about it?
- Currently it can only be run on a single machine and is limited by the resources available, thus can only serve a limited set of users and a limited file size.
