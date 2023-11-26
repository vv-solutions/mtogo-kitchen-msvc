package dk.vv.mtogo.kitchen.msvc.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rabbitmq.client.*;
import dk.vv.common.data.transfer.objects.kitchen.TicketDTO;
import dk.vv.common.data.transfer.objects.kitchen.TicketResponseDTO;
import dk.vv.mtogo.kitchen.msvc.Configuration;
import dk.vv.mtogo.kitchen.msvc.enums.TicketStatus;
import dk.vv.mtogo.kitchen.msvc.facades.TicketFacade;
import dk.vv.mtogo.kitchen.msvc.repsitories.TicketRepository;
import io.quarkiverse.rabbitmqclient.RabbitMQClient;
import io.quarkus.arc.profile.UnlessBuildProfile;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import org.jboss.logging.Logger;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;


@ApplicationScoped
@UnlessBuildProfile("test")
public class MessageServiceImpl implements MessageService {


    private  Logger logger;


    private  RabbitMQClient rabbitMQClient;


    private Configuration configuration;

    private Channel channel;


    private TicketFacade ticketFacade;


    private TicketRepository repository;



    private ObjectMapper mapper = new ObjectMapper(){{
        this.registerModule(new JavaTimeModule());
    }};


    @Inject
    public MessageServiceImpl(Logger logger, RabbitMQClient rabbitMQClient, Configuration configuration, TicketFacade ticketFacade, TicketRepository ticketRepository) {
        this.logger = logger;
        this.rabbitMQClient = rabbitMQClient;
        this.configuration = configuration;
        this.ticketFacade = ticketFacade;
        this.repository = ticketRepository;
    }

    @Override
    @Transactional
    public void onApplicationStart(@Observes StartupEvent event) {
        // on application start prepare the queues and message listener
        setupQueues();
        listenOnTicketCreationQueue();
    }

    @Override
    public void setupQueues() {
        try {
            // create a connection
            Connection connection = rabbitMQClient.connect();

            // create a channel
            channel = connection.createChannel();

            // declare queue
            channel.queueDeclare(configuration.queues().ticketCreation().queue(), true, false, false, null);

            // bind queue to exchange
            channel.queueBind(configuration.queues().ticketCreation().queue(),configuration.queues().ticketCreation().exchange(),configuration.queues().ticketCreation().routingKey());

        } catch (IOException e) {

            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void listenOnTicketCreationQueue() {
        try {
            channel.basicConsume(configuration.queues().ticketCreation().queue(), true, new DefaultConsumer(channel) {

                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                    var ticketDTO = mapper.readValue(body, TicketDTO.class);

                    logger.infof("ticket: received information about ticket with orderId [%d]",ticketDTO.getOrderId());

                    // persist new ticket with status pending
                    ticketFacade.setStatus(ticketDTO,TicketStatus.PENDING);
                    ticketDTO = ticketFacade.enrichWithProductName(ticketDTO);
                    ticketDTO = ticketFacade.saveTicket(ticketDTO);

                    logger.infof("ticket: created new ticket with id [%s]",ticketDTO.getId());

                }
            });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

    }

    @Override
    public void sendTicketResponse(TicketResponseDTO ticketResponseDTO) {
        try {
            // send a message to the exchange

            String message = mapper.writeValueAsString(ticketResponseDTO);

            logger.infof("ticket response: Sent ticket response for ticket with orderId: [%d]",ticketResponseDTO.getOrderId());

            channel.basicPublish(configuration.queues().ticketResponse().exchange(), configuration.queues().ticketResponse().routingKey(), null, message.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


}



