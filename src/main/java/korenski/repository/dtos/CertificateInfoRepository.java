package korenski.repository.dtos;

import java.math.BigInteger;
import java.util.Date;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;

import korenski.model.infrastruktura.Bank;
import korenski.model.util.CertificateInfo;
import korenski.model.util.CertificateInfo.CertStatus;

public interface CertificateInfoRepository  extends CrudRepository<CertificateInfo,Long>{
	public CertificateInfo save(CertificateInfo certificateID);
	public void delete(Long id);
	public CertificateInfo findOne(Long id);
	public CertificateInfo findBySerialNumberAndCa(BigInteger serialNumber,CertificateInfo ca);
	public Set<CertificateInfo> findByCa(CertificateInfo ca);
	public CertificateInfo findBySerialNumber(BigInteger serialNumber);
	public Set<CertificateInfo> findByStatus(CertStatus status);
	public Set<CertificateInfo> findByDateOfRevocation(Date dateOfRevocation);
	public Set<CertificateInfo> findAll();

	public CertificateInfo findBySerialNumberAndBank(BigInteger serialNumber, Bank bank);

}
