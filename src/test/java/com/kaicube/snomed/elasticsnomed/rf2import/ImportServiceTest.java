package com.kaicube.snomed.elasticsnomed.rf2import;

import com.kaicube.snomed.elasticsnomed.App;
import com.kaicube.snomed.elasticsnomed.domain.Concept;
import com.kaicube.snomed.elasticsnomed.services.BranchService;
import com.kaicube.snomed.elasticsnomed.services.ConceptService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = App.class)
public class ImportServiceTest {

	@Autowired
	private ImportService importService;

	@Autowired
	private BranchService branchService;

	@Autowired
	private ConceptService conceptService;

	@Test
	public void testImportSnapshot() throws Exception {
		final URL miniSctRelease = getClass().getResource("/MiniCT_INT_GB_20140131");
		Assert.assertNotNull(miniSctRelease);
		branchService.create("MAIN");
		final String branchPath = "MAIN/import";
		branchService.create(branchPath);
		importService.importSnapshot("/Users/kaikewley/release/SnomedCT_RF2Release_INT_20160131", branchPath);
//		importService.importSnapshot(miniSctRelease.getPath(), branchPath);

		List<Concept> concepts = new ArrayList<>();
		for (Concept concept : conceptService.findAll(branchPath)) {
			concepts.add(concept);
		}

		System.out.println(concepts.size());
	}

	@After
	public void tearDown() {
		conceptService.deleteAll();
		branchService.deleteAll();
	}

}