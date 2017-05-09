package korenski.model.autorizacija;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import korenski.model.infrastruktura.Bank;

@Entity
@Table(name="user")
public class User {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(unique = true, nullable = false)
	@Size(min = 6, max = 30)
	@NotEmpty
	@Pattern(regexp = "[\\w]{6,30}")
	private String username;
	
	@Column(unique = true, nullable = false)
	@Email
	@NotEmpty
	private String email;
	
	
	@Column(nullable = false)
	@Size(min=8, max = 25)
	//@Pattern(regexp = "[\\w]{8,25}")
	@NotEmpty
	private String password;

	@Column(nullable = false)
	@NotEmpty
	@Size(max = 30)
	private String name;

	@Column(nullable = false)
	@NotEmpty
	@Size(max = 30)
	private String surname;

	
	@ManyToOne
	@Valid
	private Role role;
	
	@ManyToOne
	@Valid
	private Bank bank;

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User(Long id, String username, String email, String password, String name, String surname, Role role,
			Bank bank) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.name = name;
		this.surname = surname;
		this.role = role;
		this.bank = bank;
	}

	public User(String username, String email, String password, String name, String surname, Role role, Bank bank) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
		this.name = name;
		this.surname = surname;
		this.role = role;
		this.bank = bank;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	
}
