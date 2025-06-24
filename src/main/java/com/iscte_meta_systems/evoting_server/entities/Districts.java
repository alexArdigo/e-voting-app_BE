// Districts.java (corrigido o nome da classe)
package com.iscte_meta_systems.evoting_server.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "districts")
public class Districts {

    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String districtName;

    @OneToMany
    private List<Municipalities> municipalities;

    public Districts() {
    }

    public Districts(String districtName) {
        this.districtName = districtName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public List<Municipalities> getMunicipalities() {
        return municipalities;
    }

    public void setMunicipalities(List<Municipalities> municipalities) {
        this.municipalities = municipalities;
    }

}
