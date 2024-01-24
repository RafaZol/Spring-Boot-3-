package com.zolet.springboot.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import com.zolet.springboot.dto.ProductRecordDTO;
import com.zolet.springboot.model.ProductModel;
import com.zolet.springboot.repositories.ProductRepository;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import jakarta.validation.Valid;

@RestController
public class ProductController {

    //ponto de injeção do repositorio
    @Autowired
    ProductRepository productRepository;

    //utilização dos metodos http de forma semântica
    @PostMapping("/products") //URI 
    //metodo que retorna um model que possui o dto - @Valid para garantir as validações
    public ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductRecordDTO productRecordDTO){
        var productModel = new ProductModel(); //instancia do model que recebe os dados do dto
        BeanUtils.copyProperties(productRecordDTO, productModel); //conversão do dto para model, função do spring
        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(productModel));//retorno do status do metodo e os dados
    }

    //@GetMapping("/products")
    //public ResponseEntity<List<ProductModel>> getAllProducts(){ // retorna uma lista de produtos 
    //    return ResponseEntity.status(HttpStatus.OK).body(productRepository.findAll()); // status 200 com corpo tendo a lista de todos os produtos da base de dados
    //}

    
    @GetMapping("/products") // metodo com manipulação de valores com RepresentationModel
    public ResponseEntity<List<ProductModel>> getAllProducts(){ // retorna uma lista de produtos 
        List<ProductModel>  productList = productRepository.findAll(); // lista de todos produtos
        if(!productList.isEmpty()){
            for(ProductModel prod : productList){
                UUID id = prod.getIdproduct(); //percorre a lista buscando os ids dos produtos
                prod.add(linkTo // criação do link de redirecionamento do endpoint
                (methodOn(ProductController.class) // controller que possui o metodo do redirecionamento
                .getOneProduct(id)).withSelfRel()); // metodo que mostra os produtos
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(productList); //lista de produtos
    }

    //@GetMapping("products/{id}") // uri com id
    //public ResponseEntity<Object> getOneProduct(@PathVariable(value = "id") UUID id){ //Object pois podem ter duas stuações de retorno
    //  Optional<ProductModel> product = productRepository.findById(id); //lista pelo id
    //    if(product.isEmpty()){
    //        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found."); // se for vazio retorna não encontrado
    //    }
    //    return ResponseEntity.status(HttpStatus.OK).body(product.get()); // retorna o produto
    //}

    @GetMapping("products/{id}") // uri com id com RepresentationModel
    public ResponseEntity<Object> getOneProduct(@PathVariable(value = "id") UUID id){ //Object pois podem ter duas stuações de retorno
        Optional<ProductModel> product = productRepository.findById(id); //lista pelo id
        if(product.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found."); // se for vazio retorna não encontrado
        }
        product.get().add(linkTo(methodOn(ProductController.class).getAllProducts()).withRel("Produtos"));
        return ResponseEntity.status(HttpStatus.OK).body(product.get()); // retorna o produto
    }


    @PutMapping("/products/{id}") // uri com id que vai ser atualizado
    public ResponseEntity<Object> updateProduct(@PathVariable(value = "id")UUID id, //object para situações diferentes
     @RequestBody @Valid ProductRecordDTO productRecordDTO){  //para poder alterar os dados é necessário passar o dto
        Optional<ProductModel> product = productRepository.findById(id); // optional para diferentes retornos listando pelo id da uri
        if(product.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found."); // se não houver not found
        }
        var productModel = product.get(); // model que vai ser atualizado instanciado com o valor da base de dados
        BeanUtils.copyProperties(productRecordDTO, productModel); // conversão dos dados do dto para o model
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(productModel)); // retorno ok da lista com o produto encontrado
    }

    @DeleteMapping("/products/{id}") // uri com id a ser deletado
    public ResponseEntity<Object> deleteProduct(@PathVariable(value="id")UUID id){ // id da uri
        Optional<ProductModel> product = productRepository.findById(id); // busca do produto para exclusão
        if(product.isEmpty()){ 
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found"); // caso produto não exista not found
        }
        productRepository.delete(product.get()); // exclusão do produto
        return ResponseEntity.status(HttpStatus.OK).body("Product deleted sucessfully"); //retorno de sucesso
    }
    
}
