package dk.vv.mtogo.kitchen.msvc.pojos;


import dk.vv.common.data.transfer.objects.kitchen.TicketDTO;
import dk.vv.common.data.transfer.objects.kitchen.TicketResponseDTO;
import dk.vv.mtogo.kitchen.msvc.enums.TicketStatus;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ticket")
public class Ticket {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "order_id")
    private int orderId;

    @Column(name = "supplier_id")
    private int supplierId;

    @Column(name = "comment")
    private String comment;

    @Column(name = "pickup_time")
    private LocalDateTime pickupTime;

    @Column(name = "status")
    private int status;


    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_stamp")
    private LocalDateTime createStamp;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    Set<TicketLine> ticketLines = new HashSet<>();

    // ===== constructors ======
    public Ticket() {
    }

    public Ticket(TicketDTO ticketDTO) {
        this.id = ticketDTO.getId();
        this.orderId = ticketDTO.getOrderId();
        this.supplierId = ticketDTO.getSupplierId();
        this.comment = ticketDTO.getComment();
        this.pickupTime = ticketDTO.getPickupTime();
        this.status = ticketDTO.getStatus();
        if(!ticketDTO.getTicketLines().isEmpty()){
            ticketDTO.getTicketLines().forEach(tl -> {
                this.addTicketLine(new TicketLine(tl));
            });
        }

    }

    // ===== methods ======


    public TicketDTO toDto() {
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setId(this.id);
        ticketDTO.setOrderId(this.orderId);
        ticketDTO.setSupplierId(this.supplierId);
        ticketDTO.setComment(this.comment);
        ticketDTO.setPickupTime(this.pickupTime);
        ticketDTO.setStatus(this.status);
        ticketDTO.setCreateStamp(this.createStamp);
        if(!this.getTicketLines().isEmpty()){
            this.ticketLines.forEach(tl ->{
                ticketDTO.addTicketLine(tl.toDto());
            });
        }
        return ticketDTO;
    }

    public TicketResponseDTO toResponse(boolean accepted){
        TicketResponseDTO ticketResponseDTO = new TicketResponseDTO();
        ticketResponseDTO.setOrderId(this.orderId);
        ticketResponseDTO.setComment(this.comment);
        ticketResponseDTO.setPickupTime(this.pickupTime);
        ticketResponseDTO.setAccepted(accepted);

        return ticketResponseDTO;
    }


    // ===== Getters and setters ======


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(LocalDateTime pickupTime) {
        this.pickupTime = pickupTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LocalDateTime getCreateStamp() {
        return createStamp;
    }

    public void setCreateStamp(LocalDateTime createStamp) {
        this.createStamp = createStamp;
    }

    public Set<TicketLine> getTicketLines() {
        return ticketLines;
    }

    public void setTicketLines(Set<TicketLine> ticketLines) {
        this.ticketLines = ticketLines;
    }

    public void addTicketLine(TicketLine ticketLine){
        this.ticketLines.add(ticketLine);

        if(ticketLine.getTicket() != this){
            ticketLine.setTicket(this);
        }
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", supplierId=" + supplierId +
                ", comment='" + comment + '\'' +
                ", pickupTime=" + pickupTime +
                ", status=" + status +
                ", createStamp=" + createStamp +
                '}';
    }
}
