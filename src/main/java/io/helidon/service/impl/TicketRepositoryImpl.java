package io.helidon.service.impl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;


public final class TicketRepositoryImpl implements TicketRepository {

	//in-memory data for now
	private final CopyOnWriteArrayList<Ticket> ticketList = new CopyOnWriteArrayList<Ticket>();
	
	public TicketRepositoryImpl() {
		JsonbConfig config = new JsonbConfig().withFormatting(Boolean.TRUE);

		Jsonb jsonb = JsonbBuilder.create(config);
		
		//pull sample data from a JSON file, located in resources directory
		ticketList.addAll(jsonb.fromJson(TicketRepositoryImpl.class.getResourceAsStream("/tickets.json"),
				new CopyOnWriteArrayList<Ticket>() {
				}.getClass().getGenericSuperclass()));
	}	
	
	@Override
	public List<Ticket> getAll() {
		return ticketList;
	}

	@Override
	public List<Ticket> getByProduct(String product) {
		
		List<Ticket> matchList = ticketList.stream().filter((t) -> (t.getProduct().contains(product)))
				.collect(Collectors.toList());

		return matchList;
	}

	@Override
	public List<Ticket> getByCustomer(String customer) {
		List<Ticket> matchList = ticketList.stream().filter((t) -> (t.getCustomer().contains(customer)))
				.collect(Collectors.toList());

		return matchList;
	}

	@Override
	public List<Ticket> getByPartner(String partner) {
		List<Ticket> matchList = ticketList.stream().filter((t) -> (t.getPartner().contains(partner)))
				.collect(Collectors.toList());

		return matchList;
	}

	@Override
	public List<Ticket> getByCreationDate(String creationDate) {
		List<Ticket> matchList = ticketList.stream().filter((t) -> (t.getCreationDate().contains(creationDate)))
				.collect(Collectors.toList());

		return matchList;
	}

	@Override
	public List<Ticket> getByStatus(String status) {
		List<Ticket> matchList = ticketList.stream().filter((t) -> (t.getStatus().contains(status)))
				.collect(Collectors.toList());

		return matchList;
	}
	
	@Override
	public Ticket save(Ticket ticket) {
		
		Ticket nextTicket = Ticket.of(null, ticket.getSubject(), ticket.getSummary(), ticket.getCustomer(),
				ticket.getCustomerId(), ticket.getPartner(), ticket.getPartnerId(), ticket.getProduct(),
				ticket.getStatus(), ticket.getCreationDate());
		
		ticketList.add(nextTicket);
		
		return nextTicket;
	}

	@Override
	public Ticket update(Ticket updatedTicket, String id) {

		deleteById(id);
		
		Ticket t = Ticket.of(id, updatedTicket.getSubject(), updatedTicket.getSummary(), updatedTicket.getCustomer(),
				updatedTicket.getCustomerId(), updatedTicket.getPartner(), updatedTicket.getPartnerId(), updatedTicket.getProduct(),
				updatedTicket.getStatus(), updatedTicket.getCreationDate());
		
		ticketList.add(t);
		
		return t;
		
	}

	@Override
	public void deleteById(String id) {
		int matchIndex;
		matchIndex = ticketList.stream().filter(t -> t.getId().equals(id)).findFirst().map(t -> ticketList.indexOf(t)).get();
		
		ticketList.remove(matchIndex);		
	}

	@Override
	public Ticket getById(String id) {
		Ticket match;
		match = ticketList.stream().filter(t -> t.getId().equals(id)).findFirst().get();
		
		return match;
	}

	@Override
	public boolean isIdFound(String id) {
		Ticket match;
		match = ticketList.stream().filter(t -> t.getId().equals(id)).findFirst().get();

		return match != null ? true : false;
	}



}
