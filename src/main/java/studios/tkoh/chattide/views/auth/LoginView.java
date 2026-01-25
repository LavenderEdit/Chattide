package studios.tkoh.chattide.views.auth;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import studios.tkoh.chattide.dto.request.auth.LoginRequest;
import studios.tkoh.chattide.dto.response.auth.AuthResponse;
import studios.tkoh.chattide.service.AuthService;
import studios.tkoh.chattide.views.home.HomeView;

/**
 *
 * @author Studios TKOH!
 */
@Route("login")
@PageTitle("Login | Chattide")
public class LoginView extends VerticalLayout {

    private final AuthService authService;

    public LoginView(AuthService authService) {
        this.authService = authService;

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        H1 title = new H1("Bienvenido a Chattide");

        LoginForm loginForm = new LoginForm();
        loginForm.addLoginListener(e -> {
            try {
                LoginRequest request = new LoginRequest(e.getUsername(), e.getPassword());
                AuthResponse response = authService.login(request);

                // TODO: Guardar token en sesión o LocalStorage
                Notification.show("Bienvenido " + response.usuario().nombre())
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);

                // Redirigir al home
                UI.getCurrent().navigate(HomeView.class);

            } catch (Exception ex) {
                loginForm.setError(true); // Muestra error visual en el form
                Notification.show("Error: " + ex.getMessage())
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        // Enlace para registrarse
        RouterLink registerLink = new RouterLink("¿No tienes cuenta? Regístrate aquí", RegisterView.class);

        add(title, loginForm, registerLink);
    }
}
