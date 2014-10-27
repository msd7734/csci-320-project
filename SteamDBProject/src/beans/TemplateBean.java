package beans;

public class TemplateBean {
	
	//The Constructor will never do anything, as objects will never be created from within the program
	//These classes serve only to be back-ends to html pages
	public TemplateBean(){
		
	}
	
	//The @ManagedBean flag indicates that we will use another bean's values, so it's postconstruct will be called.
	//You can only have a ManagedBean of a higher scope (ie ViewScoped can use Session or Application Scoped beans)
	
	//The @PostConstruct flag indicates this method is what will be called before the html page makes any
	//calls to other methods.
	//Treat this as the constructor, initialize any variables and create any object instances that may be needed.
	//@PostConstruct
	public void init() {
		
	}
	
	//Action Methods: Simple stuff, methods that do things. Mostly correspond to buttons on html page.
	
	//Getters and Setters for values html page needs access to

}
