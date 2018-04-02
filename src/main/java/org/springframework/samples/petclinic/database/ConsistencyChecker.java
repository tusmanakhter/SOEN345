package org.springframework.samples.petclinic.database;



import org.springframework.samples.petclinic.database.HashData;
import org.springframework.samples.petclinic.database.HashGenerationException;

public class ConsistencyChecker {

    //private static Logger log = LoggerFactory.getLogger(ConsistencyChecker.class);
	private String oldData [][];
	private String newData [][];
	private int	errorOccurance;
	private int totalRowChecked;
	private double thresholdLevel;

	public ConsistencyChecker(String oldData [][],String newData [][]) {
		this.oldData=oldData;
		this.newData=newData;
	}

	public int checkConsistency(String Table) throws HashGenerationException {
		errorOccurance = 0;
		totalRowChecked = 0;
		System.out.println("THIS IS THE TABLE NAME: " + Table);
		for (int i=0; i<oldData.length;i++)
		{
			totalRowChecked++;
			for (int j=0; j<oldData[0].length;j++) {
				String data_old = HashData.getHashFromString(oldData[i][j]);
				String data_new = HashData.getHashFromString(newData[i][j]);
				if (!(data_old.equals(data_new))) {
					System.out.println("Failed Migration");
					System.out.println(data_old + " not same as " + data_new);

					ConsistencyCheckerUpdate ccu = new ConsistencyCheckerUpdate();
					switch (Table) {
					case "owners"			:
						ccu.updateOwners(oldData[i][0], oldData[i][1], oldData[i][2], oldData[i][3], oldData[i][4], oldData[i][5]);
						break;
					case "types"			:
						ccu.updateTypes(oldData[i][0], oldData[i][1]);
						break;
					case "pets"				:
						ccu.updatePets(oldData[i][0], oldData[i][1], oldData[i][2], oldData[i][3], oldData[i][4]);
						break;
					case "specialties"		:
						ccu.updateSpecialities(oldData[i][0], oldData[i][1]);
						break;
					case "vets"				:
						ccu.updateVets(oldData[i][0], oldData[i][1], oldData[i][2]);
						break;
					case "visits"			:
						ccu.updateVisit(oldData[i][0], oldData[i][1], oldData[i][2], oldData[i][3]);
						break;
					}
					errorOccurance++;
				}
			}
		}
		thresholdCheck();
		return errorOccurance;
	}

	public void thresholdCheck() {
		
		if(errorOccurance == 0) {
			setThresholdLevel(100);
			System.out.println("Success! Total amount of rows checked: " + totalRowChecked +  "\ntreshold level: 100%");
		}else {
			setThresholdLevel((1-((errorOccurance * 1.0)/totalRowChecked))*100);
			System.out.println("Error! here are the total row checked: " + totalRowChecked + "\ntreshold level: " + getThresholdLevel() +"%");
		}
	}
	
	public void setThresholdLevel(double thresholdLevel) {
		this.thresholdLevel = thresholdLevel;
	}
	
	public double getThresholdLevel() {
		return thresholdLevel;
	}

	public void updateData() {}
}
