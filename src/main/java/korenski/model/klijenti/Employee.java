package korenski.model.klijenti;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import korenski.controller.autorizacija.Subject;
import korenski.model.infrastruktura.Bank;

@Entity
@Table(name="employee")
public class Employee extends Subject{
	
	
	@Column(nullable=false)
	private String name;
	
	@Column(nullable=false)
	private String surname;
	
	@ManyToOne
	private Bank bank;

	
	
	
	public Employee() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Employee(String name, String surname, Bank bank) {
		super();
		this.name = name;
		this.surname = surname;
		this.bank = bank;
	}

	public Employee(Long id, String name, String surname, Bank bank) {
		super();
		
		this.name = name;
		this.surname = surname;
		this.bank = bank;
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
