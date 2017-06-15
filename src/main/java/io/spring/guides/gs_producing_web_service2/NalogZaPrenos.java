//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.06.15 at 10:18:02 PM CEST 
//


package io.spring.guides.gs_producing_web_service2;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for nalog_za_prenos complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="nalog_za_prenos">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Podaci_o_duzniku">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="200"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Podaci_o_poveriocu">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="200"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Svrha_placanja">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="200"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Podaci_o_placanju">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Sifra_placanja">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;pattern value="\d{3}"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Valuta">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;length value="3"/>
 *                         &lt;pattern value="[A-Z]{3}"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Iznos">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                         &lt;fractionDigits value="2"/>
 *                         &lt;totalDigits value="15"/>
 *                         &lt;minInclusive value="0"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Finansijski_podaci_duznik" type="{http://spring.io/guides/gs-producing-web-service2}TFinansijski_podaci"/>
 *                   &lt;element name="Finansijski_podaci_poverilac" type="{http://spring.io/guides/gs-producing-web-service2}TFinansijski_podaci"/>
 *                   &lt;element name="Datum_valute" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="Hitno" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="Da"/>
 *             &lt;enumeration value="Ne"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "nalog_za_prenos", propOrder = {
    "podaciODuzniku",
    "podaciOPoveriocu",
    "svrhaPlacanja",
    "podaciOPlacanju"
})
@XmlRootElement
public class NalogZaPrenos {

    @XmlElement(name = "Podaci_o_duzniku", required = true)
    protected String podaciODuzniku;
    @XmlElement(name = "Podaci_o_poveriocu", required = true)
    protected String podaciOPoveriocu;
    @XmlElement(name = "Svrha_placanja", required = true)
    protected String svrhaPlacanja;
    @XmlElement(name = "Podaci_o_placanju", required = true)
    protected NalogZaPrenos.PodaciOPlacanju podaciOPlacanju;
    @XmlAttribute(name = "Hitno", required = true)
    protected String hitno;

    /**
     * Gets the value of the podaciODuzniku property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPodaciODuzniku() {
        return podaciODuzniku;
    }

    /**
     * Sets the value of the podaciODuzniku property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPodaciODuzniku(String value) {
        this.podaciODuzniku = value;
    }

    /**
     * Gets the value of the podaciOPoveriocu property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPodaciOPoveriocu() {
        return podaciOPoveriocu;
    }

    /**
     * Sets the value of the podaciOPoveriocu property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPodaciOPoveriocu(String value) {
        this.podaciOPoveriocu = value;
    }

    /**
     * Gets the value of the svrhaPlacanja property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSvrhaPlacanja() {
        return svrhaPlacanja;
    }

    /**
     * Sets the value of the svrhaPlacanja property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSvrhaPlacanja(String value) {
        this.svrhaPlacanja = value;
    }

    /**
     * Gets the value of the podaciOPlacanju property.
     * 
     * @return
     *     possible object is
     *     {@link NalogZaPrenos.PodaciOPlacanju }
     *     
     */
    public NalogZaPrenos.PodaciOPlacanju getPodaciOPlacanju() {
        return podaciOPlacanju;
    }

    /**
     * Sets the value of the podaciOPlacanju property.
     * 
     * @param value
     *     allowed object is
     *     {@link NalogZaPrenos.PodaciOPlacanju }
     *     
     */
    public void setPodaciOPlacanju(NalogZaPrenos.PodaciOPlacanju value) {
        this.podaciOPlacanju = value;
    }

    /**
     * Gets the value of the hitno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHitno() {
        return hitno;
    }

    /**
     * Sets the value of the hitno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHitno(String value) {
        this.hitno = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Sifra_placanja">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;pattern value="\d{3}"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Valuta">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;length value="3"/>
     *               &lt;pattern value="[A-Z]{3}"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Iznos">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *               &lt;fractionDigits value="2"/>
     *               &lt;totalDigits value="15"/>
     *               &lt;minInclusive value="0"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Finansijski_podaci_duznik" type="{http://spring.io/guides/gs-producing-web-service2}TFinansijski_podaci"/>
     *         &lt;element name="Finansijski_podaci_poverilac" type="{http://spring.io/guides/gs-producing-web-service2}TFinansijski_podaci"/>
     *         &lt;element name="Datum_valute" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "sifraPlacanja",
        "valuta",
        "iznos",
        "finansijskiPodaciDuznik",
        "finansijskiPodaciPoverilac",
        "datumValute"
    })
    public static class PodaciOPlacanju {

        @XmlElement(name = "Sifra_placanja", required = true)
        protected String sifraPlacanja;
        @XmlElement(name = "Valuta", required = true)
        protected String valuta;
        @XmlElement(name = "Iznos", required = true)
        protected BigDecimal iznos;
        @XmlElement(name = "Finansijski_podaci_duznik", required = true)
        protected TFinansijskiPodaci finansijskiPodaciDuznik;
        @XmlElement(name = "Finansijski_podaci_poverilac", required = true)
        protected TFinansijskiPodaci finansijskiPodaciPoverilac;
        @XmlElement(name = "Datum_valute", required = true)
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar datumValute;

        /**
         * Gets the value of the sifraPlacanja property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSifraPlacanja() {
            return sifraPlacanja;
        }

        /**
         * Sets the value of the sifraPlacanja property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSifraPlacanja(String value) {
            this.sifraPlacanja = value;
        }

        /**
         * Gets the value of the valuta property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getValuta() {
            return valuta;
        }

        /**
         * Sets the value of the valuta property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setValuta(String value) {
            this.valuta = value;
        }

        /**
         * Gets the value of the iznos property.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getIznos() {
            return iznos;
        }

        /**
         * Sets the value of the iznos property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setIznos(BigDecimal value) {
            this.iznos = value;
        }

        /**
         * Gets the value of the finansijskiPodaciDuznik property.
         * 
         * @return
         *     possible object is
         *     {@link TFinansijskiPodaci }
         *     
         */
        public TFinansijskiPodaci getFinansijskiPodaciDuznik() {
            return finansijskiPodaciDuznik;
        }

        /**
         * Sets the value of the finansijskiPodaciDuznik property.
         * 
         * @param value
         *     allowed object is
         *     {@link TFinansijskiPodaci }
         *     
         */
        public void setFinansijskiPodaciDuznik(TFinansijskiPodaci value) {
            this.finansijskiPodaciDuznik = value;
        }

        /**
         * Gets the value of the finansijskiPodaciPoverilac property.
         * 
         * @return
         *     possible object is
         *     {@link TFinansijskiPodaci }
         *     
         */
        public TFinansijskiPodaci getFinansijskiPodaciPoverilac() {
            return finansijskiPodaciPoverilac;
        }

        /**
         * Sets the value of the finansijskiPodaciPoverilac property.
         * 
         * @param value
         *     allowed object is
         *     {@link TFinansijskiPodaci }
         *     
         */
        public void setFinansijskiPodaciPoverilac(TFinansijskiPodaci value) {
            this.finansijskiPodaciPoverilac = value;
        }

        /**
         * Gets the value of the datumValute property.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getDatumValute() {
            return datumValute;
        }

        /**
         * Sets the value of the datumValute property.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setDatumValute(XMLGregorianCalendar value) {
            this.datumValute = value;
        }

    }

}