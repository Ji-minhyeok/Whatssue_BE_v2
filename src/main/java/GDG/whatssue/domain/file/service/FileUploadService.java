package GDG.whatssue.domain.file.service;

import java.io.IOException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileUploadService {

    public String saveFile(MultipartFile multipartFil, String dirName) throws IOException;
    public String downloadFile(String uploadFileName);
    public void deleteFile(String uploadFileName);
    public String getFullPath(String fileName);


}
