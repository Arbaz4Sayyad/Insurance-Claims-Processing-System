package in.arbii.service;


import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import in.arbii.bindings.CoResponse;
import in.arbii.entity.CitizenAppEntity;
import in.arbii.entity.CoTriggerEntity;
import in.arbii.entity.DcCaseEntity;
import in.arbii.entity.EligDetailsEntity;
import in.arbii.repo.CitizenAppRepository;
import in.arbii.repo.CoTriggerRepo;
import in.arbii.repo.DcCaseRepo;
import in.arbii.repo.EligDetailsRepo;
import in.arbii.util.EmailUtils;

@Service
public class CoServiceImpl implements CoService {

	@Autowired
	private CoTriggerRepo coTrgRepo;

	@Autowired
	private EligDetailsRepo eligRepo;

	@Autowired
	private CitizenAppRepository appRepo;

	@Autowired
	private DcCaseRepo dcCaseRepo;

	@Autowired
	private EmailUtils emailUtils;

	@Override
	public CoResponse processPendingTriggers() {

		final Long failed = 0l;
		final Long success = 0l;
		
		CoResponse response = new CoResponse();

		List<CoTriggerEntity> pendingTrgs = coTrgRepo.findByTrgStatus("Pending");

		response.setTotalTriggers(Long.valueOf(pendingTrgs.size()));
		
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		ExecutorCompletionService<Object> pool = new ExecutorCompletionService<>(executorService);

		// fetch all pending triggers
		
		for (CoTriggerEntity entity : pendingTrgs) {	
			pool.submit(new Callable<Object>(){
				
				@Override
				public Object call() throws Exception{
					try {
						processTrigger(response, entity);
					} catch (Exception e) {
						e.printStackTrace();
					} 
					return null;
				}
			});
		}
		
		response.setSuccTriggers(success);
		response.setFailedTriggers(failed);
		
		return response;
	}

	private CitizenAppEntity processTrigger(CoResponse response, CoTriggerEntity entity) throws Exception {
		// get eligibility data based on caseNum

		CitizenAppEntity appEntity = null;
		
		EligDetailsEntity elig = eligRepo.findByCaseNum(entity.getCaseNum());

		// get citizen data based on caseNum
		Optional<DcCaseEntity> findById = dcCaseRepo.findById(entity.getCaseNum());
		if (findById.isPresent()) {
			DcCaseEntity dcCaseEntity = findById.get();
			Integer appId = dcCaseEntity.getAppId();
			Optional<CitizenAppEntity> appEntityOptional = appRepo.findById(appId);
			if (appEntityOptional.isPresent()) {
				appEntity = appEntityOptional.get();
			}
		}
		
		// generate pdf with elig details
		// send pdf to citizen mail

		generateAndSendPdf(elig, appEntity);

		return appEntity;
	}

	private void generateAndSendPdf(EligDetailsEntity eligData, CitizenAppEntity appEntity) throws Exception {

		Document document = new Document(PageSize.A4);
		File file = new File(eligData.getCaseNum() + ".pdf");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		PdfWriter.getInstance(document, fos);

		document.open();

		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(18);
		font.setColor(Color.BLUE);

		Paragraph p = new Paragraph("Search Report", font);
		p.setAlignment(Paragraph.ALIGN_CENTER);

		document.add(p);

		PdfPTable table = new PdfPTable(7);
		table.setWidthPercentage(100f);
		table.setWidths(new float[] { 1.5f, 1.5f, 3.0f, 3.0f, 3.5f, 3.0f, 3.5f });
		table.setSpacingBefore(10);

		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.BLUE);
		cell.setPadding(5);

		font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.WHITE);

		cell.setPhrase(new Phrase("Citizen Name", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Plan Name", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Plan Status", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Plan Start Date", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Plan End Date", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Benefit Amount", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Denial Reason", font));
		table.addCell(cell);

		table.addCell(appEntity.getFullName());
		table.addCell(eligData.getPlanName());
		table.addCell(eligData.getPlanStatus());
		table.addCell(eligData.getPlanStartDate() + "");
		table.addCell(eligData.getPlanEndDate() + "");
		table.addCell(eligData.getBenefitAmt() + "");
		table.addCell(eligData.getDenialReason() + "");

		document.add(table);
		document.close();

		String subject = "HIS Eligibility Information";
		String body = "HIS Eligibility Information";

		emailUtils.sendEmail(appEntity.getEmail(), subject, body, file);
		updateTrigger(eligData.getCaseNum(), file);
		
		file.delete();
	}

	private void updateTrigger(Long caseNum, File file) throws Exception{

		CoTriggerEntity coEntity = coTrgRepo.findByCaseNum(caseNum);

		byte[] arr = new byte[(byte) file.length()];

		FileInputStream fis = new FileInputStream(file);
		fis.read(arr);

		coEntity.setCoPdf(arr);

		coEntity.setTrgStatus("Completed");

		coTrgRepo.save(coEntity);
		
		fis.close();

	}
}
