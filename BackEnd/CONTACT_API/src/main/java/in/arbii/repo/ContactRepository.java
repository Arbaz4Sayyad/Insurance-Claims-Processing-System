package in.arbii.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import in.arbii.binding.Contact;

public interface ContactRepository extends JpaRepository<Contact, Integer>{

}