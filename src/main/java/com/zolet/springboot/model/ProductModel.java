package com.zolet.springboot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;

@Entity
@Table(name =  "tb_products")
public class ProductModel extends RepresentationModel<ProductModel> implements Serializable { // quando for utilizar, iternamente já possui alguns metodos de navegabilidade
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idproduct;
    
    private String name;
    
    private BigDecimal value;

    public UUID getIdproduct() {
        return idproduct;
    }

    public void setIdproduct(UUID idproduct) {
        this.idproduct = idproduct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
    
    
    
}
