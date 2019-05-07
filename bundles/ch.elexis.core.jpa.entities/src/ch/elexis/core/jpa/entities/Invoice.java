package ch.elexis.core.jpa.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Cache;

import ch.elexis.core.jpa.entities.converter.BooleanCharacterConverterSafe;
import ch.elexis.core.jpa.entities.converter.InvoiceStateConverter;
import ch.elexis.core.jpa.entities.id.ElexisIdGenerator;
import ch.elexis.core.jpa.entities.listener.EntityWithIdListener;
import ch.elexis.core.jpa.entities.listener.InvoiceEntityListener;
import ch.elexis.core.model.InvoiceState;

@Entity
@Table(name = "RECHNUNGEN")
@EntityListeners({
	InvoiceEntityListener.class, EntityWithIdListener.class
})
@Cache(expiry = 15000)
@NamedQuery(name = "Invoice.number", query = "SELECT i FROM Invoice i WHERE i.deleted = false AND i.number = :number")
public class Invoice extends AbstractEntityWithId
		implements EntityWithId, EntityWithDeleted, EntityWithExtInfo {

	public static final String REMARK = "Bemerkung";
	
	// Transparently updated by the EntityListener
	protected Long lastupdate;
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@Column(unique = true, nullable = false, length = 25)
	private String id = ElexisIdGenerator.generateId();
	
	@Column
	@Convert(converter = BooleanCharacterConverterSafe.class)
	protected boolean deleted = false;
	
	@Basic(fetch = FetchType.LAZY)
	@Lob
	protected byte[] extInfo;
	
	@Column(length = 8, name = "RnNummer")
	protected String number;

	@OneToOne
	@JoinColumn(name = "FallID")
	protected Fall fall;

	@OneToOne
	@JoinColumn(name = "MandantID")
	protected Kontakt mandator;

	@Column(name = "RnDatum", length = 8)
	protected LocalDate invoiceDate;

	@Column(name = "RnDatumVon", length = 8)
	protected LocalDate invoiceDateFrom;

	@Column(name = "RnDatumBis", length = 8)
	protected LocalDate invoiceDateTo;

	@Column(length = 20, name = "RnStatus")
	@Convert(converter = InvoiceStateConverter.class)
	protected InvoiceState state;

	@Column(name = "StatusDatum", length = 8)
	protected LocalDate statusDate;

	@Column(length = 8, name = "Betrag")
	protected String amount;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "rechnungid")
	private List<VerrechnetCopy> invoiceBilled = new ArrayList<>();
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "rechnungsid")
	private List<Behandlung> encounters = new ArrayList<>();
	
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Fall getFall() {
		return fall;
	}

	public void setFall(Fall fall) {
		this.fall = fall;
	}

	public Kontakt getMandator() {
		return mandator;
	}

	public void setMandator(Kontakt mandator) {
		this.mandator = mandator;
	}

	public LocalDate getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(LocalDate invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public LocalDate getInvoiceDateFrom() {
		return invoiceDateFrom;
	}

	public void setInvoiceDateFrom(LocalDate invoiceDateFrom) {
		this.invoiceDateFrom = invoiceDateFrom;
	}

	public LocalDate getInvoiceDateTo() {
		return invoiceDateTo;
	}

	public void setInvoiceDateTo(LocalDate invoiceDateTo) {
		this.invoiceDateTo = invoiceDateTo;
	}

	public InvoiceState getState() {
		return state;
	}

	public void setState(InvoiceState state) {
		this.state = state;
	}

	public LocalDate getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(LocalDate statusDate) {
		this.statusDate = statusDate;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	@Override
	public byte[] getExtInfo(){
		return extInfo;
	}
	
	@Override
	public void setExtInfo(byte[] extInfo){
		this.extInfo = extInfo;
	}
	
	@Override
	public boolean isDeleted(){
		return deleted;
	}
	
	@Override
	public void setDeleted(boolean deleted){
		this.deleted = deleted;
	}
	
	@Override
	public String getId(){
		return id;
	}
	
	@Override
	public void setId(String id){
		this.id = id;
	}
	
	@Override
	public Long getLastupdate(){
		return lastupdate;
	}
	
	@Override
	public void setLastupdate(Long lastupdate){
		this.lastupdate = lastupdate;
	}
	
	public List<VerrechnetCopy> getInvoiceBilled(){
		return invoiceBilled;
	}
	
	public List<Behandlung> getEncounters(){
		return encounters;
	}
}
