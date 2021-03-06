package tr.com.metu.ramazan.repository;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

import tr.com.metu.ramazan.excel.ExcelReader;
import tr.com.metu.ramazan.model.Product;

@Component
public class ProductRepository {

    private final ExcelReader excelReader;
    private final BasketTypeRepository basketTypeRepository;
    private List<Product> products;

    public ProductRepository(ExcelReader excelReader,
            BasketTypeRepository basketTypeRepository) {
        this.excelReader = excelReader;
        this.basketTypeRepository = basketTypeRepository;
    }

    @PostConstruct
    public void init() {
        products = constructProducts();

    }

    private List<Product> constructProducts() {
        return excelReader.getRowStreamFromSheet(0)
                .map(mapperRowToProduct())
                .collect(Collectors.toList());
    }

    public Product findProductById(Integer id) {
        return products.stream()
                .filter(pro -> pro.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Couldn't find product with the id: " + id));
    }

    public List<Product> getProducts() {
        return products;
    }

    public Function<Row, Product> mapperRowToProduct() {
        return row -> {
            try {
                final int basketTypeId = (int) row.getCell(3).getNumericCellValue();
                return new Product((int) row.getCell(0).getNumericCellValue(),
                        row.getCell(1).getStringCellValue(),
                        basketTypeRepository.findBasketTypeById(basketTypeId));
            } catch (Exception e) {
                return null;
            }
        };
    }

}
