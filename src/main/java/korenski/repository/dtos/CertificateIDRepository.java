package korenski.repository.dtos;

import java.math.BigInteger;
import java.util.Date;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;

import korenski.model.dto.CertificateID;
import korenski.model.dto.CertificateID.Status;

public interface CertificateIDRepository  extends CrudRepository<CertificateID,Long>{
	public CertificateID save(CertificateID certificateID);
	public void delete(Long id);
	public CertificateID findOne(Long id);
	public CertificateID findBySerialNumberAndCa(BigInteger serialNumber,CertificateID ca);
	public Set<CertificateID> findByCa(CertificateID ca);
	public Set<CertificateID> findBySerialNumber(BigInteger serialNumber);
	public Set<CertificateID> findByStatus(Status status);
	public Set<CertificateID> findByDateOfRevocation(Date dateOfRevocation);
	public Set<CertificateID> findAll();
	public CertificateID findByAlias(String alias);
}
