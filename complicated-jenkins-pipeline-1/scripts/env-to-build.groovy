try {
	List<String> temp_artifacts = new ArrayList<String>()
    List<String> temp2_artifacts = new ArrayList<String>()
    List<String> artifacts = new ArrayList<String>()
	def artifactsUrl = "http://artifactoryprod:8081/artifactory/api/storage/app-local/app/linux-x64/1.0.5.9/AppConfig"          
	def sout = new StringBuilder(), serr = new StringBuilder()
	def artifactsObjectRaw = "curl ${artifactsUrl}".execute() | "jq .children".execute() | "jq .[].uri".execute()
	artifactsObjectRaw.consumeProcessOutput(sout, serr)
	artifactsObjectRaw.waitForOrKill(1000)
//	println "$sout"

	def list = sout.readLines()
	for(item in list){
      	temp_artifacts.add(item.replaceAll('/',''))
    }  
  	for(item in temp_artifacts) {
   	 	temp2_artifacts.add(item.replaceAll('.zip',''))
   
	}
  	for(item in temp2_artifacts) {
   	 	artifacts.add(item.replaceAll('"','')) 
	}
  	return artifacts
} catch (Exception e) {
    print "There was a problem fetching the artifacts"
}
