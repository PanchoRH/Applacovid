package mx.cinvestav.cs.applacovid.jpa;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Entity(name = "Device")
@Table(name = "Device")
public class Device {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Basic
	@Column(name = "id")
	private Long id;
	@Basic
	@Column(name = "udid", nullable = false, unique = true)
	@NotBlank(message = "udid is mandatory")
	@NotEmpty
	private String udid;
	@Basic
	@Column(name = "usuario", nullable = false, unique = true)
	@NotBlank(message = "user is mandatory")
	@NotEmpty
	private String user;
	@Basic
	@Column(name = "mail", nullable = false, unique = true)
	@NotBlank(message = "mail is mandatory")
	private String mail;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUdid() {
		return udid;
	}
	public void setUdid(String udid) {
		this.udid = udid;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	
}
