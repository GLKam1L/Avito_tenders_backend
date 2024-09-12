package io.codefresh.gradleexample.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "organization_responsible")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationResponsible {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
