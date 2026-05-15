package com.sifa.clinic.model;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Payment {
    private Long id;
    private Long appointmentId;
    private BigDecimal amount;            // Toplam tutar
    private BigDecimal insuranceDiscount; // Sigorta indirimi
    private PaymentMethod method;         // NAKIT, KREDI_KARTI vb.
    private LocalDateTime paidAt;         // Ödeme zamanı
    
    // Getter, Setter, Constructor...

    public enum PaymentMethod {
        CASH,
        CREDIT_CARD,
        INSURANCE_COVERED // Tamamı sigortadan karşılanmışsa
    }
    

	public Payment(Long id, Long appointmentId, BigDecimal amount, BigDecimal insuranceDiscount, PaymentMethod method,
			LocalDateTime paidAt) {
		super();
		this.id = id;
		this.appointmentId = appointmentId;
		this.amount = amount;
		this.insuranceDiscount = insuranceDiscount;
		this.method = method;
		this.paidAt = paidAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(Long appointmentId) {
		this.appointmentId = appointmentId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getInsuranceDiscount() {
		return insuranceDiscount;
	}

	public void setInsuranceDiscount(BigDecimal insuranceDiscount) {
		this.insuranceDiscount = insuranceDiscount;
	}

	public PaymentMethod getMethod() {
		return method;
	}

	public void setMethod(PaymentMethod method) {
		this.method = method;
	}

	public LocalDateTime getPaidAt() {
		return paidAt;
	}

	public void setPaidAt(LocalDateTime paidAt) {
		this.paidAt = paidAt;
	}

	@Override
	public String toString() {
		return "Payment [id=" + id + ", appointmentId=" + appointmentId + ", amount=" + amount + ", insuranceDiscount="
				+ insuranceDiscount + ", method=" + method + ", paidAt=" + paidAt + "]";
	}
    
	
	
    
}