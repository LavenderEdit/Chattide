package studios.tkoh.chattide.service;

import org.springframework.web.multipart.MultipartFile;
import studios.tkoh.chattide.model.Usuario;

/**
 *
 * @author Studios TKOH!
 */
public interface GoogleDriveService {

    /**
     * Uploads a user avatar to the specified folder. Deletes the old one if it
     * exists.
     */
    String uploadAvatar(Usuario usuario, MultipartFile file);

    /**
     * Uploads a post image.
     */
    String uploadPostImage(Usuario usuario, MultipartFile file);

    /**
     * Deletes a file from Drive by its URL or ID.
     */
    void deleteFile(String fileUrl);
}
