package korenski.controller.ocsp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import korenski.intercepting.CustomAnnotation;
import korenski.model.autorizacija.User;
import korenski.model.infrastruktura.Bank;
import korenski.model.klijenti.Employee;
import korenski.model.klijenti.Klijent;
import korenski.model.klijenti.PravnoLice;
import korenski.model.util.CertificateInfo;
import korenski.model.util.CertificateInfo.CertStatus;
import korenski.ocsp.BasicOCSPResponse;
import korenski.ocsp.CAData;
import korenski.ocsp.CertID;
import korenski.ocsp.CertID.HashAlgorithm;
import korenski.ocsp.OCSPRequest;
import korenski.ocsp.OCSPResponse;
import korenski.ocsp.OCSPResponse.OCSPResponseStatus;
import korenski.ocsp.Request;
import korenski.ocsp.ResponseData;
import korenski.ocsp.RevocationInfo;
import korenski.ocsp.SingleResponse;

import korenski.ocsp.TBSRequest;
import korenski.ocsp.TBSRequest.Version;
import korenski.repository.institutions.BankRepository;
import korenski.service.dtos.CertificateInfoService;
import korenski.util.Base64Utility;

/**
 * Klasa koja simulira OCSPResponder koji prima podatke od klijenta na osnovu
 * kojih generise OCSP request i na osnovu njega kreira odgovor.
 * 
 * @author Biljana
 *
 */
@Controller
public class OCSPResponder {
	@Autowired
	CertificateInfoService certificateInfoService;
	@Autowired
	BankRepository bankRepository;

	private KeyStore ks;

	/**
	 * Generisanje OSCPRequest-a na osnovu podataka primljenih sa korisnicke
	 * forme u kojoj registrovani korisnik na sistem unosi alijas sertifikata
	 * ciji status zeli da provjeri.
	 * 
	 * @param bank
	 * @param alias
	 * @param requestorName
	 * @return
	 * @author Biljana
	 */
	public OCSPRequest generateOcspRequest(Bank bank, BigInteger serialNumber, String requestorName) {
		String swiftCode = bank.getSwiftCode();
		CertificateInfo certInfo = certificateInfoService.findBySerialNumber(serialNumber);
		// certInfo.advancedGetCaKeyStorePassword();
		if(certInfo!=null){
		certInfo.advancedGetKeyStorePassword();
		System.out.println("---------------------------------------------");
		System.out.println("Keystore name " + certInfo.getKeyStorName());
		System.out.println("---------------------------------------------");
		CertID certID;
		if(certInfo.getCa()!=null){
			certID = new CertID(HashAlgorithm.SHA1withRSA, certInfo.getCa().getBank().getSwiftCode(), "nestoK", serialNumber);
		}else{
			certID = new CertID(HashAlgorithm.SHA1withRSA, certInfo.getBank().getSwiftCode(), "nestoK", serialNumber);
		}
		Request req = new Request(certID);
		TBSRequest tbsReq = new TBSRequest(Version.V1, requestorName);
		tbsReq.add(req);
		OCSPRequest ocspReq = new OCSPRequest(tbsReq);

		return ocspReq;
		}else{
			return null;
		}

	}

	/**
	 * Ukloniti banku nakon sto logovanje za pravna lica bude zavrseno
	 * 
	 * @param bank
	 * @param alias
	 * @param request
	 * @return
	 * @author Biljana
	 */
	@CustomAnnotation(value = "OCSP_RESPONSE")
	@RequestMapping(value = "/ocspResponse/{serialNumber}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<OCSPResponse> processOcspRequest(@PathVariable BigInteger serialNumber,
			@Context HttpServletRequest request) {
		//Logger logger=LoggerFactory.getInstance();
		User user = (User) request.getSession().getAttribute("user");
		Bank bank;
		String name = "";
		if (user.getSubject() instanceof PravnoLice) {
			bank = user.getBank();
			name = ((PravnoLice) user.getSubject()).getPib();
		} else if (user.getSubject() instanceof Klijent) {

			name = ((Klijent) user.getSubject()).getJmbg();
		} else if (user.getSubject() instanceof Employee) {
			bank = bankRepository.findOne(new Long(1));
			name = ((Employee) user.getSubject()).getName();
		}
		bank = user.getBank();

		OCSPRequest ocspReq = generateOcspRequest(bank, serialNumber, name);
		if (ocspReq != null) {
			OCSPResponse ocspResp = new OCSPResponse(OCSPResponseStatus.SUCCESSFUL);
			ResponseData responseData = new ResponseData(Version.V1, new Date());
			BasicOCSPResponse basicOCSPResponse = new BasicOCSPResponse();
			ocspResp.setRespnseBytes(basicOCSPResponse);

			basicOCSPResponse.setResponseData(responseData);
			if (ocspReq.getTbsRequest().getVersion() == Version.V1
					|| ocspReq.getTbsRequest().getVersion() == Version.V2) {
				for (Request req : ocspReq.getTbsRequest().getRequestList()) {
					if (req.getReqCert().getHashAlgorithm() == HashAlgorithm.SHA1withRSA
							|| req.getReqCert().getHashAlgorithm() == HashAlgorithm.SHA256WithRSA) {

						responseData.add(generateSingleResponse(bank, req.getReqCert().getSeriaNumber()));
					} else {
						return new ResponseEntity<OCSPResponse>(new OCSPResponse(OCSPResponseStatus.MALFORMEDREQUEST),
								HttpStatus.OK);
					}
				}
				//CAData caData=null;
				CAData caData = getCA(bank,
						ocspReq.getTbsRequest().getRequestList().get(0).getReqCert().getSeriaNumber());
				if (caData != null && caData.getCertificate() != null) {
					HashAlgorithm algorithm=ocspReq.getTbsRequest().getRequestList().get(0).getReqCert().getHashAlgorithm();
					byte[] signature = sign(ocspResp.getRespnseBytes().toString().getBytes(), caData.getPrivateKey(),algorithm);
					if (verify(ocspResp.getRespnseBytes().toString().getBytes(), signature,
							caData.getCertificate().getPublicKey(),algorithm)) {
						return new ResponseEntity<OCSPResponse>(ocspResp, HttpStatus.OK);
					} else {
						return new ResponseEntity<OCSPResponse>(ocspResp, HttpStatus.OK);
					}

				} else {
					return new ResponseEntity<OCSPResponse>(ocspResp, HttpStatus.OK);
				}
				
			} else {

				return new ResponseEntity<OCSPResponse>(new OCSPResponse(OCSPResponseStatus.MALFORMEDREQUEST),
						HttpStatus.OK);
			}

		}
		return new ResponseEntity<OCSPResponse>(new OCSPResponse(OCSPResponseStatus.MALFORMEDREQUEST), HttpStatus.OK);
	}

	/**
	 * Preouzimanje keyStore-a u kome se ocekuje da se nalazi sertifikat na
	 * osnovu putanje do fajla.
	 * 
	 * @param filePathString
	 * @return
	 * @throws KeyStoreException
	 * @throws NoSuchProviderException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @author Biljana
	 */
	private KeyStore getKeyStore(String filePathString,String password) throws KeyStoreException, NoSuchProviderException,
			NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException {
		// KeyStore ks=null;
		System.out.println("FilePath "+filePathString);
		if (ks == null) {
			ks = KeyStore.getInstance("BKS", "BC");
		}
	//	String newFilePath = "./files/KEYSTORE-" + filePathString + ".jks";
		try {
			File f = new File(filePathString);
			if (f.exists() && !f.isDirectory()) {
				System.out.println("Password "+password);
				ks.load(new FileInputStream(filePathString), password.toCharArray());
				System.out.println("Pronadjen jks");
			} else {
				
				 ks.load(null, password.toCharArray());
			}
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("Nesto je krenulo lose sa ucitavanjem keystore");
		}
		return ks;
	}

	/**
	 * Generisanje SingleResponse objekta koji nosi podatke o statusu
	 * sertifikata jednog od proslijedjenih sertifikata cija provjera status je
	 * zatrazena. Provjera krece od samog sertifikata za koga je poslan upit pa
	 * sve do korijenskog sertifikata. Ukoliko se u "lancu" sertifikata pronadje
	 * jedna koji je povucen status trazenog se postavlja na "REVOKE" i objekat
	 * se vraca.
	 * 
	 * @param bank
	 * @param serialNumber
	 * @return
	 */
	public SingleResponse generateSingleResponse(Bank bank, BigInteger serialNumber) {

		// CertificateInfo certInfo =
		// certificateInfoService.findCertificateBySerialNumberAndBank(serialNumber,
		// bank);

		CertificateInfo certInfo = certificateInfoService.findBySerialNumber(serialNumber);
		SingleResponse singleResp;
		if (certInfo == null) {
			singleResp = new SingleResponse(CertStatus.UNKNOWN, null, null, null, null);
		} else {
			/*
			 * Prolazak kroz sve serifikate koji se nalaze na putu do ruta i
			 * provjera da li je neki od njih povucen, ukoliko jeste automatski
			 * se povlaci i sertifikat za ciju provjeru statusa je poslan
			 * zahtjev
			 */
			CertificateInfo tempCertInfo = certInfo;
			RevocationInfo info;
			while (tempCertInfo.getIdOfCA() != null) {
				if (tempCertInfo.getStatus() == CertStatus.REVOKED) {
					if (tempCertInfo != certInfo) {
						certInfo.setStatus(CertStatus.REVOKED);
						certInfo.setDateOfRevocation(new Date());

						break;
					}

				}
				tempCertInfo = tempCertInfo.getCa();
			}
			info = new RevocationInfo(certInfo.getDateOfRevocation());
			singleResp = new SingleResponse(certInfo.getStatus(), null, new Date(), null, info);
		}
		return singleResp;
	}

	/**
	 * AKO NEMAMO POTPISIVANJE OCSP-a NE TREBA NAM METODA Precuzimanje CAData
	 * objekta koji sadrzi privatni kljuc issuer-a da bi mogao da potpise
	 * odgovor i njegov sertifikat da bi odgovor mogao biti validiran.
	 * 
	 * @param bank
	 * @param serialNumber
	 * @return
	 */
	public CAData getCA(Bank bank, BigInteger serialNumber) {
		// CertificateInfo certificateInfo =
		// certificateInfoService.findCertificateBySerialNumberAndBank(serialNumber,
		// bank);
		CertificateInfo certificateInfo = certificateInfoService.findBySerialNumber(serialNumber);
		if (certificateInfo != null && certificateInfo.getCa() != null) {
			CertificateInfo ca = certificateInfo.getCa();
			String pathForPrivateKey=ca.getKeyStorName();
			String passwordForPrivateKey=ca.advancedGetKeyStorePassword();
			String pathForCert=ca.getCaKeyStoreName();
			String passwordForCert=ca.advancedGetCaKeyStorePassword();
			String aliasKey=ca.getKeyAlias();
			String aliasCert=ca.getCertAlias();
			
			try {
				KeyStore ks = getKeyStore(pathForPrivateKey,passwordForPrivateKey);
				KeyStore certKs=getKeyStore(pathForCert,passwordForCert);
				if (ks != null) {
					
					PrivateKey key = (PrivateKey) ks.getKey(aliasKey, passwordForPrivateKey.toCharArray());
					if(key!=null){
						System.out.println("Privatni kjuc je pronadjen");
					}else{
						System.out.println("Privatni kljuc nije pronadjen");
					}
					Certificate cert=certKs.getCertificate(aliasCert);
					if(cert!=null){
						System.out.println("Cert je prnadjen");
					}else{
						System.out.println("CERT nije pronadjen");
					}
					System.out.println("__________________________________");
					System.out.println("GET__CA___DATA");
					System.out.println("__________________________________");
					return new CAData(cert, key);
				}
			} catch (KeyStoreException | NoSuchProviderException | NoSuchAlgorithmException | CertificateException
					| IOException e) {
				
				//e.printStackTrace();
			} catch (UnrecoverableKeyException e) {

				//e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Potpisivisivanje podataka
	 * 
	 * @param data
	 *            podaci koji se trebaju potpisati
	 * @param privateKey
	 *            kljuc kojim se potpisuju
	 * @return niz bajtova koji predstavljaju potpis
	 */
	private byte[] sign(byte[] data, PrivateKey privateKey,HashAlgorithm hashAlgotirhm) {
		try {
			System.out.println("Private Key(str): "+new String(privateKey.getEncoded()));
			System.out.println("Private Key(enc): "+ Base64Utility.encode(privateKey.getEncoded()));
			System.out.println("POTPISIVANJE");
			// Kreiranje objekta koji nudi funkcionalnost digitalnog
			// potpisivanja
			// Prilikom getInstance poziva prosledjujemo algoritam koji cemo
			// koristiti
			// U ovom slucaju cemo generisati SHA-1 hes kod koji cemo potpisati
			// upotrebom RSA asimetricne sifre
			Signature sig;
			if(hashAlgotirhm==HashAlgorithm.SHA256WithRSA){
				sig=Signature.getInstance("SHA256WithRSA");
			}else{
			 sig= Signature.getInstance("SHA1withRSA");
			}
			// Navodimo kljuc kojim potpisujemo
			sig.initSign(privateKey);
			// Postavljamo podatke koje potpisujemo
			sig.update(data);

			// Vrsimo potpisivanje
			return sig.sign();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Provjera potpisa
	 * 
	 * @param data
	 *            podaci koji su potpisani "plain" podaci
	 * @param signature
	 *            potpis nad podacima
	 * @param publicKey
	 *            janvi kljuc kojim se vrsi provjera potpisa
	 * @return
	 */
	private boolean verify(byte[] data, byte[] signature, PublicKey publicKey,HashAlgorithm hashAlgorithm) {
		try {
			System.out.println("Private Key(str): "+new String(publicKey.getEncoded()));
			System.out.println("Private Key(enc): "+ Base64Utility.encode(publicKey.getEncoded()));
			System.out.println("PROVJERA");
			// Kreiranje objekta koji nudi funkcionalnost digitalnog
			// potpisivanja
			// Prilikom getInstance poziva prosledjujemo algoritam koji cemo
			// koristiti
			// U ovom slucaju cemo generisati SHA-1 hes kod koji cemo potpisati
			// upotrebom RSA asimetricne sifre
			Signature sig;
			if(hashAlgorithm==HashAlgorithm.SHA256WithRSA){
				sig=Signature.getInstance("SHA256WithRSA");
			}else{
			sig = Signature.getInstance("SHA1withRSA");
			}
			// Navodimo kljuc sa kojim proveravamo potpis
			sig.initVerify(publicKey);
			// Postavljamo podatke koje potpisujemo
			sig.update(data);

			// Vrsimo proveru digitalnog potpisa
			return sig.verify(signature);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		}
		return false;
	}
}
