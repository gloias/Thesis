package main;
import analysis.automation.*;
import token.split.*;
import functionTokens.features.*;
import java.io.*;
import java.util.ArrayList;

import LSA.features.*;
public class ComparisonMainClass {

	public static void main(String[] args) throws Exception  {
		String path1 = "https://github.com/SERG-Delft/jpacman-framework";
		String path2 = "https://github.com/96Asch/jpacman-framework";
	
		System.out.println(mainApplication(path1, path2));
	}

	public static String mainApplication(String path1, String path2) {
		try {
			path1 = download(path1.trim());
			path2 = download(path2.trim());
			if(path1.isEmpty() || path2.isEmpty())
				throw new Exception("Empty paths are not valid");
			CallTreeComparison treeAlg=new CallTreeComparison();
			System.out.println("Tree method is calculated");
			double	treeSim=path1.equals(path2)?1:treeAlg.executeComparison(path1, path2,"CT1"); //set CT1,CT2 or CT3. If you don't set nothing of these, it will execute the CT1 method.
			
			FileTokenComparison tokenAlg=new FileTokenComparison();
			System.out.println("File token method is calculated");
			double tokenSim=path1.equals(path2)?1:tokenAlg.executeTwoFiles(path1, path2);
			FunctionTokenComparison funTokenAlg=new FunctionTokenComparison();
			System.out.println("Function token method is calculated");
			double functionSim=path1.equals(path2)?1:funTokenAlg.executeTwoFiles(path1, path2);
			LSAComparison lsaALg=new LSAComparison();
			System.out.println("LSA method is calculated");
			double lsaSim=path1.equals(path2)?1:lsaALg.executeTwoFiles(path1,path2,"LSA"); //set SVD or LSA. If you don't set nothing of these, it will execute the LSA method. 
			String ret = "";
			ret += "\"Tree Similarity\":\""+treeSim+"\",";
			ret += "\"File Token Similarity\":\""+tokenSim+"\",";
			ret += "\"Function Token Similarity\":\""+functionSim+"\",";
			ret += "\"LSA Similarity\":\""+lsaSim+"\"";
			return "{"+ret+"}";
		}
		catch(Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			return sw.toString();	
		}
	}
	
	public static String download(String repo) {
		if(!repo.startsWith("https://github.com/"))
			return repo;
		String folder = "downloads/"+repo.substring("https://github.com/".length());
		if(!repo.endsWith(".git"))
			repo += ".git";
		else
			repo = repo.substring(0, repo.length()-4);
		downloadRepo(repo, folder);
		return folder;
	}
	

	public static void downloadRepo(String repo, String targetFolder) {
		if((new File(targetFolder)).exists())
			return;
		if(!targetFolder.endsWith("/"))
			targetFolder += "/";
		System.out.println("Downloading "+repo+" in folder "+targetFolder+"...");
		new File(targetFolder).mkdirs();
		runCommand("git clone "+repo+" "+targetFolder);
		System.out.println("Finished");
	}
	

	private static Runtime runtime = Runtime.getRuntime();
	protected static ArrayList<String> runCommand(String command) {
		try {
			Process proc = runtime.exec(command);
			InputStream stdin = proc.getInputStream();
			InputStreamReader isr = new InputStreamReader(stdin);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			ArrayList<String> lines = new ArrayList<String>();
			while ( (line = br.readLine()) != null) {
				lines.add(line);
			}
			return lines;
		}
		catch(Exception e) {
			e.printStackTrace();
			return new ArrayList<String>();
		}
	}

}
