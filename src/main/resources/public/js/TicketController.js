var server = "/";

function search (){
    var searchTerm = $("#searchText").val().trim();
    if (searchTerm != "") {
        $("#tickets").show();
        $("#tickets").html("SEARCHING...");
        $.ajax({
            url: server + "tickets/" +
                $("#searchType").val() + "/" +
                encodeURIComponent(searchTerm),
            method: "GET"
        }).done(
            function(data) {
                $("#tickets").empty();
                $("#tickets").hide();
                if (data.length == 0) {
                    $("#tickets").html("");
                    $("#notFound").show();
                    $("#notFound").html("No ticket found matching your search criteria");
                } else {
                    showResults(data);
                }
                $("#tickets").show(400, "swing");
            });
    } else {
        loadTickets();
    }
}

$(function() {   
    $("#searchText").on("keyup", function(e) {
        if (e.keyCode == 13) {
            search ();
        }
    });
});

function showResults(data){
	console.log("DISPLAYING results...");
    $("#tickets").hide();
    $("#tickets").empty();
    $("#notFound").hide();
    data.forEach(function(ticket) {
        var item = $(renderTickets(ticket));
        item.on("click", function() {
            var detailItem = $(renderDetailTicket(ticket));
            $("#home").hide();
            $("#detail").empty();                                   
            $("#notFound").hide();
            $("#detail").append(detailItem);
            $("#tickets").hide(
                400,
                "swing",
                function() {
                    $("#detail").show()
                });
        });
        $("#tickets").append(item);
    });
}

function showTicketForm() {
    $("#notFound").hide();
    $("#editForm").hide();
    $("#deleteButton").hide();
    $("#ticketForm").show();
    $("#formTitle").text("Add ticket");
    $("#home").hide();
    $("#tickets").hide();
}

function loadTickets() {
    $("#notFound").hide();
    $("#searchText").val("");
    $("#ticketForm").hide();
    $("#editForm").hide();
    $("#home").show();
    $("#tickets").show();
    $("#tickets").html("LOADING...");
    $.ajax({
        dataType: "json",
        url: server + "tickets",
        method: "GET"
    }).done(function(data) {
        showResults(data); 
        $("#tickets").show(400, "swing");
    });
}


function renderTickets(ticket){
    var template = $('#tickets_tpl').html();
    Mustache.parse(template);
    var rendered = Mustache.render(template, {
        "product" : ticket.product,
        "partner" : ticket.partner,
        "customer" : ticket.customer,
        "creationDate" : ticket.creationDate,
        "subject" : ticket.subject,
        "status" : ticket.status
    });
    return rendered;
}

function renderDetailTicket(ticket){
    var template = $('#detail_tpl').html();
    Mustache.parse(template);
    var rendered = Mustache.render(template,{
        "id" : ticket.id,
        "subject" : ticket.subject,
        "summary" : ticket.summary,
        "customer" : ticket.customer,
        "customerId" : ticket.customerId,
        "partner" : ticket.partner,
        "partnerId" : ticket.partnerId,
        "product" : ticket.product,
        "status" : ticket.status,
        "creationDate" : ticket.creationDate
    });
    return rendered;
}

function save() {
    var ticket = {
    	id: '',
        subject: $("#subject").val(),
        summary: $("#summary").val(),
        customer: $("#customer").val(),
        customerId: $("#customerId").val(),
        partner: $("#partner").val(),
        partnerId: $("#partnerId").val(),
        product: $("#product").val(),
        status: $("#status").val(),
        creationDate: $("#creationDate").val()
    };

    $.ajax({    	
        url: server + "tickets",
        method: "POST",
        data: JSON.stringify(ticket),        
        error: function(){
        	 console.log("There was an error while saving data..." + ticket.subject);
        },
        timeout: 3000 // sets timeout to 3 seconds
    }).done(function(data) {
        $("#detail").hide();
        $("#subject").val("");
        $("#summary").val("");
        $("#customer").val("");
        $("#customerId").val("");
        $("#partner").val("");
        $("#partnerId").val("");
        $("#product").val("");
        $("#status").val("");
        $("#creationDate").val("");
        loadTickets();
    });

}

function updateTicket() {
    var ticket = {
        id: $("#editId").val(),
        subject: $("#editSubject").val(),
        summary: $("#editSummary").val(),
        customer: $("#editCustomer").val(),
        customerId: $("#editCustomerId").val(),
        partner: $("#editPartner").val(),
        partnerId: $("#editPartnerId").val(),
        product: $("#editProduct").val(),
        status: $("#editStatus").val(),
        creationDate: $("#editCreationDate").val()
    };
    $("#detail").html("UPDATING...");
    console.log("Triggering update ... " + ticket.id);
    $.ajax({
        url: server + "tickets/" + ticket.id,
        method: "PUT",
        data: JSON.stringify(ticket)
    }).done(function(data) {
        $("#detail").hide();
        loadTickets();
    });
}

function deleteTicket() {
	
    var ticket = {
    	subject: $("#editSubject").val(),
        creationDate: $("#editCreationDate").val(),
        id: $("#editId").val()
    };
    
    $('<div></div>').dialog({
        modal: true,
        title: "Confirm Delete",
        open: function() {
            var markup = 'Are you sure you want to delete ' +
                ticket.subject + ', created on ' + ticket.creationDate +
                " ticket?";
            $(this).html(markup);
        },
        buttons: {
            Ok: function() {
                $("#detail").html("DELETING...");
                $(this).dialog("close");
                $.ajax({
                    url: server + "tickets/" + ticket.id,
                    method: "DELETE"
                }).done(function(data) {
                    $("#detail").hide();
                    loadTickets();
                });
            },
            Cancel: function() {
                $(this).dialog("close");
            }
        }
    });

}