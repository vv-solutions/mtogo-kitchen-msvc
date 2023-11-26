package dk.vv.mtogo.kitchen.msvc;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "kitchen.msvc", namingStrategy = ConfigMapping.NamingStrategy.VERBATIM)
public interface Configuration {


    QueueConfig queues();

    public interface QueueConfig {

        interface InQueue {
            String queue();

            String exchange();

            String routingKey();

        }

        interface OutQueue {
            String exchange();
            String routingKey();
        }
        InQueue ticketCreation();
        OutQueue ticketResponse();

    }


}
