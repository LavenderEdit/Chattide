package studios.tkoh.chattide.dto.request;

/**
 *
 * @author Studios TKOH!
 */
public record BusquedaRequest(
        String query,
        Integer page,
        Integer size,
        String sortBy,
        String direction) {

}
