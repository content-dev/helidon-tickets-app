package io.helidon.service.tickets;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import io.helidon.config.Config;
import io.helidon.service.impl.Ticket;
import io.helidon.service.impl.TicketRepository;

import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
import io.helidon.webserver.Service;

public class TicketService implements Service {

	private final TicketRepository tickets;
	private static final Logger LOGGER = Logger.getLogger(TicketService.class.getName());

	TicketService(Config config) {
		tickets = TicketRepository.create(config.get("app.drivertype").asString().orElse("Array"), config);
	}

	@Override
	public void update(Routing.Rules rules) {
		rules.get("/", this::getAll)
				.get("/status/{status}", this::getByStatus)
				.get("/product/{product}", this::getByProduct)
				.get("/customer/{customer}", this::getByCustomer)
				.get("/partner/{partner}", this::getByPartner)
				.get("/creation/{creationDate}", this::getByCreationDate)
				.post("/", this::save)
				.get("/{ticketId}", this::getTicketById)
				.put("/{ticketId}", this::update)
				.delete("/{ticketId}", this::delete);
	}

	private void getAll(final ServerRequest request, final ServerResponse response) {
		LOGGER.fine("getAll");
		List<Ticket> allTickets = this.tickets.getAll();
		response.send(allTickets);
	}

	private void getByStatus(final ServerRequest request, final ServerResponse response) {
		LOGGER.fine("getByStatus");
		// Invalid query strings handled in isValidQueryStr. Keeping DRY
		if (isValidQueryStr(response, request.path().param("status"))) {
			response.status(200).send(this.tickets.getByStatus(request.path().param("status")));
		}
	}
	
	private void getByProduct(final ServerRequest request, final ServerResponse response) {
		LOGGER.fine("getByProduct");
		// Invalid query strings handled in isValidQueryStr. Keeping DRY
		if (isValidQueryStr(response, request.path().param("product"))) {
			response.status(200).send(this.tickets.getByProduct(request.path().param("product")));
		}
	}

	private void getByCustomer(final ServerRequest request, final ServerResponse response) {
		LOGGER.fine("getByCustomer");
		// Invalid query strings handled in isValidQueryStr. Keeping DRY
		if (isValidQueryStr(response, request.path().param("customer"))) {
			response.status(200).send(this.tickets.getByCustomer(request.path().param("customer")));
		}
	}

	private void getByPartner(final ServerRequest request, final ServerResponse response) {
		LOGGER.fine("getByPartner");
		// Invalid query strings handled in isValidQueryStr. Keeping DRY
		if (isValidQueryStr(response, request.path().param("partner"))) {
			response.status(200).send(this.tickets.getByPartner(request.path().param("partner")));
		}
	}

	private void getByCreationDate(final ServerRequest request, final ServerResponse response) {
		LOGGER.fine("getByCreationDate");
		// Invalid query strings handled in isValidQueryStr. Keeping DRY
		if (isValidQueryStr(response, request.path().param("creationDate"))) {
			response.status(200).send(this.tickets.getByCreationDate(request.path().param("creationDate")));
		}
	}

	private boolean isValidQueryStr(ServerResponse response, String nameStr) {
		Map<String, String> errorMessage = new HashMap<>();
		if (nameStr == null || nameStr.isEmpty() || nameStr.length() > 100) {
			errorMessage.put("errorMessage", "Invalid query string");
			response.status(400).send(errorMessage);
			return false;
		} else {
			return true;
		}
	}

	private void save(ServerRequest request, ServerResponse response) {
		LOGGER.info("save");				
		request.content().as(Ticket.class)
		.thenApply(ticket -> Ticket.of(null, ticket.getSubject(), ticket.getSummary(), ticket.getCustomer(),
				ticket.getCustomerId(), ticket.getPartner(), ticket.getPartnerId(), ticket.getProduct(),
				ticket.getStatus(), ticket.getCreationDate()))
		.thenApply(this.tickets::save)
		.thenCompose(p -> response.status(201).send());
		
	}

	private void getTicketById(ServerRequest request, ServerResponse response) {
		LOGGER.fine("getTicketById");
		// If invalid, response handled in isValidId. Keeping DRY
		if (isValidId(response, request.path().param("ticketId"))) {
			Ticket tickets = this.tickets.getById(request.path().param("ticketId"));
			response.status(200).send(tickets);
		}
	}

	private boolean isValidId(ServerResponse response, String idStr) {
		Map<String, String> errorMessage = new HashMap<>();
		if (idStr == null || idStr.isEmpty()) {
			errorMessage.put("errorMessage", "Invalid query string");
			response.status(400).send(errorMessage);
			return false;
		} else if (this.tickets.isIdFound(idStr)) {
			return true;
		} else {
			errorMessage.put("errorMessage", "ID " + idStr + " not found around here!");
			response.status(404).send(errorMessage);
			return false;
		}

	}

	private void update(ServerRequest request, ServerResponse response) {
		LOGGER.fine("update");
		if (isValidId(response, request.path().param("ticketId"))) {
			request.content().as(Ticket.class)
				.thenApply(ticket -> {
					return this.tickets.update(Ticket.of(null, ticket.getSubject(), ticket.getSummary(),
						ticket.getCustomer(), ticket.getCustomerId(), ticket.getPartner(), ticket.getPartnerId(),
						ticket.getProduct(), ticket.getStatus(), ticket.getCreationDate()),
						request.path().param("ticketId"));
					})
				.thenCompose(p -> response.status(204).send());
		}
	}

	private void delete(final ServerRequest request, final ServerResponse response) {
		LOGGER.fine("delete");
		if (isValidId(response, request.path().param("ticketId"))) {
			this.tickets.deleteById(request.path().param("ticketId"));
			response.status(204).send();
		}
	}

}
