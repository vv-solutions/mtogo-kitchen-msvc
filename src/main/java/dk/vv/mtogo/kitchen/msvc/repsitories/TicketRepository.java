package dk.vv.mtogo.kitchen.msvc.repsitories;

import dk.vv.mtogo.kitchen.msvc.pojos.Ticket;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class TicketRepository implements PanacheRepository<Ticket> {
}
