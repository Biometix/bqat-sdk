package io.bqat.sdk.test;

import static java.lang.Integer.parseInt;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import io.mosip.kernel.biometrics.constant.BiometricType;
import io.mosip.kernel.biometrics.entities.BDBInfo;
import io.mosip.kernel.biometrics.entities.BIR;
import io.mosip.kernel.biometrics.entities.BiometricRecord;
import io.mosip.kernel.biometrics.entities.VersionType;
import io.mosip.kernel.biometrics.model.QualityCheck;
import io.mosip.kernel.biometrics.model.QualityScore;
import io.mosip.kernel.biometrics.model.Response;
import io.bqat.sdk.impl.BqatQualitySDKService;
import io.bqat.sdk.utils.Util;

@RunWith(MockitoJUnitRunner.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BqatQualitySDKTest {
	private Logger logger = LoggerFactory.getLogger(BqatQualitySDKTest.class);

	@InjectMocks
	private BqatQualitySDKService qualitySDKService;

	private String sampleFacePath = "";
	private String sampleIrisPath = "";
	private String sampleFingerPath = "";

	@Before
	public void setUp() {
		ReflectionTestUtils.setField(qualitySDKService, "getJsonKeyFingerQualityScore", "NFIQ2");
		ReflectionTestUtils.setField(qualitySDKService, "getJsonKeyIrisQualityScore", "quality");
		ReflectionTestUtils.setField(qualitySDKService, "getJsonKeyFaceQualityScore", "quality");
		ReflectionTestUtils.setField(qualitySDKService, "getServerIpAddress", "127.0.0.1");
		ReflectionTestUtils.setField(qualitySDKService, "getServerPort", ":8848");
		ReflectionTestUtils.setField(qualitySDKService, "getServerPath", "/base64?urlsafe=true");
		ReflectionTestUtils.setField(qualitySDKService, "getContentType", "application/json");
		ReflectionTestUtils.setField(qualitySDKService, "getContentCharset", "utf-8");
		ReflectionTestUtils.setField(qualitySDKService, "getJsonResults", "results");
		ReflectionTestUtils.setField(qualitySDKService, "getContentCharset", "utf-8");
		ReflectionTestUtils.setField(qualitySDKService, "getJsonResults", "results");
		ReflectionTestUtils.setField(qualitySDKService, "getBqatEngine", "engine");
		ReflectionTestUtils.setField(qualitySDKService, "getTimestamp", "timestamp");
	}

	/*
	 * Test Finger quality JP2000
	 */
	@Test
	public void qualityFinger_JP2000() {
		setUp();
		sampleFingerPath = BqatQualitySDKTest.class.getResource("/sample_files/sample_finger_right_index.xml")
				.getPath();
		try {
			List<BiometricType> modalitiesToMatch = new ArrayList<BiometricType>() {
				{
					add(BiometricType.FINGER);
				}
			};
			BiometricRecord sample_record = xmlFileToBiometricRecord(sampleFingerPath);

			Response<QualityCheck> response = qualitySDKService.checkQuality(sample_record, modalitiesToMatch,
					new HashMap<>());
			if (response != null && response.getResponse() != null) {
				Map<BiometricType, QualityScore> scores = response.getResponse().getScores();
				if (scores != null && scores.get(BiometricType.FINGER) != null) {
					QualityScore score = scores.get(BiometricType.FINGER);
					Assert.assertTrue("Score (" + score.getScore() + ") should be greater than (0)",
							score.getScore() > 0);
				}
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Test Finger quality WSQ
	 */
	@Test
	public void qualityFinger_WSQ() {
		setUp();
		sampleFingerPath = BqatQualitySDKTest.class.getResource("/sample_files/sample_finger_right_index_wsq.xml")
				.getPath();
		try {
			List<BiometricType> modalitiesToMatch = new ArrayList<BiometricType>() {
				{
					add(BiometricType.FINGER);
				}
			};
			BiometricRecord sample_record = xmlFileToBiometricRecord(sampleFingerPath);

			Response<QualityCheck> response = qualitySDKService.checkQuality(sample_record, modalitiesToMatch,
					new HashMap<>());
			if (response != null && response.getResponse() != null) {
				Map<BiometricType, QualityScore> scores = response.getResponse().getScores();
				if (scores != null && scores.get(BiometricType.FINGER) != null) {
					QualityScore score = scores.get(BiometricType.FINGER);
					Assert.assertTrue("Score (" + score.getScore() + ") should be greater than (0)",
							score.getScore() > 0);
				}
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Test Iris quality
	 */
	@Test
	public void qualityIris() {
		setUp();
		sampleIrisPath = BqatQualitySDKTest.class.getResource("/sample_files/sample_iris_left.xml").getPath();
		try {
			List<BiometricType> modalitiesToMatch = new ArrayList<BiometricType>() {
				{
					add(BiometricType.IRIS);
				}
			};
			BiometricRecord sample_record = xmlFileToBiometricRecord(sampleIrisPath);

			Response<QualityCheck> response = qualitySDKService.checkQuality(sample_record, modalitiesToMatch,
					new HashMap<>());
			if (response != null && response.getResponse() != null) {
				Map<BiometricType, QualityScore> scores = response.getResponse().getScores();
				if (scores != null && scores.get(BiometricType.IRIS) != null) {
					QualityScore score = scores.get(BiometricType.IRIS);
					Assert.assertTrue("Score (" + score.getScore() + ") should be greater than (0)",
							score.getScore() > 0);
				}
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Test Face quality
	 */
	@Test
	public void qualityFace() {
		setUp();
		sampleFacePath = BqatQualitySDKTest.class.getResource("/sample_files/sample_face.xml").getPath();
		try {
			List<BiometricType> modalitiesToMatch = new ArrayList<>() {
				{
					add(BiometricType.FACE);
				}
			};
			BiometricRecord sample_record = xmlFileToBiometricRecord(sampleFacePath);

			Response<QualityCheck> response = qualitySDKService.checkQuality(sample_record, modalitiesToMatch,
					new HashMap<>());
			if (response != null && response.getResponse() != null) {
				Map<BiometricType, QualityScore> scores = response.getResponse().getScores();
				if (scores != null && scores.get(BiometricType.FACE) != null) {
					QualityScore score = scores.get(BiometricType.FACE);
					Assert.assertTrue("Score (" + score.getScore() + ") should be greater than (0)",
							score.getScore() > 0);
				}
			}
		} catch (ParserConfigurationException e) {
			logger.error("qualityFace", e);
		} catch (IOException e) {
			logger.error("qualityFace", e);
		} catch (SAXException e) {
			logger.error("qualityFace", e);
		}
	}

	private BiometricRecord xmlFileToBiometricRecord(String path)
			throws ParserConfigurationException, IOException, SAXException {
		BiometricRecord biometricRecord = new BiometricRecord();
		List<BIR> birSegments = new ArrayList<>();
		File fXmlFile = new File(path);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();
		logger.debug("Root element : {}", doc.getDocumentElement().getNodeName());
		Node rootBIRElement = doc.getDocumentElement();
		NodeList childNodes = rootBIRElement.getChildNodes();
		for (int temp = 0; temp < childNodes.getLength(); temp++) {
			Node childNode = childNodes.item(temp);
			if (childNode.getNodeName().equalsIgnoreCase("bir")) {
				BIR.BIRBuilder bd = new BIR.BIRBuilder();

				/* Version */
				Node nVersion = ((Element) childNode).getElementsByTagName("Version").item(0);
				String major_version = ((Element) nVersion).getElementsByTagName("Major").item(0).getTextContent();
				String minor_version = ((Element) nVersion).getElementsByTagName("Minor").item(0).getTextContent();
				VersionType bir_version = new VersionType(parseInt(major_version), parseInt(minor_version));
				bd.withVersion(bir_version);

				/* CBEFF Version */
				Node nCBEFFVersion = ((Element) childNode).getElementsByTagName("Version").item(0);
				String cbeff_major_version = ((Element) nCBEFFVersion).getElementsByTagName("Major").item(0)
						.getTextContent();
				String cbeff_minor_version = ((Element) nCBEFFVersion).getElementsByTagName("Minor").item(0)
						.getTextContent();
				VersionType cbeff_bir_version = new VersionType(parseInt(cbeff_major_version),
						parseInt(cbeff_minor_version));
				bd.withCbeffversion(cbeff_bir_version);

				/* BDB Info */
				Node nBDBInfo = ((Element) childNode).getElementsByTagName("BDBInfo").item(0);
				String bdb_info_type = "";
				String bdb_info_subtype = "";
				NodeList nBDBInfoChilds = nBDBInfo.getChildNodes();
				for (int z = 0; z < nBDBInfoChilds.getLength(); z++) {
					Node nBDBInfoChild = nBDBInfoChilds.item(z);
					if (nBDBInfoChild.getNodeName().equalsIgnoreCase("Type")) {
						bdb_info_type = nBDBInfoChild.getTextContent();
					}
					if (nBDBInfoChild.getNodeName().equalsIgnoreCase("Subtype")) {
						bdb_info_subtype = nBDBInfoChild.getTextContent();
					}
				}

				BDBInfo.BDBInfoBuilder bdbInfoBuilder = new BDBInfo.BDBInfoBuilder();
				bdbInfoBuilder.withType(Arrays.asList(BiometricType.fromValue(bdb_info_type)));
				bdbInfoBuilder.withSubtype(Arrays.asList(bdb_info_subtype));
				BDBInfo bdbInfo = new BDBInfo(bdbInfoBuilder);
				bd.withBdbInfo(bdbInfo);

				/* BDB */
				byte[] nBDB = Util.decodeURLSafeBase64(
						((Element) childNode).getElementsByTagName("BDB").item(0).getTextContent());
				bd.withBdb(nBDB);

				/* Prepare BIR */
				BIR bir = new BIR(bd);

				/* Add BIR to list of segments */
				birSegments.add(bir);
			}
		}
		biometricRecord.setSegments(birSegments);
		return biometricRecord;
	}
}