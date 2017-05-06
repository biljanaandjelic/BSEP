package korenski.controller.geografija;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
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
import java.text.ParseException;
import java.util.Date;
import java.util.Enumeration;

import javax.ws.rs.core.MediaType;

import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.cert.ocsp.OCSPReq;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import korenski.DTOs.CertificateDTO;

@Controller
@RequestMapping("/certificates")
public class CertificatesController {

	private KeyStore ks;

	@RequestMapping(value = "/genCertificate", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN)
	public ResponseEntity<String> genCertificate(@RequestBody CertificateDTO dto)
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException,
			IOException, NoSuchProviderException, OperatorCreationException, ParseException {

		if (ks == null) {
			ks = KeyStore.getInstance("JCEKS", "SunJCE");
		}

		ks.load(new FileInputStream("./files/keystore.jks"), "test".toCharArray());

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

		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = dto.validFrom;
		Date endDate = dto.validTo;

		X509v3CertificateBuilder certGen;
		if (dto.selfSigned) {
			certGen = new JcaX509v3CertificateBuilder(name, new BigInteger("1"), startDate, endDate, name,
					pair.getPublic());
		} else {
			Certificate issuerCertificate = ks.getCertificate(dto.issuerAlias);

			if (((X509Certificate) issuerCertificate).getNotAfter().before(new Date())) {
				return new ResponseEntity<String>("Issuer certificate is no longer valid", HttpStatus.OK);
			} else if (((X509Certificate) issuerCertificate).getBasicConstraints() != -1) {
				return new ResponseEntity<String>("Issuer certificate is not CA!", HttpStatus.OK);
			}

			X500Name issuerName = new JcaX509CertificateHolder((X509Certificate) issuerCertificate).getIssuer();

			certGen = new JcaX509v3CertificateBuilder(issuerName, new BigInteger("1"), startDate, endDate, name,
					pair.getPublic());
		}

		certGen.addExtension(Extension.basicConstraints, true, new BasicConstraints(dto.ca));

		X509CertificateHolder certHolder = certGen.build(contentSigner);

		JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
		certConverter = certConverter.setProvider("BC");

		ks.setCertificateEntry(dto.alias, certConverter.getCertificate(certHolder));
		ks.setKeyEntry(dto.keyAlias, pair.getPrivate().getEncoded(),
				new Certificate[] { (Certificate) certConverter.getCertificate(certHolder) });

		ks.store(new FileOutputStream("./files/keystore.jks"), "test".toCharArray());

		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}

	@RequestMapping(value = "/certificate/{alias}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<CertificateDTO> findCertificateDTO(@PathVariable("alias") String alias)
			throws KeyStoreException, NoSuchProviderException {
		System.out.println("Pronalazenje sertifikata na osnovu alijasa");
		if (ks == null) {
			ks = KeyStore.getInstance("JCEKS", "SunJCE");
		}
		try {
			ks.load(new FileInputStream("./files/keystore.jks"), "test".toCharArray());
		} catch (NoSuchAlgorithmException | CertificateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Certificate foundCertificate = ks.getCertificate(alias);

		if (foundCertificate != null) {
			System.out.println("*******************************************");
			System.out.println(foundCertificate.toString());
			System.out.println("*******************************************");
			System.out.println("*******************************************");
			System.out.println("CN: " + foundCertificate.getType().equals("CN"));
			// System.out.println("SURNAME "+
			// foundCertificate.getType().equals(anObject));
			System.out.println("*******************************************");
			CertificateDTO certificateDTO = getDataFromCertificate(foundCertificate);
			return new ResponseEntity<CertificateDTO>(certificateDTO, HttpStatus.OK);
		}

		return new ResponseEntity<CertificateDTO>(HttpStatus.NOT_FOUND);
	}

	public CertificateDTO getCertificateBySerialNumber(String serialNumber) {
		try {
			ks.load(new FileInputStream("./files/keystore.jks"), "test".toCharArray());
			Enumeration aliases = ks.aliases();
			String alias;
			for (; aliases.hasMoreElements();) {
				alias = (String) aliases.nextElement();

				Certificate cert = ks.getCertificate(alias);

				if (cert.getType().equals(BCStyle.SERIALNUMBER)) {
					// if(cert.getType().)
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
			// certificateDTO.setPublicKey(publicKey);
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
}
