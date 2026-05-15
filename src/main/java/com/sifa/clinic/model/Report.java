package com.sifa.clinic.model;

public class Report {
    private Long id;
    private Long examinationId;
    private String content; // Raporun detaylı içeriği
	
    // Getter, Setter, Constructor...
    
    public Report(Long id, Long examinationId, String content) {
		super();
		this.id = id;
		this.examinationId = examinationId;
		this.content = content;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public Long getExaminationId() {
		return examinationId;
	}

	public void setExaminationId(Long examinationId) {
		this.examinationId = examinationId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Override
	public String toString() {
		return "Report [id=" + id + ", examinationId=" + examinationId + ", content=" + content + "]";
	}
    
    
    
}