package studios.tkoh.chattide.service.impl;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import studios.tkoh.chattide.exception.ChattideException;
import studios.tkoh.chattide.model.Usuario;
import studios.tkoh.chattide.service.GoogleDriveService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

/**
 *
 * @author Studios TKOH!
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class GoogleDriveServiceImpl implements GoogleDriveService {

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    @Value("${google.drive.oauth.client-id}")
    private String clientId;

    @Value("${google.drive.oauth.client-secret}")
    private String clientSecret;

    @Value("${google.drive.oauth.refresh-token}")
    private String refreshToken;

    @Value("${google.drive.folders.user-avatars}")
    private String avatarsFolderId;

    @Value("${google.drive.folders.user-posts}")
    private String postsFolderId;

    private Drive driveService;

    @PostConstruct
    public void init() {
        try {
            TokenResponse response = new GoogleRefreshTokenRequest(
                    HTTP_TRANSPORT, JSON_FACTORY, refreshToken, clientId, clientSecret)
                    .execute();

            Credential credential = new Credential(BearerToken.authorizationHeaderAccessMethod())
                    .setAccessToken(response.getAccessToken());

            this.driveService = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                    .setApplicationName("Chattide")
                    .build();
        } catch (IOException e) {
            log.error("Error initializing Google Drive Service", e);
        }
    }

    @Override
    public String uploadAvatar(Usuario usuario, MultipartFile file) {
        // 1. Delete old avatar if it exists in Drive
        if (usuario.getFotoPerfil() != null && usuario.getFotoPerfil().contains("drive.google.com")) {
            deleteFile(usuario.getFotoPerfil());
        }

        // 2. Generate custom name: Name_LastName_Date_CopyCount
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String extension = getFileExtension(file.getOriginalFilename());

        // Simulating copy number for this specific upload
        String fileName = String.format("%s_%s_%s_v1.%s",
                usuario.getNombre(), usuario.getApellido(), timestamp, extension);

        return uploadToFolder(file, fileName, avatarsFolderId);
    }

    @Override
    public String uploadPostImage(Usuario usuario, MultipartFile file) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = String.format("POST_%s_%s.%s",
                usuario.getNombre(), timestamp, getFileExtension(file.getOriginalFilename()));

        return uploadToFolder(file, fileName, postsFolderId);
    }

    @Override
    public void deleteFile(String fileUrl) {
        try {
            String fileId = extractIdFromUrl(fileUrl);
            if (fileId != null) {
                driveService.files().delete(fileId).execute();
            }
        } catch (IOException e) {
            log.warn("Could not delete file from Drive: {}", fileUrl);
        }
    }

    private String uploadToFolder(MultipartFile file, String fileName, String folderId) {
        try {
            File fileMetadata = new File();
            fileMetadata.setName(fileName);
            fileMetadata.setParents(Collections.singletonList(folderId));

            java.io.File tempFile = java.io.File.createTempFile("upload-", ".tmp");
            file.transferTo(tempFile);

            FileContent mediaContent = new FileContent(file.getContentType(), tempFile);
            File uploadedFile = driveService.files().create(fileMetadata, mediaContent)
                    .setFields("id, webViewLink")
                    .execute();

            // Make file readable to everyone (public link)
            driveService.permissions().create(uploadedFile.getId(),
                    new com.google.api.services.drive.model.Permission()
                            .setRole("reader")
                            .setType("anyone"))
                    .execute();

            tempFile.delete();
            return uploadedFile.getWebViewLink();
        } catch (IOException e) {
            throw new ChattideException("Error uploading file to Google Drive: " + e.getMessage());
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "png";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private String extractIdFromUrl(String url) {
        // Basic extractor for Drive URLs
        if (url == null) {
            return null;
        }
        if (url.contains("id=")) {
            return url.split("id=")[1].split("&")[0];
        }
        if (url.contains("/d/")) {
            return url.split("/d/")[1].split("/")[0];
        }
        return null;
    }
}
