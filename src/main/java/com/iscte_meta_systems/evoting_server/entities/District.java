// Districts.java (corrigido o nome da classe)
package com.iscte_meta_systems.evoting_server.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "districts")
public class District {

    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String districtName;

    @JsonIgnore
    @OneToMany
    private List<Municipality> municipalities;

    public District() {
    }

    public District(String districtName) {
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

    public List<Municipality> getMunicipalities() {
        return municipalities;
    }

    public void setMunicipalities(List<Municipality> municipalities) {
        this.municipalities = municipalities;
    }

}
