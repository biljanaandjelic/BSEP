package korenski.model.klijenti;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import korenski.model.infrastruktura.Bank;

@Entity
public class Employed {
	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable=false)
	private String name;
	
	@Column(nullable=false)
	private String surname;
	
	@ManyToOne
	private Bank bank;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}
	
	

}
