package xml;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler; 
import javax.xml.bind.ValidationEventLocator;

/**
 * A straight forward implementation of the JAXB ValidationEventHandler 
 * interface. 
 * 
 * If an error occurs whilst parsing the XML file, the details of the validation
 * event are printed. If the severity of the error is 'fatal error' then the 
 * handler will indication that the exception should be thrown. If the severity
 * is 'error' or 'warning', the handler indicates that the parser should attempt
 * to continue.
 * 
 * @author Simon Dicken
 * @version 2015-08-09
 */
public class BasicValidationEventHandler implements ValidationEventHandler {

	@Override
	public boolean handleEvent(ValidationEvent event) {
		
		// Print out details of the event.
		int severity = event.getSeverity();
	    System.err.println("\nEVENT");
	    System.err.println("SEVERITY:  " + severity);
	    System.err.println("MESSAGE:  " + event.getMessage());
	    System.err.println("LINKED EXCEPTION:  " + event.getLinkedException());
	    System.err.println("LOCATOR");
	    ValidationEventLocator locator = event.getLocator();
	    System.err.println("\tLINE NUMBER:\t" + locator.getLineNumber());
	    System.err.println("\tCOLUMN NUMBER:\t" + locator.getColumnNumber());
	    System.err.println("\tOFFSET:\t" + locator.getOffset());
	    System.err.println("\tOBJECT:\t" + locator.getObject());
	    System.err.println("\tNODE:\t" + locator.getNode());
	    System.err.println("\tURL:\t" + locator.getURL());

	    // If it's an error or a warning, we'll try to continue.
	    // If it's a fatal error, indicate that an exception should be thrown.
		if (severity == ValidationEvent.WARNING || 
			severity == ValidationEvent.ERROR) {
			return true;
		} else if (severity == ValidationEvent.FATAL_ERROR) {
			return false;
		}
		
		// Don't expect to ever get here...
        return false;
	}

}
