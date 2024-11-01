import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static java.awt.SystemColor.text;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SendingFormTest {
    private WebDriver driver;

    @BeforeAll
    public static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void sendForm() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Добряк Дмитрий");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79065647109");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button.button")).click();
        var actualElement = driver.findElement(By.cssSelector("[data-test-id=order-success]"));
        var actualText = actualElement.getText().trim();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", actualText);
        assertTrue(actualElement.isDisplayed());
    }

    @Test
    void sendEmptyForm() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("button.button")).click();
        var actualElement = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub"));
        var actualText = actualElement.getText().trim();
        assertEquals("Поле обязательно для заполнения", actualText);
        assertTrue(actualElement.isDisplayed());
    }

    @Test
    void sendNegativeName() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Dobryak Dmitriy");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79065647109");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button.button")).click();
        var actualElement = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub"));
        var actualText = actualElement.getText().trim();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", actualText);
        assertTrue(actualElement.isDisplayed());
    }

    @Test
    void sendNegativePhone() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Добряк Дмитрий");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("89065647109");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button.button")).click();
        var actualElement = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub"));
        var actualText = actualElement.getText().trim();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", actualText);
        assertTrue(actualElement.isDisplayed());
    }

    @Test
    void sendEmptyName() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79065647109");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button.button")).click();
        var actualElement = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub"));
        var actualText = actualElement.getText().trim();
        assertEquals("Поле обязательно для заполнения", actualText);
        assertTrue(actualElement.isDisplayed());
    }

    @Test
    void sendEmptyPhone() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Добряк Дмитрий");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button.button")).click();
        var actualElement = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub"));
        var actualText = actualElement.getText().trim();
        assertEquals("Поле обязательно для заполнения", actualText);
        assertTrue(actualElement.isDisplayed());
    }

    @Test
    void sendWithoutCheckbox() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Добряк Дмитрий");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79065647109");
        driver.findElement(By.cssSelector("button.button")).click();
        var actualElement = driver.findElement(By.cssSelector("[data-test-id=agreement].input_invalid"));
        assertTrue(actualElement.isDisplayed());
    }
}
