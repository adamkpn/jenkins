import groovy.json.JsonSlurper

def recurse 
def versionArraySort = { a1, a2 -> 
    def headCompare = a1[0] <=> a2[0] 
    if (a1.size() == 1 || a2.size() == 1 || headCompare != 0) { 
        return headCompare 
    } else { 
        return recurse(a1[1..-1], a2[1..-1]) 
    } 
} 
// fool Groovy to understand recursive closure 
recurse = versionArraySort
def versionStringSort = { s1, s2 -> 
    def nums = { it.tokenize('.').collect{ it.toInteger() } } 
    versionArraySort(nums(s1), nums(s2)) 
}

try {
	List<String> artifacts = new ArrayList<String>()
	def artifactsUrl = "http://artifactoryprod:8081/artifactory/api/storage/app/linux-x64"          
	def artifactsObjectRaw = 'curl http://artifactoryprod:8081/artifactory/api/storage/app/linux-x64'.execute() | 'jq -r .children'.execute()
		artifactsObjectRaw.waitFor()

	def jsonSlurper = new JsonSlurper()
	def artifactsJsonObject = jsonSlurper.parseText(artifactsObjectRaw.text)
	for(item in artifactsJsonObject){
        artifacts.add(item.uri.replaceAll('/',''))
    } 
    return artifacts.sort(versionStringSort).reverse()
} catch (Exception e) {
    print "There was a problem fetching the artifacts"
}
