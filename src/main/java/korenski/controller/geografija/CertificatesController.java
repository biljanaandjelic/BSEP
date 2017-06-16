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
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.jce.PrincipalUtil;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.annotation.JsonFormat.Value;

import korenski.DTOs.CertificateDTO;
import korenski.DTOs.CertificateRequestDTO;
import korenski.DTOs.ImportCertificateDTO;
import korenski.DTOs.KeystoreDTO;
import korenski.controller.autentifikacija.pomocneKlase.LoginObject;
import korenski.intercepting.CustomAnnotation;
import korenski.model.autorizacija.User;
import korenski.model.infrastruktura.Bank;
import korenski.model.util.CertificateInfo;
import korenski.model.util.RevokeRequest;
import korenski.model.util.CertificateInfo.CertStatus;
import korenski.model.util.CertificateInfo.Type;
import korenski.repository.institutions.BankRepository;
import korenski.service.dtos.CertificateInfoService;
import korenski.service.dtos.RevokeRequestService;
import util.Base64Utility;

@Controller
@RequestMapping("/certificates")
public class CertificatesController {
	@Autowired
	CertificateInfoService certificateInfoService;
	@Autowired
	BankRepository bankRepository;

	@Autowired
	RevokeRequestService revokeRequestService;
	
	private KeyStore ks;

	@CustomAnnotation(value = "GENERATE_CERTIFICATE")
	@RequestMapping(value = "/genCertificate", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN)
	public ResponseEntity<String> genCertificate(@RequestBody CertificateDTO dto, @Context HttpServletRequest request)
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException,
			IOException, NoSuchProviderException, OperatorCreationException, ParseException {
		
		//inicijalizacija
		KeystoreDTO keystoreSession = (KeystoreDTO)request.getSession().getAttribute("keystore");
		String filePathString = "./files/" + keystoreSession.getName() + ".jks";
		
		if (ks == null) {
			ks = KeyStore.getInstance("BKS", "BC");
		}

		getKeyStore2(filePathString, keystoreSession.getPassword());
		
		//kreiranje sertifikata
		X500NameBuilder b = new X500NameBuilder(BCStyle.INSTANCE);
		b.addRDN(BCStyle.CN, dto.cn);
		b.addRDN(BCStyle.SURNAME, dto.surname);
		b.addRDN(BCStyle.GIVENNAME, dto.givenName);
		b.addRDN(BCStyle.O, dto.organization);
		b.addRDN(BCStyle.OU, dto.organizationUnit);
		b.addRDN(BCStyle.C, dto.country);
		b.addRDN(BCStyle.E, dto.email);
		b.addRDN(BCStyle.UID, dto.uid);

		X500Name name = b.build();

		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
		keyGen.initialize(2048, random);

		KeyPair pair = keyGen.generateKeyPair();

		JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");

		builder = builder.setProvider("BC");

		ContentSigner contentSigner = builder.build(pair.getPrivate());

		Date startDate = dto.validFrom;
		Date endDate = dto.validTo;
		BigInteger serial = new BigInteger(20, new SecureRandom());
		
		System.out.println(serial);
		
		X509v3CertificateBuilder certGen;

		certGen = new JcaX509v3CertificateBuilder(name, serial, startDate, endDate, name, pair.getPublic());
		
		certGen.addExtension(Extension.basicConstraints, true, new BasicConstraints(true));
		certGen.addExtension(Extension.subjectAlternativeName, true, new GeneralNames(new GeneralName(GeneralName.rfc822Name, "localhost")));
		
		X509CertificateHolder certHolder = certGen.build(contentSigner);

		JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
		certConverter = certConverter.setProvider("BC");

		System.out.println(certConverter.getCertificate(certHolder));
		
		//potencijalno izmeniti
	
		Bank bank = ((User)request.getSession().getAttribute("user")).getBank();
		

		//smesanje kljuca i sertifikata u keystore
		ks.setCertificateEntry(dto.alias, certConverter.getCertificate(certHolder));
		//potencijalno izmeniti
	
		int cnt = 1;
		while(true){
			if(!ks.isKeyEntry("KEY-" + cnt)){
				ks.setKeyEntry("KEY-" + cnt, pair.getPrivate(), keystoreSession.getPassword().toCharArray(), new Certificate[] { (Certificate) certConverter.getCertificate(certHolder) });
				break;
			}
			cnt++;
		}
		
		//export sertifikata u fajlove projekta
		//ove sertifikate mogu importovati poslovne banke i pravna lica
		X509Certificate cert = certConverter.getCertificate(certHolder);
		X500Name x500name = new JcaX509CertificateHolder(cert).getSubject();
		RDN uid = x500name.getRDNs(BCStyle.UID)[0];
		
		File f = new File("./files/certificates/" + uid.getFirst().getValue() + "-" + cnt +  ".cer");
		BufferedWriter w = new BufferedWriter(new FileWriter(f.getPath()));
		
		StringWriter writer = new StringWriter();
		PemWriter pemWriter = new PemWriter(writer);
		
		pemWriter.writeObject(new PemObject("CERTIFICATE", cert.getEncoded()));
		
		pemWriter.close();
		w.write(writer.toString());
		writer.close();
		w.flush();
		w.close();
		
		//izmeniti
		String certificateName= uid.getFirst().getValue() + "-" + cnt;
		CertificateInfo certificateInfo = new CertificateInfo(serial, CertStatus.GOOD, null, null,  Type.NationalBank, certificateName);
		certificateInfo.setBank(bank);
		certificateInfo.setKeyStorName(keystoreSession.getName());
		certificateInfo.advancedSetKeyStorePassword(keystoreSession.getPassword());
		certificateInfo.setCaKeyStoreName(keystoreSession.getName());
		certificateInfo.advancedSetCaKeyStorePassword(keystoreSession.getPassword());
		try{
		certificateInfoService.create(certificateInfo);
		}catch(Exception e){
			return new  ResponseEntity<String>("notok", HttpStatus.OK);
		}
		//cuvanje keystore-a
		saveKeyStore2(filePathString, keystoreSession.getPassword());
		
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}
	
	@CustomAnnotation(value = "GENERATE_CERTIFICATE_REQUEST")
	@RequestMapping(value = "/genCertificateRequest", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN)
	public ResponseEntity<String> genCertificateRequest(@RequestBody CertificateDTO dto, @Context HttpServletRequest httpRequest)
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException,
			IOException, NoSuchProviderException, OperatorCreationException, ParseException, InvalidKeySpecException {
		
		//inicijalizacija
		if (ks == null) {
			ks = KeyStore.getInstance("BKS", "BC");
		}
		
		KeystoreDTO keystoreSession = (KeystoreDTO)httpRequest.getSession().getAttribute("keystore");
		
		
		//kreiranje csr zahteva
		X500NameBuilder b = new X500NameBuilder(BCStyle.INSTANCE);
		b.addRDN(BCStyle.CN, dto.cn);
		b.addRDN(BCStyle.SURNAME, dto.surname);
		b.addRDN(BCStyle.GIVENNAME, dto.givenName);
		b.addRDN(BCStyle.O, dto.organization);
		b.addRDN(BCStyle.OU, dto.organizationUnit);
		b.addRDN(BCStyle.C, dto.country);
		b.addRDN(BCStyle.E, dto.email);
		b.addRDN(BCStyle.UID, dto.uid);

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
		
		//na osnovu korisnika se csr zahtev smesta u odredjeni fajl po sablonu CSR+swift kod banke
		User u = (User)httpRequest.getSession().getAttribute("user");
		String bankSwiftCode;
		if(u.getRole().getName().equals("ADMINISTRATOR_BANK")){
			Bank bank = bankRepository.findOne(new Long(1));
			bankSwiftCode = bank.getSwiftCode();
		}else if(u.getRole().getName().equals("LEGAL")){
			bankSwiftCode = ((User)httpRequest.getSession().getAttribute("user")).getBank().getSwiftCode();
		}else{
			return null;
		}
		
		File folder = new File("./files/CSR"+bankSwiftCode);
		if(!folder.exists()){
			folder.mkdirs();
		}
		
		
		//kreiranje csr zahteva i njegovo smestanje u fajl
		int cnt = 1;
		while(true){
			File f = new File("./files/CSR"+bankSwiftCode+"/CSR-"+dto.uid + "-" +cnt+".csr");
			if(!f.exists()){
				break;
			}
			cnt++;
		}
		
		File f = new File("./files/CSR"+bankSwiftCode+"/CSR-"+dto.uid + "-" +cnt+".csr");
		BufferedWriter w = new BufferedWriter(new FileWriter(f.getPath()));
		StringWriter sw = new StringWriter();
		JcaPEMWriter pemWriter = new JcaPEMWriter(sw);
		pemWriter.writeObject(request);
		
		pemWriter.close();
		w.write(sw.toString());
		sw.close();
		w.flush();
		w.close();
		
		//preuzimanje sertifikata za privremeni potpis privatnog kljuca
		f = new File("./files/certificates/12345678-1.cer");
		BufferedReader r = new BufferedReader(new FileReader(f.getPath()));
		PemReader pemReader = new PemReader(r);
		PEMParser pemParser = new PEMParser(pemReader);
		Object o = pemParser.readObject();
		X509CertificateHolder certificateHolder = (X509CertificateHolder)o;
		
		pemParser.close();
		pemReader.close();
		r.close();
		
		JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
		certConverter = certConverter.setProvider("BC");
		
		Certificate certificate = certConverter.getCertificate(certificateHolder);
		
		getKeyStore2("./files/" + keystoreSession.getName() + ".jks", keystoreSession.getPassword());
		
		//smestanje privatnog kljuca u keystore
		cnt = 1;
		while(true){
			if(!ks.isKeyEntry("KEY-" + cnt)){
				ks.setKeyEntry("KEY-" + cnt, pair.getPrivate(), keystoreSession.getPassword().toCharArray(), new Certificate[] { (certificate) });
				break;
			}
			cnt++;
		}
		
		saveKeyStore2("./files/" + keystoreSession.getName() + ".jks", keystoreSession.getPassword());
		
		return new ResponseEntity<String>("ok", HttpStatus.OK);	
	}
	
	@CustomAnnotation(value = "GET_CERTIFICATE_REQUESTS")
	@RequestMapping(value = "/getCertificateRequests", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<Collection<CertificateRequestDTO>> getCertificateRequests(@Context HttpServletRequest httpRequest) throws IOException{
		
		//inicijalizacija
		
		ArrayList<CertificateRequestDTO> retVal = new ArrayList<CertificateRequestDTO>();
		
		
		//na osnovu ulogovanog korisnika se preuzima swift kod banke na osnovu cega se pretrazuju csr fajlovi
		User u = (User)httpRequest.getSession().getAttribute("user");
		String bankSwiftCode;
		if(u.getRole().getName().equals("ADMINISTRATOR_CENTRAL")){
			Bank bank = bankRepository.findOne(new Long(1));
			bankSwiftCode = bank.getSwiftCode();
		}else if(u.getRole().getName().equals("ADMINISTRATOR_BANK")){
			bankSwiftCode = ((User)httpRequest.getSession().getAttribute("user")).getBank().getSwiftCode();
		}else{
			return null;
		}
		
		File folder = new File("./files/CSR"+bankSwiftCode);
		if(!folder.exists()){
			folder.mkdirs();
		}
		File[] listOfFiles = folder.listFiles();
		
		//iteracija kroz csr fajlove i instanciranje DTO-ova koji ce se prikazati na frontendu
		for(File f : listOfFiles){
			
			BufferedReader r = new BufferedReader(new FileReader(f.getPath()));
			PemReader pemReader = new PemReader(r);
			PEMParser pemParser = new PEMParser(pemReader);
			Object o = pemParser.readObject();
			PKCS10CertificationRequest csr = (PKCS10CertificationRequest)o;
			
			//kreiranje pojedinacnog dto-a koji se smesta u listu za prikaz
			CertificateRequestDTO dto = new CertificateRequestDTO();
			
			dto.setId(f.getName().split("-")[1] + "-" + f.getName().split("-")[2]);
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
	
	@CustomAnnotation(value = "MAKE_CERTIFICATE")
	@RequestMapping(value = "/makeCertificate/{id}", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN)
	public ResponseEntity<String> makeCertificate(@PathVariable("id") String id, @Context HttpServletRequest httpRequest) throws IOException, KeyStoreException, NoSuchProviderException, NoSuchAlgorithmException, CertificateException, InvalidKeySpecException, UnrecoverableKeyException, OperatorCreationException{
		
		//inicijalizacija
		
		KeystoreDTO keystoreSession = (KeystoreDTO)httpRequest.getSession().getAttribute("keystore");
		
		User u = (User)httpRequest.getSession().getAttribute("user");
		String bankSwiftCode;
		
		//na osnovu swift koda ulogovanog korisnika se pretrazuju csrovi
		if(u.getRole().getName().equals("ADMINISTRATOR_CENTRAL")){
			Bank bank = bankRepository.findOne(new Long(1));
			bankSwiftCode = bank.getSwiftCode();
		}else if(u.getRole().getName().equals("ADMINISTRATOR_BANK")){
			bankSwiftCode = ((User)httpRequest.getSession().getAttribute("user")).getBank().getSwiftCode();
		}else{
			return null;
		}
		
		//preuzima se csr na osnovu prosledjenog parametra
		File f = new File("./files/CSR"+bankSwiftCode+"/CSR-"+id + ".csr");
		
		BufferedReader r = new BufferedReader(new FileReader(f.getPath()));
		PemReader pemReader = new PemReader(r);
		PEMParser pemParser = new PEMParser(pemReader);
		Object o = pemParser.readObject();
		PKCS10CertificationRequest csr = (PKCS10CertificationRequest)o;
		
		pemParser.close();
		pemReader.close();
		r.close();
		
		//inicijalizacija keystore-a
		String filePathString = "./files/" + keystoreSession.getName() + ".jks";
		
		if (ks == null) {
			ks = KeyStore.getInstance("BKS", "BC");
		}

		getKeyStore2(filePathString, keystoreSession.getPassword());
		
		//kreiranje sertifikata
		JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");

		builder = builder.setProvider("BC");

		ContentSigner contentSigner = builder.build((PrivateKey)ks.getKey("KEY-1", keystoreSession.getPassword().toCharArray()));

		Calendar cal = Calendar.getInstance();
		Date startDate = cal.getTime();
		cal.add(Calendar.YEAR, 1);
		Date endDate = cal.getTime();
		BigInteger serial = new BigInteger(20, new SecureRandom());
		
		System.out.println(serial);
		
		X509v3CertificateBuilder certGen;
		CertificateInfo certificateInfo;
		
		X509Certificate certificate = (X509Certificate)ks.getCertificate("active");
		
		X500Name issuer = X500Name.getInstance(PrincipalUtil.getIssuerX509Principal(certificate));
		
		SubjectPublicKeyInfo pkInfo = csr.getSubjectPublicKeyInfo();
		RSAKeyParameters rsa = (RSAKeyParameters) PublicKeyFactory.createKey(pkInfo);
		RSAPublicKeySpec rsaSpec = new RSAPublicKeySpec(rsa.getModulus(), rsa.getExponent());
		KeyFactory kf = KeyFactory.getInstance("RSA");
		PublicKey rsaPub = kf.generatePublic(rsaSpec);
		
		certGen = new JcaX509v3CertificateBuilder(issuer, serial, startDate, endDate, csr.getSubject(),
				rsaPub);
		
		
		CertificateInfo ca = new CertificateInfo();
		try {
			ca = certificateInfoService.findBySerialNumber(certificate.getSerialNumber());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<String>("notok", HttpStatus.OK);
		}
		
		//na osnovu prava ulogovanog korisnika kreira se sertifikat sa ca = true ili false
		//potencijalno izmeniti
		if(u.getRole().getName().equals("ADMINISTRATOR_CENTRAL")){
			certGen.addExtension(Extension.basicConstraints, true, new BasicConstraints(true));
			certificateInfo=new CertificateInfo(serial,CertStatus.GOOD,null,ca ,Type.Bank,"");
			certificateInfo.setBank(((User)httpRequest.getSession().getAttribute("user")).getBank());
		}else if(u.getRole().getName().equals("ADMINISTRATOR_BANK")){
			certGen.addExtension(Extension.basicConstraints, true, new BasicConstraints(false));
			certificateInfo=new CertificateInfo(serial,CertStatus.GOOD,null,ca,Type.Company,"");
			certificateInfo.setBank(((User)httpRequest.getSession().getAttribute("user")).getBank());
		}else{
			return new ResponseEntity<String>("notok", HttpStatus.OK);
		}
		
		X509CertificateHolder certHolder = certGen.build(contentSigner);

		JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
		certConverter = certConverter.setProvider("BC");
		
		System.out.println(certConverter.getCertificate(certHolder));

		//smestanje izgenerisanog sertifikata u keystore nadredjenog
		int cnt = 1;
		while(true){
			if(!ks.isCertificateEntry("CERT-" + IETFUtils.valueToString(csr.getSubject().getRDNs(BCStyle.UID)[0].getFirst().getValue()) + "-" + cnt)){
				ks.setCertificateEntry("CERT-" + IETFUtils.valueToString(csr.getSubject().getRDNs(BCStyle.UID)[0].getFirst().getValue()) + "-" + cnt, certConverter.getCertificate(certHolder));
			
				break;
			}
			cnt++;
		}
		
		//potencijalno izmeniti
		
		
		saveKeyStore(filePathString);
		
		//brisanje csr zahteva
		f.delete();

		//export sertifikata u lokalni fajl, ovaj sertifikat mogu importovati druge strane
		X509Certificate cert = certConverter.getCertificate(certHolder);
		X500Name x500name = new JcaX509CertificateHolder(cert).getSubject();
		RDN uid = x500name.getRDNs(BCStyle.UID)[0];
		
		f = new File("./files/certificates/" + uid.getFirst().getValue() + "-" + cnt + ".cer");
		BufferedWriter w = new BufferedWriter(new FileWriter(f.getPath()));
		
		StringWriter writer = new StringWriter();
		PemWriter pemWriter = new PemWriter(writer);
		pemWriter.writeObject(new PemObject("CERTIFICATE", cert.getEncoded()));
		
		pemWriter.close();
		w.write(writer.toString());
		writer.close();
		w.flush();
		w.close();
		
		String certificateName=uid.getFirst().getValue() + "-" + cnt;
		certificateInfo.setCertificateName(certificateName);
		certificateInfo.setCaKeyStoreName(keystoreSession.getName());
		certificateInfo.advancedSetCaKeyStorePassword(keystoreSession.getPassword());
		try{
		certificateInfoService.create(certificateInfo);
		}catch (Exception e) {
			// TODO: handle exception
			return new ResponseEntity<String>("notok", HttpStatus.OK);
		}
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}

	

	/**
	 * Metodu zamjeniti....
	 * @param alias
	 * @return
	 * @throws KeyStoreException
	 * @throws NoSuchProviderException
	 * @throws IOException 
	 * @throws CertificateException 
	 */
	@CustomAnnotation(value = "FIND_CERTIFICATE_BY_ALIAS")
	@RequestMapping(value = "/certificate/{serialNumber}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<CertificateDTO> findCertificateDTO(@PathVariable("serialNumber") BigInteger serialNumber, @Context HttpServletRequest request)
			throws KeyStoreException, NoSuchProviderException, IOException, CertificateException {
		System.out.println("Pronalazenje sertifikata na osnovu alijasa");
		CertificateInfo certInfo=certificateInfoService.findBySerialNumber(serialNumber);
		String certFileName=certInfo.getCertificateName()+".cer";
	
		File f = new File("./files/certificates/" + certFileName);
		BufferedReader r = new BufferedReader(new FileReader(f.getPath()));
		PemReader pemReader = new PemReader(r);
		PEMParser pemParser = new PEMParser(pemReader);
		Object o = pemParser.readObject();
		X509CertificateHolder certificateHolder = (X509CertificateHolder)o;
		
		pemParser.close();
		pemReader.close();
		r.close();
		
		JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
		certConverter = certConverter.setProvider("BC");
		
		Certificate certificate = certConverter.getCertificate(certificateHolder);
		CertificateDTO certificateDTO = getDataFromCertificate(certificate);
		if(certificateDTO!=null){
			return new ResponseEntity<CertificateDTO>(certificateDTO, HttpStatus.OK);
		}
//		User user=(User)request.getSession().getAttribute("user");
//		Bank bank=user.getBank();
//		if (ks == null) {
//			
//			ks = KeyStore.getInstance("BKS", "BC");
//			System.out.println("Nije prethodno instanciran");
//		}
//		try {
//			
//			String newFilePath = "./files/KEYSTORE-" + bank.getSwiftCode() + ".jks";
//			ks.load(new FileInputStream(newFilePath), "test".toCharArray());
//			System.out.println("USpijesno je uctan ks");
//		} catch (NoSuchAlgorithmException | CertificateException | IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		alias="CERT-"+alias;
//		Certificate foundCertificate = ks.getCertificate(alias);
//
//		if (foundCertificate != null) {
//
//
//			CertificateDTO certificateDTO = getDataFromCertificate(foundCertificate);
//			return new ResponseEntity<CertificateDTO>(certificateDTO, HttpStatus.OK);
//		}

		return new ResponseEntity<CertificateDTO>(HttpStatus.NOT_FOUND);
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


	


	/**
	 * Procesiranje zahtjeva za povlcenje sertifikata, u slucaju da je prosijedjen tip 
	 * accept onda
	 * ce se prihvatiti zahtjev za povlacenje i status certificateInfo ce se postaviti na 
	 * REVOKED,a u slucaju da je odbijen nece doci do promjene statusa
	 * @param id
	 * @param type
	 * @param request
	 * @return
	 * @author Biljana
	 */
	@CustomAnnotation(value = "PROCESS_REVOKE_REQUEST")
	@RequestMapping(value = "/revokeRequest/{id}/{type}", method = RequestMethod.DELETE, produces = MediaType.TEXT_PLAIN)
	public ResponseEntity<String> processRevokeRequest(@PathVariable Long id, @PathVariable String type,
			@Context HttpServletRequest request) {
		
		if(type.equals("decline")){
			return new ResponseEntity<String>("OK", HttpStatus.OK);
		}
	
		RevokeRequest revokeRequest=revokeRequestService.findRevokeRequest(id);
		CertificateInfo certInfo=certificateInfoService.findBySerialNumber(revokeRequest.getSerialNumber());
		if(certInfo.getStatus()==CertStatus.GOOD){
			certInfo.setStatus(CertStatus.REVOKED);
			certInfo.setDateOfRevocation(new Date());
			
			if(certInfo.getType() == Type.NationalBank || certInfo.getType() == Type.Bank){
				changeSubordinateCertificateStatus(certInfo);
			}
		}

		certificateInfoService.update(certInfo);
		return new ResponseEntity<String>("OK", HttpStatus.OK);

	}
	
	
	public void changeSubordinateCertificateStatus(CertificateInfo ca){
		
		if(ca.getType()==Type.NationalBank){
			Set<CertificateInfo> certificates = certificateInfoService.findByCa(ca);
			for(CertificateInfo certificate : certificates){
				if(certificate.getStatus() == CertStatus.GOOD){
					certificate.setStatus(CertStatus.REVOKED);
					certificate.setDateOfRevocation(new Date());
					certificateInfoService.update(certificate);
					Set<CertificateInfo> certificates2 = certificateInfoService.findByCa(certificate);
					for(CertificateInfo certificate2 : certificates2){
						if(certificate2.getStatus() == CertStatus.GOOD){
							certificate2.setStatus(CertStatus.REVOKED);
							certificate2.setDateOfRevocation(new Date());
							certificateInfoService.update(certificate2);
						}
					}
				}
				
			}
		}else if(ca.getType()==Type.Bank){
			Set<CertificateInfo> certificates = certificateInfoService.findByCa(ca);
			for(CertificateInfo certificate : certificates){
				if(certificate.getStatus() == CertStatus.GOOD){
					certificate.setStatus(CertStatus.REVOKED);
					certificate.setDateOfRevocation(new Date());
					certificateInfoService.update(certificate);
				}
				
			}
		}
		
	}
	/**
	 * 
	 * @param request
	 * @return
	 * @author Biljana
	 */
	@CustomAnnotation(value = "GET_REVOKE_REQUEST")
	@RequestMapping(value = "/revokeRequest", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<Set<RevokeRequest>> revokeRequest(@Context HttpServletRequest request) {
		User loginUser = (User) request.getSession().getAttribute("user");
		if (loginUser != null) {
			System.out.println("Korisnik je ulogovan i njegova banka je " + loginUser.getBank().getName());
			Bank bank = loginUser.getBank();
			Set<RevokeRequest> revocationRequests = revokeRequestService.findRevokeRequestByBank(bank);
			if (revocationRequests != null) {
				System.out.println("Pronadjeni su neki zahtjevi upuceni ka banci ciji admin je ulogovan");
			} else {
				System.out.println("Nisu pronadjeni zahtjevi upuceni ka banci");
			}
			return new ResponseEntity<Set<RevokeRequest>>(revocationRequests, HttpStatus.OK);
		}
		System.out.println("Korisnik nije ulogovan");
		return null;
	}
	

	/**
	 * Slanje zahtjeva za povlacenje......
	 * @param revokeRequest
	 * @param request
	 * @return
	 * @author Biljana
	 */
	@CustomAnnotation(value = "SAVE_REVOKE_REQUEST")
	@RequestMapping(value = "/createRevokeRequest", method = RequestMethod.POST,
			produces = MediaType.TEXT_PLAIN)
	public ResponseEntity<String> saveRevokeRequest(@RequestBody RevokeRequest revokeRequest,
			@Context HttpServletRequest request) {
		System.out.println("Sacuvaj zahtjev!!!!");

		CertificateInfo certificateInfo=null;
	
		certificateInfo=certificateInfoService.findBySerialNumber(revokeRequest.getSerialNumber());
		revokeRequest.setBank(certificateInfo.getBank());
		System.out.println("Request alijas " + revokeRequest.getBank().getName());
		RevokeRequest revokeRequestPersist = revokeRequestService.save(revokeRequest);
		if (revokeRequestPersist == null) {
			return new ResponseEntity<String>("Doslo je do geske. Pokusajte ponovo.", HttpStatus.OK);
		}
		return new ResponseEntity<String>("Zahtjev za povlacenje sertifikata je zabiljezen.", HttpStatus.OK);
		

	}
	private void getKeyStore(String filePathString) throws KeyStoreException, NoSuchProviderException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException{

		File f = new File(filePathString);
		if(f.exists() && !f.isDirectory()) { 
			ks.load(new FileInputStream(filePathString), "test".toCharArray());
		}else{
			ks.load(null, "test".toCharArray());
		}
		
	}
	
	private void getKeyStore2(String filePathString, String password) throws KeyStoreException, NoSuchProviderException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException{

		File f = new File(filePathString);
		if(f.exists() && !f.isDirectory()) { 
			ks.load(new FileInputStream(filePathString), password.toCharArray());
		}else{
			ks.load(null, password.toCharArray());
		}
		
	}
	
	private void saveKeyStore(String filePathString) throws KeyStoreException, NoSuchProviderException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException{
		
		ks.store(new FileOutputStream(filePathString), "test".toCharArray());
		
	}
	
	private void saveKeyStore2(String filePathString, String password) throws KeyStoreException, NoSuchProviderException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException{
		
		ks.store(new FileOutputStream(filePathString), password.toCharArray());
		
	}
	
	@RequestMapping(value = "/validateXML", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN)
	public ResponseEntity<String> validateXML(@Context HttpServletRequest request) {
		
		String retVal;
		
		try
	    {
	        SchemaFactory factory = 
	            SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	        Schema schema = factory.newSchema(new StreamSource(new File("./files/xml_tests/Faktura.xsd")));
	        Validator validator = schema.newValidator();
	        validator.validate(new StreamSource(new File("./files/xml_tests/Faktura.xml")));
	        retVal = "ok";
	    }
	    catch(Exception ex)
	    {
	        retVal = "notok";
	    }
		
		return new ResponseEntity<String>(retVal, HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "/openKeystore",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON,
			produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<KeystoreDTO> openKeystore(@RequestBody KeystoreDTO dto, @Context HttpServletRequest request) throws Exception {
		
		//inicijalizacija
		if (ks == null) {
			ks = KeyStore.getInstance("BKS", "BC");
		}
		
		KeystoreDTO keystoreSession = (KeystoreDTO)request.getSession().getAttribute("keystore");
		User u = (User)request.getSession().getAttribute("user");
		
		
		//ako korisnik nije ulogovan prebaci ga na login stranicu
		if(u == null){
			dto.setId(-2);
			
			String url = "";
			String scheme = request.getScheme();
			String host = request.getServerName();
			int port = request.getServerPort();
			url = url.concat(scheme).concat("://").concat(host).concat(":"+Integer.toString(port)).concat("/authentification/login.html");
			
			dto.setUrl(url);
			
			return new ResponseEntity<KeystoreDTO>(dto, HttpStatus.OK);
		}
		

		if(keystoreSession != null){
			//ako ima vec keystore u sesiji
			dto.setId(-1);
			
			return new ResponseEntity<KeystoreDTO>(dto, HttpStatus.OK);
		}else{
			//provera da li su ispravni naziv/lozinka keystore-a
			File f = new File("./files/" + dto.getName() + ".jks");
			if(f.exists() && !f.isDirectory()) {
				try{
					ks.load(new FileInputStream(f.getPath()), dto.getPassword().toCharArray());
				}catch(Exception e){
					dto.setId(-3);
					
					return new ResponseEntity<KeystoreDTO>(dto, HttpStatus.OK);
				}
			}else if(!f.exists()){
				dto.setId(-3);
				
				return new ResponseEntity<KeystoreDTO>(dto, HttpStatus.OK);
			}
			
			request.getSession().setAttribute("keystore", dto);
		}
		
		//redirekcija na stranice za rukovanje kljucevima/sertifikatima u zavisnosti od korisnicke role
		if(u.getRole().getName().equals("ADMINISTRATOR_CENTRAL")){
			dto.setId(1);
			
			String url = "";
			String scheme = request.getScheme();
			String host = request.getServerName();
			int port = request.getServerPort();
			url = url.concat(scheme).concat("://").concat(host).concat(":"+Integer.toString(port)).concat("/certificates/certificates.html");
			
			dto.setUrl(url);
		}else if(u.getRole().getName().equals("ADMINISTRATOR_BANK")){
			dto.setId(1);
			
			String url = "";
			String scheme = request.getScheme();
			String host = request.getServerName();
			int port = request.getServerPort();
			url = url.concat(scheme).concat("://").concat(host).concat(":"+Integer.toString(port)).concat("/certificates/certificatesAdmin.html");
			
			dto.setUrl(url);
		}else if(u.getRole().getName().equals("LEGAL")){
			dto.setId(1);
			
			String url = "";
			String scheme = request.getScheme();
			String host = request.getServerName();
			int port = request.getServerPort();
			url = url.concat(scheme).concat("://").concat(host).concat(":"+Integer.toString(port)).concat("/certificates/certificatesLegal.html");
			
			dto.setUrl(url);
		}
		
		return new ResponseEntity<KeystoreDTO>(dto, HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "/closeKeystore",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<KeystoreDTO> closeKeystore(@Context HttpServletRequest request) throws KeyStoreException, NoSuchProviderException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException {
		
		//inicijalizacija
		KeystoreDTO keystoreSession = (KeystoreDTO)request.getSession().getAttribute("keystore");
		
		if (ks == null) {
			ks = KeyStore.getInstance("BKS", "BC");
		}
		
		
		//redirekcija na stranicu za otvaranje keystore-a
		keystoreSession.setId(1);
		
		String url = "";
		String scheme = request.getScheme();
		String host = request.getServerName();
		int port = request.getServerPort();
		url = url.concat(scheme).concat("://").concat(host).concat(":"+Integer.toString(port)).concat("/certificates/openkeystore.html");
		
		keystoreSession.setUrl(url);
		
		saveKeyStore2("./files/" + keystoreSession.getName() + ".jks", keystoreSession.getPassword());
		
		request.getSession().removeAttribute("keystore");
		
		return new ResponseEntity<KeystoreDTO>(keystoreSession, HttpStatus.OK);
	}
	
	@RequestMapping(
			value = "/importCert",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON,
			produces = MediaType.TEXT_PLAIN)
	public ResponseEntity<String> importCert(@RequestBody ImportCertificateDTO dto, @Context HttpServletRequest request) throws KeyStoreException, NoSuchProviderException, IOException, CertificateException, NoSuchAlgorithmException {
		
		//inicijalizacija
		KeystoreDTO keystoreSession = (KeystoreDTO)request.getSession().getAttribute("keystore");
		
		if (ks == null) {
			ks = KeyStore.getInstance("BKS", "BC");
		}
		
		//preuzimanje sertifikata iz fajl sistema
		File f = new File("./files/certificates/" + dto.getName() + ".cer");
		BufferedReader r = new BufferedReader(new FileReader(f.getPath()));
		PemReader pemReader = new PemReader(r);
		PEMParser pemParser = new PEMParser(pemReader);
		Object o = pemParser.readObject();
		X509CertificateHolder certificateHolder = (X509CertificateHolder)o;
		
		pemParser.close();
		pemReader.close();
		r.close();
		
		JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
		certConverter = certConverter.setProvider("BC");
		
		Certificate certificate = certConverter.getCertificate(certificateHolder);
		/*
		 * Preuzimanje serijskog broja iz sertifikata da bi mu mogli pristupiti u bazi i da 
		 * bi mogli da mu setujemo keystore name i keystore password  u kom se nalazi privatni
		 * kljuc sertifikata
		 */
		BigInteger serialNumber=null;
		X500Name x500name = new JcaX509CertificateHolder((X509Certificate) certificate).getSubject();
			if (x500name != null) {
				X509Certificate x509Cert = (X509Certificate) certificate;
				 serialNumber = x509Cert.getSerialNumber();
			}
		if(serialNumber!=null){
		CertificateInfo certificateInfo=certificateInfoService.findBySerialNumber(serialNumber);
		certificateInfo.advancedSetKeyStorePassword(keystoreSession.getPassword());
		certificateInfo.setKeyStorName(keystoreSession.getName());
		try{
			certificateInfoService.create(certificateInfo);
		}catch (Exception e) {
			// TODO: handle exception
			return new ResponseEntity<String>("notok", HttpStatus.OK);
		}
		}
		getKeyStore2("./files/" + keystoreSession.getName() + ".jks", keystoreSession.getPassword());
		
		//smestanje sertifikata pod zadatim alijasom
		ks.setCertificateEntry(dto.getAlias(), certificate);
		
		saveKeyStore2("./files/" + keystoreSession.getName() + ".jks", keystoreSession.getPassword());
		
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}
	
	//pomocna metoda za generisanje keystore-ova, potencijalno izbrisati pred rok
	@RequestMapping(value = "/generateKeystores", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN)
	public ResponseEntity<String> generateKeystores(@Context HttpServletRequest request) throws KeyStoreException, NoSuchProviderException, NoSuchAlgorithmException, CertificateException, IOException {
		
		if (ks == null) {
			ks = KeyStore.getInstance("BKS", "BC");
		}
		
		ks.load(null, "prvi".toCharArray());
		ks.store(new FileOutputStream("./files/prvi.jks"), "prvi".toCharArray());
		
		ks.load(null, "drugi".toCharArray());
		ks.store(new FileOutputStream("./files/drugi.jks"), "drugi".toCharArray());
		
		ks.load(null, "treci".toCharArray());
		ks.store(new FileOutputStream("./files/treci.jks"), "treci".toCharArray());
		
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}
}