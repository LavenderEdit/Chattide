package studios.tkoh.chattide.views.home;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import studios.tkoh.chattide.views.MainLayout;

/**
 *
 * @author Studios TKOH!
 */
@Route(value = "home", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@PageTitle("Inicio | Chattide")
public class HomeView extends VerticalLayout {

    public HomeView() {
        add(new H2("¡Bienvenido al Feed de Chattide!"));
        add("Aquí aparecerán las publicaciones...");
    }
}
