package br.com.alura.flix.infra.seguranca.entities;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "Usuario")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class UsuarioEntity implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = false)
	private String password;

	@Getter(AccessLevel.NONE)
	@ManyToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.EAGER)
	@JoinTable(name = "user_role", joinColumns = {
			@JoinColumn(name = "USER_ID", referencedColumnName = "ID") }, inverseJoinColumns = {
					@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID") })
	private Set<FuncaoEntity> funcoes = new HashSet<>();

	@Column(nullable = false)
	private boolean accountNonExpired = true;

	@Column(nullable = false)
	private boolean accountNonLocked = true;

	@Column(nullable = false)
	private boolean credentialsNonExpired = true;

	@Column(nullable = false)
	private boolean enabled = true;

	@CreationTimestamp
	@Column(updatable = false)
	private Timestamp createdDate;

	@UpdateTimestamp
	private Timestamp lastModifiedDate;

	@Override
	@Transient
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return this.funcoes.stream().map(funcao -> new SimpleGrantedAuthority(funcao.getNome()))
				.collect(Collectors.toSet());
	}

	public UsuarioEntity(String username, String password) {

		this.username = username;
		this.password = password;
	}

	public void adicionarFuncao(FuncaoEntity funcao) {
		
		funcoes.add(funcao);
	}
}
