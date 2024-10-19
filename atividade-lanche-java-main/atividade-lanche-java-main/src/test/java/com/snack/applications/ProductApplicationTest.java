package com.snack.applications;


import com.snack.entities.Product;
import com.snack.services.ProductService;
import com.snack.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class ProductApplicationTest {

    private ProductService productService;
    private ProductRepository productRepository;

    @BeforeEach
    public void setup() {
        productRepository = new ProductRepository();
        productService = new ProductService();
    }

    @Test
    public void listarTodosOsProdutosDoRepositorio() {
        productRepository.append(new Product(1, "Hotdog", 5.00f, "hotDog.jpg"));
        productRepository.append(new Product(2, "Burger", 9.50f, "burger.jpg"));
        assertEquals(2, productRepository.getAll().size());
    }

    @Test
    public void obterProdutoPorIdValido() {
        Product product = new Product(1, "Hotdog", 5.00f, "hotDog.jpg");
        productRepository.append(product);

        Product foundProduct = productRepository.getById(1);
        assertNotNull(foundProduct);
        assertEquals(1, foundProduct.getId());
    }

    @Test
    public void retornarNuloAoObterProdutoPorIdInvalido() {
        Product product = productRepository.getById(999);
        assertNull(product);
    }

    @Test
    public void verificarSeProdutoExistePorIdValido() {
        Product product = new Product(1, "Hotdog", 5.00f, "hotDog.jpg");
        productRepository.append(product);

        assertTrue(productRepository.exists(1));
    }

    @Test
    public void retornarFalsoAoVerificarExistenciaDeProdutoComIdInvalido() {
        assertFalse(productRepository.exists(999));
    }

    @Test
    public void adicionarNovoProdutoESalvarImagemCorretamente() throws Exception {
        Product product = new Product(1, "Hotdog", 5.00f, "hotDog.jpg");

        boolean saved = productService.save(product);
        assertTrue(saved);

        Path savedImagePath = Paths.get("hotDog.jpg");
        assertTrue(Files.exists(savedImagePath));
    }

    @Test
    public void removerProdutoExistenteEDeletarSuaImagem() throws Exception {
        Product product = new Product(1, "Hotdog", 5.00f, "hotDog.jpg");
        productService.save(product);

        productService.remove(1);

        Path imagePath = Paths.get("hotDog.jpg");
        assertFalse(Files.exists(imagePath));
        assertFalse(productRepository.exists(1));
    }

    @Test
    public void naoAlterarSistemaAoRemoverProdutoComIdInexistente() {
        int initialProductCount = productRepository.getAll().size();
        productService.remove(999);
        int productCountAfterRemoval = productRepository.getAll().size();

        assertEquals(initialProductCount, productCountAfterRemoval);
    }

    @Test
    public void deveAtualizarProdutoESubstituirImagem() {
        ProductService productService = new ProductService();
        Product product = new Product(1, "Hot Dog", 9.0, "hotDog.jpg");

        productService.update(product);

        String novoCaminho = productService.getImagePathById(1);
        assertEquals("C:\\Users\\yasmi\\Downloads\\atividade-lanche-java-main\\atividade-lanche-java-main\\images\\new_hotDog.jpg");
    }

}

