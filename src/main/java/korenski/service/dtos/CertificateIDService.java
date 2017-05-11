package korenski.service.dtos;

import java.math.BigInteger;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import korenski.model.dto.CertificateID;
import korenski.model.dto.CertificateID.Status;
import korenski.repository.dtos.CertificateIDRepository;

@Service
public class CertificateIDService {
	@Autowired
	CertificateIDRepository certificateIDRepository;
	
	public CertificateID create(CertificateID certificateID){
		return certificateIDRepository.save(certificateID);
	}
	
	public CertificateID update(CertificateID certificateID){
		return certificateIDRepository.save(certificateID);
	}
	
	
	public CertificateID findCerificateID(Long id){
		return certificateIDRepository.findOne(id);
	}
	
	public CertificateID findBySerialNumberAndCa(BigInteger serialNumber, CertificateID ca){
		return certificateIDRepository.findBySerialNumberAndCa(serialNumber, ca);
	}
	
	public Set<CertificateID> findBySerialNumber(BigInteger serialNumber){
		return certificateIDRepository.findBySerialNumber(serialNumber);
	}
	
	public Set<CertificateID> findByCa(CertificateID ca){
		return certificateIDRepository.findByCa(ca);
	}
	
	public Set<CertificateID> findByStatus(Status status){
		return certificateIDRepository.findByStatus(status);
	}
	
	public void delete(Long id){
		certificateIDRepository.delete(id);
	}
	
	public CertificateID findByAlias(String alias){
		return certificateIDRepository.findByAlias(alias);
	}
}
