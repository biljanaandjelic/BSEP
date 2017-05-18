package korenski.controller.geografija;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.cert.ocsp.OCSPReq;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import korenski.DTOs.CertificateDTO;
import korenski.DTOs.CertificateRequestDTO;
import korenski.model.autorizacija.User;
import korenski.model.dto.CertificateID;
import korenski.model.dto.CertificateID.Status;
import korenski.model.infrastruktura.Bank;
import korenski.repository.institutions.BankRepository;
import korenski.service.dtos.CertificateIDService;
import util.Base64Utility;

@Controller
@RequestMapping("/certificates")
public class CertificatesController {
	@Autowired
	CertificateIDService certificateIDService;
	@Autowired
	BankRepository bankRepository;
	
	private KeyStore ks;

	@RequestMapping(value = "/genCertificate", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN)
	public ResponseEntity<String> genCertificate(@RequestBody CertificateDTO dto, @Context HttpServletRequest request)
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException,
			IOException, NoSuchProviderException, OperatorCreationException, ParseException {
		
		String filePathString = "./files/" + ((User)request.getSession().getAttribute("user")).getBank().getSwiftCode() + ".jks";
		
		if (ks == null) {
			ks = KeyStore.getInstance("BKS", "BC");
		}

		//ks.load(new FileInputStream("./files/gagi.jks"), "test".toCharArray());
		getKeyStore(filePathString);
		
		
		X500NameBuilder b = new X500NameBuilder(BCStyle.INSTANCE);
		b.addRDN(BCStyle.CN, dto.cn);
		b.addRDN(BCStyle.SURNAME, dto.surname);
		b.addRDN(BCStyle.GIVENNAME, dto.givenName);
		b.addRDN(BCStyle.O, dto.organization);
		b.addRDN(BCStyle.OU, dto.organizationUnit);
		b.addRDN(BCStyle.C, dto.country);
		b.addRDN(BCStyle.E, dto.email);
		b.addRDN(BCStyle.UID, "");

		X500Name name = b.build();

		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
		keyGen.initialize(2048, random);

		KeyPair pair = keyGen.generateKeyPair();

		JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");

		builder = builder.setProvider("BC");

		ContentSigner contentSigner = builder.build(pair.getPrivate());

		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = dto.validFrom;
		Date endDate = dto.validTo;
		BigInteger serial = new BigInteger(20, new SecureRandom());
		
		System.out.println(serial);
		
		X509v3CertificateBuilder certGen;
		//if (dto.selfSigned) {
		CertificateID certificateID;
		

		certificateID = new CertificateID(serial, Status.OK, null, null, dto.alias);
		
		//ovo je tvoj deo biljo, samo zakaci na certificateinfo banku
		Bank bank = ((User)request.getSession().getAttribute("user")).getBank();
		
		certificateIDService.create(certificateID);
		certGen = new JcaX509v3CertificateBuilder(name, serial, startDate, endDate, name,
				pair.getPublic());
		/*} else {
			
			Certificate issuerCertificate = ks.getCertificate(dto.issuerAlias);

			if (((X509Certificate) issuerCertificate).getNotAfter().before(new Date())) {
				return new ResponseEntity<String>("Issuer certificate is no longer valid", HttpStatus.OK);
			} else if (((X509Certificate) issuerCertificate).getBasicConstraints() == -1) {
				return new ResponseEntity<String>("Issuer certificate is not CA!", HttpStatus.OK);
			}
			
			CertificateID ca = certificateIDService.findByAlias(dto.issuerAlias);
			CertificateID certificateID = new CertificateID(serial, Status.OK, null, ca, dto.alias);
			certificateIDService.create(certificateID); 
			X500Name issuerName = new JcaX509CertificateHolder((X509Certificate) issuerCertificate).getIssuer();

			certGen = new JcaX509v3CertificateBuilder(issuerName, serial, startDate, endDate, name,
					pair.getPublic());
		}
		*/

		//certGen.addExtension(Extension.basicConstraints, true, new BasicConstraints(dto.ca));
		certGen.addExtension(Extension.basicConstraints, true, new BasicConstraints(true));
		
		X509CertificateHolder certHolder = certGen.build(contentSigner);

		JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
		certConverter = certConverter.setProvider("BC");
		
		System.out.println(certConverter.getCertificate(certHolder));
		
		ks.setCertificateEntry(dto.alias, certConverter.getCertificate(certHolder));
		ks.setKeyEntry(dto.keyAlias, pair.getPrivate().getEncoded(),
				new Certificate[] { (Certificate) certConverter.getCertificate(certHolder) });
		
		//ks.store(new FileOutputStream("./files/gagi.jks"), "test".toCharArray());
		saveKeyStore(filePathString);
		
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/genCertificateRequest", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN)
	public ResponseEntity<String> genCertificateRequest(@RequestBody CertificateDTO dto, @Context HttpServletRequest httpRequest)
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException,
			IOException, NoSuchProviderException, OperatorCreationException, ParseException, InvalidKeySpecException {
		
		X500NameBuilder b = new X500NameBuilder(BCStyle.INSTANCE);
		b.addRDN(BCStyle.CN, dto.cn);
		b.addRDN(BCStyle.SURNAME, dto.surname);
		b.addRDN(BCStyle.GIVENNAME, dto.givenName);
		b.addRDN(BCStyle.O, dto.organization);
		b.addRDN(BCStyle.OU, dto.organizationUnit);
		b.addRDN(BCStyle.C, dto.country);
		b.addRDN(BCStyle.E, dto.email);
		b.addRDN(BCStyle.UID, "");

		X500Name name = b.build();
		
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
		keyGen.initialize(2048, random);

		KeyPair pair = keyGen.generateKeyPair();
		
		System.out.println(pair.getPublic());
		
		JcaPKCS10CertificationRequestBuilder p10Builder = new JcaPKCS10CertificationRequestBuilder(name,  pair.getPublic());
		JcaContentSignerBuilder csBuilder = new JcaContentSignerBuilder("SHA256withRSA");
		ContentSigner signer = csBuilder.build(pair.getPrivate());
		PKCS10CertificationRequest request = p10Builder.build(signer);
		String data = request.toString();
		System.out.println("DATA: " + data);
		
		User u = (User)httpRequest.getSession().getAttribute("user");
		String bankSwiftCode;
		if(u.getRole().getId().equals(new Long(2))){
			Bank bank = bankRepository.findOne(new Long(1));
			bankSwiftCode = bank.getSwiftCode();
		}else if(u.getRole().getId().equals(new Long(3))){
			bankSwiftCode = ((User)httpRequest.getSession().getAttribute("user")).getBank().getSwiftCode();
		}else{
			return null;
		}
		
		//String bankSwiftCode = ((User)httpRequest.getSession().getAttribute("user")).getBank().getSwiftCode();
		File folder = new File("./files/CSR"+bankSwiftCode);
		if(!folder.exists()){
			folder.mkdirs();
		}
		File[] listOfFiles = folder.listFiles();
		int num = 0;
		if(listOfFiles!=null){
			 num=listOfFiles.length;
		}
		num++;
		
//		File f1 = new File("./files/CSR"+bankSwiftCode);
		File f = new File("./files/CSR"+bankSwiftCode+"/CSR"+num+".csr");
		BufferedWriter w = new BufferedWriter(new FileWriter(f.getPath()));
		StringWriter sw = new StringWriter();
		JcaPEMWriter pemWriter = new JcaPEMWriter(sw);
		pemWriter.writeObject(request);
		pemWriter.close();
		
		sw.close();
		w.write(sw.toString());
		w.flush();
		w.close();
		
		System.out.println(request.getSubject());
		System.out.println(pair.getPublic());
		//System.out.println(request.getSubjectPublicKeyInfo().parsePublicKey());
		
		SubjectPublicKeyInfo pkInfo = request.getSubjectPublicKeyInfo();
		RSAKeyParameters rsa = (RSAKeyParameters) PublicKeyFactory.createKey(pkInfo);
		RSAPublicKeySpec rsaSpec = new RSAPublicKeySpec(rsa.getModulus(), rsa.getExponent());
		KeyFactory kf = KeyFactory.getInstance("RSA");
		PublicKey rsaPub = kf.generatePublic(rsaSpec);
		
		System.out.println(rsaPub);
		
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getCertificateRequests", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<Collection<CertificateRequestDTO>> getCertificateRequests(@Context HttpServletRequest httpRequest) throws IOException{
		
		ArrayList<CertificateRequestDTO> retVal = new ArrayList<CertificateRequestDTO>();
		
		User u = (User)httpRequest.getSession().getAttribute("user");
		String bankSwiftCode;
		if(u.getRole().getId().equals(new Long(1))){
			Bank bank = bankRepository.findOne(new Long(1));
			bankSwiftCode = bank.getSwiftCode();
		}else if(u.getRole().getId().equals(new Long(2))){
			bankSwiftCode = ((User)httpRequest.getSession().getAttribute("user")).getBank().getSwiftCode();
		}else{
			return null;
		}
		
		File folder = new File("./files/CSR"+bankSwiftCode);
		if(!folder.exists()){
			folder.mkdirs();
		}
		File[] listOfFiles = folder.listFiles();
		
		for(File f : listOfFiles){
			BufferedReader r = new BufferedReader(new FileReader(f.getPath()));
			//StringReader sr = new StringReader();
			PemReader pemReader = new PemReader(r);
			//PKCS10CertificationRequest csr = (PKCS10CertificationRequest)pemReader.readPemObject(); 
			PEMParser pemParser = new PEMParser(pemReader);
			Object o = pemParser.readObject();
			PKCS10CertificationRequest csr = (PKCS10CertificationRequest)o;
			
			CertificateRequestDTO dto = new CertificateRequestDTO();
			
			dto.setCn((IETFUtils.valueToString(csr.getSubject().getRDNs(BCStyle.CN)[0].getFirst().getValue())));
			dto.setSurname((IETFUtils.valueToString(csr.getSubject().getRDNs(BCStyle.SURNAME)[0].getFirst().getValue())));
			dto.setGivenName((IETFUtils.valueToString(csr.getSubject().getRDNs(BCStyle.GIVENNAME)[0].getFirst().getValue())));
			dto.setOrganization((IETFUtils.valueToString(csr.getSubject().getRDNs(BCStyle.O)[0].getFirst().getValue())));
			dto.setOrganizationUnit((IETFUtils.valueToString(csr.getSubject().getRDNs(BCStyle.OU)[0].getFirst().getValue())));
			dto.setCountry((IETFUtils.valueToString(csr.getSubject().getRDNs(BCStyle.C)[0].getFirst().getValue())));
			dto.setEmail((IETFUtils.valueToString(csr.getSubject().getRDNs(BCStyle.E)[0].getFirst().getValue())));
			
			retVal.add(dto);
			
			pemParser.close();
			pemReader.close();
			r.close();
		}
		
		return new ResponseEntity<Collection<CertificateRequestDTO>>(retVal, HttpStatus.OK);
	}

	@RequestMapping(value = "/certificate/{alias}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<CertificateDTO> findCertificateDTO(@PathVariable("alias") String alias)
			throws KeyStoreException, NoSuchProviderException {
		System.out.println("Pronalazenje sertifikata na osnovu alijasa");
		if (ks == null) {
			//ks = KeyStore.getInstance("JCEKS", "SunJCE");
			ks = KeyStore.getInstance("BKS", "BC");
		}
		try {
			//ks.load(new FileInputStream("./files/keystore.jks"), "test".toCharArray());
			ks.load(new FileInputStream("./files/gagi.jks"), "test".toCharArray());
		} catch (NoSuchAlgorithmException | CertificateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Certificate foundCertificate = ks.getCertificate(alias);

		if (foundCertificate != null) {


			CertificateDTO certificateDTO = getDataFromCertificate(foundCertificate);
			return new ResponseEntity<CertificateDTO>(certificateDTO, HttpStatus.OK);
		}

		return new ResponseEntity<CertificateDTO>(HttpStatus.NOT_FOUND);
	}

	public CertificateDTO getCertificateBySerialNumber(String serialNumber) {
		try {
			
			ks.load(new FileInputStream("./files/gagi.jks"), "test".toCharArray());
			Enumeration aliases = ks.aliases();
			String alias;
			for (; aliases.hasMoreElements();) {
				alias = (String) aliases.nextElement();

				Certificate cert = ks.getCertificate(alias);

				if (cert.getType().equals(BCStyle.SERIALNUMBER)) {
					
					if (((X509Certificate) cert).getSerialNumber().equals(serialNumber)) {
						CertificateDTO certDTO = getDataFromCertificate(cert);
						return certDTO;
					}
				}

			}
		} catch (NoSuchAlgorithmException | CertificateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// List the aliases
		return null;

	}

	public CertificateDTO getDataFromCertificate(Certificate certificat) {
		X500Name x500name;
		try {
			x500name = new JcaX509CertificateHolder((X509Certificate) certificat).getSubject();
			RDN cn = x500name.getRDNs(BCStyle.CN)[0];
			RDN surname = x500name.getRDNs(BCStyle.SURNAME)[0];
			RDN givenName = x500name.getRDNs(BCStyle.GIVENNAME)[0];
			RDN organization = x500name.getRDNs(BCStyle.O)[0];
			RDN organizationUnit = x500name.getRDNs(BCStyle.OU)[0];

			PublicKey publicKey = certificat.getPublicKey();
			RDN email = x500name.getRDNs(BCStyle.E)[0];
			RDN country = x500name.getRDNs(BCStyle.C)[0];
			Date validFrom = ((X509Certificate) certificat).getNotBefore();
			Date validTo = ((X509Certificate) certificat).getNotAfter();
			CertificateDTO certificateDTO = new CertificateDTO();
			String cN = IETFUtils.valueToString(cn.getFirst().getValue());
			certificateDTO.setCn(cN);
			certificateDTO.setCountry(IETFUtils.valueToString(country.getFirst().getValue()));
			certificateDTO.setGivenName(IETFUtils.valueToString(givenName.getFirst().getValue()));
			certificateDTO.setSurname(IETFUtils.valueToString(surname.getFirst().getValue()));
			certificateDTO.setEmail(IETFUtils.valueToString(email.getFirst().getValue()));
			certificateDTO.setOrganization(IETFUtils.valueToString(organization.getFirst().getValue()));
			certificateDTO.setOrganizationUnit(IETFUtils.valueToString(organizationUnit.getFirst().getValue()));
			certificateDTO.setValidTo(validFrom);
			certificateDTO.setValidFrom(validFrom);
			certificateDTO.setPublicKey(Base64Utility.encode(publicKey.getEncoded()));
			return certificateDTO;
		} catch (CertificateEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void workWithOCSP() {
		OCSPReq ocspReq;

	}
	
	/**
	 * Revoke certificate if private key is lost or for some other reason
	 * 
	 * @param serialNumber
	 */
	@RequestMapping(value = "/revokeCertificate/{alias}", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN)
	public ResponseEntity<String> revokeCertificate(@PathVariable("alias") String alias) {

		CertificateID certID = certificateIDService.findByAlias(alias);
		System.out.println("Povlacenje sertifikata");
		System.out.println("Alijsa sertifikata koji se povlaci " + alias);
		if (certID != null) {
			System.out.println("Certifikat je pronadjen i uspijesno se povlaci");
			Date dateOfRevocation = new Date();
			certID.setDateOfRevocation(dateOfRevocation);
			certID.setStatus(Status.REVOKED);
			certificateIDService.update(certID);
			return new ResponseEntity<String>("Sertifikat je uspijesno povucen.", HttpStatus.OK);

		} else {
			System.out.println("Sertifikat nije povucen.");
			return new ResponseEntity<String>("Sertifika nije pronadjen.", HttpStatus.NO_CONTENT);
		}
	}

	/**
	 * Check if certificate is valid or not.
	 * 
	 * @param serialNumber
	 */
	@RequestMapping(value = "/status/{alias}", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN)
	public ResponseEntity<String> checkCertificateStatus(@PathVariable("alias") String alias) {
		CertificateID certID = certificateIDService.findByAlias(alias);
		CertificateID tempCertID = certID;
		if (certID != null) {
			while (tempCertID.getIdOfCA() != null) {
				if (tempCertID.getStatus() == Status.REVOKED ) {
					if (tempCertID != certID) {
						certID.setStatus(Status.REVOKED);
						certID.setDateOfRevocation(new Date());
						
					}
					return new ResponseEntity<String>("Sertifikat je povucen.", HttpStatus.OK);

				}
				tempCertID=tempCertID.getCa();
			}
			
			if(certID.getStatus()==Status.OK){
				return new ResponseEntity<String>("Sertifikat je aktuelan.",HttpStatus.OK);
			}else if(certID.getStatus()==Status.UNKNOWN){
				return new ResponseEntity<String>("Status sertifikata nije poznat", HttpStatus.OK);
			}
		}
		return new ResponseEntity<String>("Sertifikat nije pronadjen.", HttpStatus.NO_CONTENT);

	}
	
	private void getKeyStore(String filePathString) throws KeyStoreException, NoSuchProviderException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException{

		File f = new File(filePathString);
		if(f.exists() && !f.isDirectory()) { 
			ks.load(new FileInputStream(filePathString), "test".toCharArray());
		}else{
			ks.load(null, "test".toCharArray());
		}
		
	}
	
	private void saveKeyStore(String filePathString) throws KeyStoreException, NoSuchProviderException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException{
		
		ks.store(new FileOutputStream(filePathString), "test".toCharArray());
		
	}
}
