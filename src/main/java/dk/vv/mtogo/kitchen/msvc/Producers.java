package dk.vv.mtogo.kitchen.msvc;

import dk.vv.mtogo.kitchen.msvc.api.ProductService;
import dk.vv.mtogo.kitchen.msvc.facades.TicketFacade;
import dk.vv.mtogo.kitchen.msvc.message.MessageService;
import dk.vv.mtogo.kitchen.msvc.message.MessageServiceImpl;
import dk.vv.mtogo.kitchen.msvc.repsitories.TicketRepository;
import io.quarkiverse.rabbitmqclient.RabbitMQClient;
import jakarta.enterprise.context.ApplicationScoped;

import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

@ApplicationScoped
public class Producers {

}
