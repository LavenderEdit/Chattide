package studios.tkoh.chattide.views.auth;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import studios.tkoh.chattide.dto.request.UsuarioRequest;
import studios.tkoh.chattide.service.AuthService;

/**
 *
 * @author Studios TKOH!
 */
@Route("register")
@PageTitle("Registro | Chattide")
public class RegisterView extends VerticalLayout {

    private final AuthService authService;

    // Campos del formulario
    private TextField nombre = new TextField("Nombre");
    private TextField apellido = new TextField("Apellido");
    private EmailField correo = new EmailField("Correo Electrónico");
    private PasswordField password = new PasswordField("Contraseña");
    private Button submitButton = new Button("Crear Cuenta");

    public RegisterView(AuthService authService) {
        this.authService = authService;

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        H2 title = new H2("Únete a Chattide");

        // Configurar ancho de campos
        nombre.setWidth("300px");
        apellido.setWidth("300px");
        correo.setWidth("300px");
        password.setWidth("300px");

        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        submitButton.setWidth("300px");

        submitButton.addClickListener(e -> registrar());

        RouterLink loginLink = new RouterLink("¿Ya tienes cuenta? Inicia sesión", LoginView.class);

        add(title, nombre, apellido, correo, password, submitButton, loginLink);
    }

    private void registrar() {
        try {
            // Creamos el DTO manualmente (o podríamos usar Binder para validación automática)
            UsuarioRequest request = new UsuarioRequest(
                    nombre.getValue(),
                    apellido.getValue(),
                    correo.getValue(),
                    password.getValue(),
                    null // Foto perfil null por defecto
            );

            authService.registrarUsuario(request);

            Notification.show("Cuenta creada exitosamente")
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);

            UI.getCurrent().navigate(LoginView.class);

        } catch (Exception ex) {
            Notification.show("Error al registrar: " + ex.getMessage(), 5000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
}
