package api;

import java.io.File;
//import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import utils.JSONToExcelConverter;

import static io.restassured.RestAssured.*;

public class TwoWayReconciliation {

	@Test
	public void performTwoWayReconciliation() throws IOException {
		// Code to perform two-way reconciliation
		System.out.println("Performing two-way reconciliation...");
		// Here you would typically implement the logic to compare two sets of data and identify discrepancies
		
		RestAssured.baseURI = "https://reconciliation-api.purplemushroom-d9ddba87.uksouth.azurecontainerapps.io"; // Set the base URI for the API
	
			File file1 = new File("src/test/resources/testdata/Bank- 70155.xlsx"); // Replace with the actual path to the first dataset
			File file2 = new File("src/test/resources/testdata/Yardo-11020.xlsx"); // Replace with the actual path to the first dataset
			
			Response respose=
					(Response) given()
					.log().all()
						//.header("Authorization", "Bearer token1234567890") // Replace with the actual authorization token if required
						.multiPart("files", file1)
						.multiPart("files", file2)
						.contentType("multipart/form-data")
					
	.when()
		.post("/api/v1/reconcile")
	.then()
	.log().all()
		//.statusCode(200)
		.extract().response();
			
			String jsonResponse = respose.asPrettyString();
			System.out.println("Response: " + respose.asPrettyString());
			
			try {
				Files.write(Paths.get("src/test/resources/ReponseData/respose1.json"),jsonResponse.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
			System.out.println("Response saved to response1.json");
			
			JSONToExcelConverter.converter(jsonResponse, "src/test/resources/ReponseData/response.xlsx");	
		
		
}
}