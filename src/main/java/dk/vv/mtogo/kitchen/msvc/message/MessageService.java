package dk.vv.mtogo.kitchen.msvc.message;

import dk.vv.common.data.transfer.objects.kitchen.TicketResponseDTO;
import dk.vv.common.data.transfer.objects.order.OrderDTO;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;

public interface MessageService {

    void onApplicationStart(@Observes StartupEvent event);
    void setupQueues();
    void listenOnTicketCreationQueue();

    void sendTicketResponse(TicketResponseDTO ticketResponseDTO);

}
