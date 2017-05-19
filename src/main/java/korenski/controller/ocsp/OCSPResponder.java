package korenski.controller.ocsp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import korenski.model.dto.CertificateInfo;
import korenski.model.dto.CertificateInfo.CertStatus;
import korenski.model.infrastruktura.Bank;
import korenski.ocsp.BasicOCSPResponse;
import korenski.ocsp.CertID;
import korenski.ocsp.CertID.HashAlgorithm;
import korenski.ocsp.OCSPRequest;
import korenski.ocsp.OCSPResponse;
import korenski.ocsp.OCSPResponse.OCSPResponseStatus;
import korenski.ocsp.Request;
import korenski.ocsp.ResponseData;
import korenski.ocsp.SingleResponse;
//import korenski.ocsp.SingleResponse.CertStatus;
import korenski.ocsp.TBSRequest;
import korenski.ocsp.TBSRequest.Version;
import korenski.service.dtos.CertificateInfoService;

@Controller
public class OCSPResponder {
	@Autowired
	CertificateInfoService certificateInfoService;

	/**
	 * 
	 * @param bank
	 * @param alias
	 * @param requestorName
	 * @return
	 * @author Biljana
	 */

	public OCSPRequest generateOcspRequest(Bank bank, String alias, String requestorName) {
		String swiftCode = bank.getSwiftCode();
		try {
			KeyStore ks = getKeyStore(swiftCode);
			if (ks != null) {
				Certificate certificat = ks.getCertificate(alias);
				if (certificat != null) {
					X500Name x500name = new JcaX509CertificateHolder((X509Certificate) certificat).getSubject();
					RDN nesto = x500name.getRDNs(BCStyle.SERIALNUMBER)[0];
					BigInteger serialNumber = new BigInteger(IETFUtils.valueToString(nesto.getFirst().getValue()));
					CertID certID = new CertID(HashAlgorithm.SHA1withRSAencryption, "nestoI", "nestoK", serialNumber);
					Request req = new Request(certID);
					TBSRequest tbsReq = new TBSRequest(Version.V1, requestorName);
					tbsReq.add(req);
					OCSPRequest ocspReq = new OCSPRequest(tbsReq);
					return ocspReq;
				}
			}
		} catch (KeyStoreException | NoSuchProviderException | NoSuchAlgorithmException | CertificateException
				| IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
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
	@RequestMapping(value = "/ocspResponse/{alias}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<OCSPResponse> processOcspRequest(@RequestBody Bank bank, @PathVariable String alias,
			@Context HttpServletRequest request) {
		// Preuzeti responder name iz sesije
		OCSPRequest ocspReq = generateOcspRequest(bank, alias, "requestorName");
		if (ocspReq != null) {
			OCSPResponse ocspResp = new OCSPResponse(OCSPResponseStatus.SUCCESSFUL);
			ResponseData responseData = new ResponseData(Version.V1, new Date());
			BasicOCSPResponse basicOCSPResponse = new BasicOCSPResponse();
			ocspResp.setRespnseBytes(basicOCSPResponse);
			basicOCSPResponse.setResponseData(responseData);
			if (ocspReq.getTbsRequest().getVersion() == Version.V1
					|| ocspReq.getTbsRequest().getVersion() == Version.V2) {
				for (Request req : ocspReq.getTbsRequest().getRequestList()) {
					if (req.getReqCert().getHashAlgorithm() == HashAlgorithm.SHA1withRSAencryption
							|| req.getReqCert().getHashAlgorithm() == HashAlgorithm.SHA256WithRSAEncryption) {
						// req.getReqCert().get
						responseData.add(generateSingleResponse(bank, req.getReqCert().getSeriaNumber()));
					} else {
						return new ResponseEntity<OCSPResponse>(new OCSPResponse(OCSPResponseStatus.MALFORMEDREQUEST),
								HttpStatus.OK);
					}
				}
			} else {
				// OCSPResponse ocspResp=new
				// OCSPResponse(OCSPResponseStatus.MALFORMEDREQUEST)
				return new ResponseEntity<OCSPResponse>(new OCSPResponse(OCSPResponseStatus.MALFORMEDREQUEST),
						HttpStatus.OK);
			}
			return new ResponseEntity<OCSPResponse>(ocspResp, HttpStatus.OK);
		}
		return new ResponseEntity<OCSPResponse>(new OCSPResponse(OCSPResponseStatus.MALFORMEDREQUEST),
				HttpStatus.OK);
	}

	/**
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
	private KeyStore getKeyStore(String filePathString) throws KeyStoreException, NoSuchProviderException,
			NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException {
		KeyStore ks = null;
		File f = new File(filePathString);
		if (f.exists() && !f.isDirectory()) {
			ks.load(new FileInputStream(filePathString), "test".toCharArray());
		} else {
			// ks.load(null, "test".toCharArray());
		}
		return ks;
	}

	public SingleResponse generateSingleResponse(Bank bank, BigInteger serialNumber) {
		CertificateInfo certInfo = certificateInfoService.findCertificateBySerialNumberAndBank(serialNumber, bank);
		SingleResponse singleResp;
		if (certInfo != null) {
			singleResp = new SingleResponse(CertStatus.UNKNOWN, null, null, null);
		} else {
			/*
			 * Prolazak kroz sve serifikate koji se nalaze na putu do ruta i provjera da li je neki
			 * od njih povucen, ukoliko jeste automatski se povlaci i sertifikat za ciju provjeru statusa 
			 * je poslan zahtjev
			 */
			CertificateInfo tempCertInfo=certInfo;
			while (tempCertInfo.getIdOfCA() != null) {
				if (tempCertInfo.getStatus() == CertStatus.REVOKED) {
					if (tempCertInfo != certInfo) {
						certInfo.setStatus(CertStatus.REVOKED);
						certInfo.setDateOfRevocation(new Date());
						break;
					}
					//return new ResponseEntity<String>("Sertifikat je povucen.", HttpStatus.OK);

				}
				tempCertInfo= tempCertInfo.getCa();
			}
			singleResp = new SingleResponse(certInfo.getStatus(), null, new Date(), null);
		}
		return singleResp;
	}
}
