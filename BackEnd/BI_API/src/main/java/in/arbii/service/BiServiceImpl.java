package in.arbii.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


import com.opencsv.CSVWriter;

import in.arbii.entity.BiEntity;
import in.arbii.repo.BiRepository;


import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.FileInputStream;


@Service
public class BiServiceImpl implements BiService{

	@Autowired
	private BiRepository repo;
	
	@Override
	public List<BiEntity> getApprovedCitizens() {
		return BiRepository.findByStatus(true);
	}

	@Override
	public File generateCsvFile() {
		BiRepository.findByStatus("Approved");
		File csvFile = new File("benefits.csv");
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFile))) {
            String[] header = {"CaseNumber", "Citizen Name", "SSN", "BenefitAmt", "Account Number", "Bank Name"};
            writer.writeNext(header);
            for (BiEntity citizen : citizens) {
                String[] data = {
                        citizen.getCaseNumber(),
                        citizen.getName(),
                        citizen.getSsn(),
                        citizen.getBenefitAmount().toString(),
                        citizen.getAccountNumber(),
                        citizen.getBankName()
                };
                writer.writeNext(data);
            }
        }
        return csvFile;
	
    }

	@Override
	public void uploadToFTP(File file) throws IOException {
		
		FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect("ftp.example.com");
            ftpClient.login("ftp_user", "ftp_password");
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            try (InputStream inputStream = new FileInputStream(file)) {
                boolean done = ftpClient.storeFile("/remote/path/benefits.csv", inputStream);
                if (done) {
                    System.out.println("File is uploaded successfully.");
                }
            }
        } finally {
            ftpClient.logout();
            ftpClient.disconnect();
        }
		
	}
	
	public void processBenefits() throws Exception {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Callable<Void> task = () -> {
            List<BiEntity> citizens = getApprovedCitizens();
            File csvFile = generateCsvFile(citizens);
            uploadToFTP(csvFile);
            return null;
        };
        Future<Void> future = executorService.submit(task);
        future.get();
        executorService.shutdown();
    }
	

}
