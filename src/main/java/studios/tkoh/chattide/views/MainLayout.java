package studios.tkoh.chattide.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;
import studios.tkoh.chattide.views.auth.LoginView;
import studios.tkoh.chattide.views.groups.MisGruposView;
import studios.tkoh.chattide.views.home.HomeView;

/**
 *
 * @author Studios TKOH!
 */
public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Chattide");
        logo.addClassNames(
                LumoUtility.FontSize.LARGE,
                LumoUtility.Margin.MEDIUM
        );

        // Botón de menú hamburguesa
        DrawerToggle toggle = new DrawerToggle();

        // Botón de Logout (Simulado por ahora)
        Button logout = new Button("Salir", new Icon(VaadinIcon.SIGN_OUT));
        logout.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(LoginView.class)));

        HorizontalLayout header = new HorizontalLayout(toggle, logo, logout);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo); // Empuja el botón de salir a la derecha
        header.setWidthFull();
        header.addClassNames(
                LumoUtility.Padding.Vertical.NONE,
                LumoUtility.Padding.Horizontal.MEDIUM
        );

        addToNavbar(header);
    }

    private void createDrawer() {
        // Enlaces de navegación
        // Nota: Crearemos estas clases (HomeView, MisGruposView) en el siguiente paso
        RouterLink homeLink = new RouterLink("Inicio", HomeView.class);
        RouterLink groupsLink = new RouterLink("Mis Grupos", MisGruposView.class);

        VerticalLayout list = new VerticalLayout(homeLink, groupsLink);
        addToDrawer(list);
    }
}
