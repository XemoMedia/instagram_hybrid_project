package com.xmedia.social.mail.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "email_config")
public class EmailConfig {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(nullable = false)
	    private String host;

	    @Column(nullable = false)
	    private Integer port;

	    @Column(nullable = false)
	    private String username;

	    @Column(nullable = false)
	    private String password;

	    @Column(name = "smtp_auth")
	    private Boolean smtpAuth;

	    @Column(name = "starttls_enable")
	    private Boolean starttlsEnable;

	    @Transient
	    private String to;
}
