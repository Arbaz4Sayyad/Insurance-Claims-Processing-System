package in.arbii.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import in.arbii.entity.BiEntity;

public interface BiService {
	
	public List<BiEntity> getApprovedCitizens();
	
	public File generateCsvFile();
	
	public void uploadToFTP(File file) throws IOException;
	
	public void processBenefits() throws Exception;
}
