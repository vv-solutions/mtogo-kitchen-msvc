package dk.vv.mtogo.kitchen.msvc;


import dk.vv.common.data.transfer.objects.kitchen.TicketResponseDTO;
import dk.vv.mtogo.kitchen.msvc.message.MessageService;
import io.quarkus.arc.profile.UnlessBuildProfile;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.test.Mock;
import jakarta.enterprise.context.ApplicationScoped;

@Mock
@ApplicationScoped
public class MessageServiceMock implements MessageService {
    @Override
    public void onApplicationStart(StartupEvent event) {

    }
    @Override
    public void setupQueues() {

    }

    @Override
    public void listenOnTicketCreationQueue() {

    }

    @Override
    public void sendTicketResponse(TicketResponseDTO ticketResponseDTO) {

    }
}
