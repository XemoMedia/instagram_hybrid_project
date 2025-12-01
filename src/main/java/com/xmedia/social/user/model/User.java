package com.xmedia.social.user.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "xemo_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // <-- fix circular reference
@ToString(exclude = {"roles", "preferences"})    // <-- avoid StackOverflow
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String username;
	private String password;
	private String email;

	@Enumerated(EnumType.STRING)
	private UserType userType;

	@Enumerated(EnumType.STRING)
	private UserAccountType accountType; // <-- New field

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "xemo_user_roles", // <-- NEW JOIN TABLE
			joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles;

	public Set<Role> getRoles() {
		if (roles == null) {
			roles = new HashSet<>();
		}
		return roles;
	}

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<UserPreference> preferences;

	public Set<UserPreference> getPreferences() {
		if (preferences == null) {
			preferences = new HashSet<>();
		}
		return preferences;
	}

	@PrePersist
	public void onCreate() {
		createdAt = LocalDateTime.now();
		updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	public void onUpdate() {
		updatedAt = LocalDateTime.now();
	}
}
