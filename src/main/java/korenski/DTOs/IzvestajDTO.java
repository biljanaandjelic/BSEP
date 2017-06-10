package korenski.DTOs;

import java.util.Date;

public class IzvestajDTO {
	private Long id;
	private Date izvestajOd;
	private Date izvestajDo;
	
	public IzvestajDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getIzvestajOd() {
		return izvestajOd;
	}

	public void setIzvestajOd(Date izvestajOd) {
		this.izvestajOd = izvestajOd;
	}

	public Date getIzvestajDo() {
		return izvestajDo;
	}

	public void setIzvestajDo(Date izvestajDo) {
		this.izvestajDo = izvestajDo;
	}
}
