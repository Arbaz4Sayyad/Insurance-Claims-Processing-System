package in.arbii.binding;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "CONTACT_DTLS")
public class Contact {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer contactId;
	private String contactName;
	private String contactEmail;
	private Long contactNum;

}