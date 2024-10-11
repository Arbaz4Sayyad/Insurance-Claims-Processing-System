package in.arbii.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import in.arbii.binding.Contact;
import in.arbii.service.ContactService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
public class ContactRestController {
	@Autowired
	private ContactService service;

	@PostMapping("/contact")
	public String createContact(@RequestBody Contact contact) {
		String status = service.saveContact(contact);
		return status;
	}

	@GetMapping("/contacts")
	public List<Contact> getAllContacts() {
		return service.getAllContacts();
	}

	@GetMapping("/contact/{contactId}")
	public Contact getContactById(@PathVariable Integer contactId) {
		return service.getContactById(contactId);
	}

	@PutMapping("/contact")
	public String updateContact(@RequestBody Contact contact) {
		return service.updateContact(contact);
	}

	@DeleteMapping("/contact/{contactId}")
	public String deleteContact(@PathVariable Integer contactId) {
		return service.deleteContactById(contactId);
	}
}
