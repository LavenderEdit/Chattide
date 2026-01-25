package studios.tkoh.chattide.views.groups;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import studios.tkoh.chattide.views.MainLayout;

/**
 *
 * @author Studios TKOH!
 */
@Route(value = "mis-grupos", layout = MainLayout.class)
@PageTitle("Mis Grupos | Chattide")
public class MisGruposView extends VerticalLayout {

    public MisGruposView() {
        add(new H2("Gestión de Grupos"));
        add("Aquí verás tus grupos y podrás crear nuevos.");
    }
}
