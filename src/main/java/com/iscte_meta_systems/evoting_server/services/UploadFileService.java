package com.iscte_meta_systems.evoting_server.services;

import com.iscte_meta_systems.evoting_server.entities.Election;
import com.iscte_meta_systems.evoting_server.entities.Organisation;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface UploadFileService {

    List<Organisation> parceCSVFile(MultipartFile file, Election election);

    List<Organisation> parseElectoralCircleCSV(MultipartFile file);
}
