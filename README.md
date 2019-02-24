# Thesis
Source code of my thesis "Measuring Semantic Similarity of Software Projects Using Comments". It can measure the similarity between two Java software projects using text-based and call-based features.

## How to Deploy
A ready-to-launch version for the [JAppService](https://github.com/maniospas/JAppService) REST-based deployment environment can be found in the *deployment* subfolder. It requires installed git command line tools to automatically download github projects for analysis.

To run it, one must run the JAppService server (included in the folder under the name *eng.auth.services-0.0.1-SNAPSHOT.jar*) either by double clicking or -to avoid the service remaining as a background task- through the command line:
```
java -jar eng.auth.services-0.0.1-SNAPSHOT.jar
```

This launches a local server on the port *localhost:8080* if not already running and then opens the html GUI that interacts with the open service. (To allow cross-domain requests, change the *origin* property in *algorithms.properties* to the incoming domain.)

## How to Use
The provided html GUI requires two Java projects. Those projects can be stored to either a local folder or be available in an repository. In each case, either the path or the URL of the project is required in each field.

To compare these projects the button bellow these fields should be pressed. If the comparison takes too long to complete, the system will stop trying to retrieve its status. In this case, pressing the button again will continue the waiting process.

The results of the comparison for the best-performing methods are finally presented in a bar-chart.
