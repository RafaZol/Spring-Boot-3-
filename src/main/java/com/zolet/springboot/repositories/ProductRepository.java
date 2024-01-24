package com.zolet.springboot.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.zolet.springboot.model.ProductModel;


@Repository //bean gerenciado pelo spring
public interface ProductRepository extends JpaRepository<ProductModel, UUID>{ 

    // utilizara metodos do JpaRepository

    
}
