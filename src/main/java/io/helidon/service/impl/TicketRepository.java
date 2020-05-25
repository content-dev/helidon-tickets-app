package io.helidon.service.impl;

import java.util.List;

import io.helidon.config.Config;


public interface TicketRepository {
	
	public static TicketRepository create(String driverType, Config config) {
        switch (driverType) {
        case "Array":
            return new TicketRepositoryImpl();
        /*case "Oracle":
            return new TicketRepositoryImplDB(config);*/
        default:
            // Array is default
            return new TicketRepositoryImpl();
        }
    }

    public List<Ticket> getAll();
    public List<Ticket> getByProduct(String product);
    public List<Ticket> getByCustomer(String customer);
    public List<Ticket> getByPartner(String partner);
    public List<Ticket> getByCreationDate(String creationDate);
    public List<Ticket> getByStatus(String status);
    public Ticket save(Ticket ticket); // Add new Ticket
    public Ticket update(Ticket updatedTicket, String id);
    public void deleteById(String id);
    public Ticket getById(String id);
    public boolean isIdFound(String id);
}
