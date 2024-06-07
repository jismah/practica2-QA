import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;

import java.nio.file.Paths;

public class LinkTest {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50));
            Page page = browser.newPage();
            page.navigate("http://localhost:3000/login");
            System.out.println("Titulo de Pagina " + page.title());


            page.getByLabel("email").fill("test@test.com");
            page.getByLabel("contraseña").fill("password");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Ingresar"))
                    .click();

            System.out.println("Inicio de Sesion Correcto, siguiente vista -> " + page.url());

            page.navigate("http://localhost:3000/contact");

            // Verificar Form
            boolean formPresent = page.isVisible("form#contactForm");
            boolean submitButtonPresent = page.isVisible("button[type='submit']");

            System.out.println("Formulario de contacto estado: " + formPresent);
            System.out.println("Boton de enviar estado: " + submitButtonPresent);

            // Completar formulario de contacto
            if (formPresent && submitButtonPresent) {
                page.fill("input[name='first-name']", "Test");
                page.fill("input[name='last-name']", "User");
                page.fill("input[name='email']", "testuser@example.com");
                page.fill("textarea[name='mensaje']", "Este es un mensaje de prueba.");
                page.click("button[type='submit']");

                // Esperar una respuesta o redirección después del envío
                page.waitForLoadState(LoadState.NETWORKIDLE);
                System.out.println("Formulario enviado correctamente.");
            } else {
                System.out.println("El formulario de contacto o el botón de enviar no estan presentes.");
            }

            // Crea una captura de pantalla
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("screenshotTesting.png")).setFullPage(true));

            browser.close();
        }
    }
}
