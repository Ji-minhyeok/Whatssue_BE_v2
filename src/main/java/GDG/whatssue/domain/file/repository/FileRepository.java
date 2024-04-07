package GDG.whatssue.domain.file.repository;

import GDG.whatssue.domain.file.entity.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<UploadFile, Long> {

    public void deleteById(Long uploadFileId);
}
