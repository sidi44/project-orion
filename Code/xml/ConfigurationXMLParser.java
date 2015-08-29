package xml;

import game.Configuration;
import geometry.PolygonShape;

import java.io.File;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import com.badlogic.gdx.math.Vector2;

import logic.GameConfiguration;
import physics.PhysicsConfiguration;
import render.RendererConfiguration;

/**
 * A class for parsing a Configuration XML file. If a schema filename is 
 * provided in the constructor then the XML file is validated against the schema 
 * during parsing.
 * 
 * This class can be used to attempt to parse the provided XML file to a 
 * Configuration object (see 'game' package).
 * 
 * @author Simon Dicken
 * @version 2015-08-09
 */
public class ConfigurationXMLParser {

	private Configuration config;
	private String filename;
	private String schemaFilename;
	
	/**
	 * Constructor for parsing without a schema file.
	 * 
	 * @param filename - the filename of the XML file.
	 */
	public ConfigurationXMLParser(String filename) {
		this.filename = filename;
		this.schemaFilename = "";
	}
	
	/**
	 * Constructor for parsing an XML file, and validating it against a schema.
	 *  
	 * @param filename - the filename of the XML file.
	 * @param schemaFilename - the filename of the schema.
	 */
	public ConfigurationXMLParser(String filename, String schemaFilename) {
		this.filename = filename;
		this.schemaFilename = schemaFilename;
	}
	
	/**
	 * Carry out the parsing of the XML file provided in the constructor.
	 * 
	 * This method will attempt to parse the XML file to a Configuraiton object.
	 * If a problem occurs during parsing, an error message will be printed and 
	 * the Configuration object will be default constructed. 
	 * 
	 * Postcondition: config != null
	 */
	public void parseXML() {
		
		try {
			JAXBContext context = JAXBContext.newInstance(Configuration.class);
			URL url = ClassLoader.getSystemResource(filename);
			if (url == null) {
				System.err.println("Couldn't find XML file.");
				config = new Configuration();
				return;
			}
			File file = new File(url.getFile());
			Unmarshaller unmarshaller = context.createUnmarshaller();
			
			if (!schemaFilename.isEmpty()) {
				String schemaLang = XMLConstants.W3C_XML_SCHEMA_NS_URI;
		        SchemaFactory sf = SchemaFactory.newInstance(schemaLang);
		        try {
					URL schemaUrl = 
							ClassLoader.getSystemResource(schemaFilename);
					File schemaFile = new File(schemaUrl.getFile());
					Schema schema = sf.newSchema(schemaFile);
					unmarshaller.setSchema(schema);
					unmarshaller.setEventHandler(
							new BasicValidationEventHandler());
				} catch (SAXException e1) {
					// A SAX error has occurred during parsing.
					System.err.println(e1.getMessage());
					
					// Create a default Configuration for now and try to 
					// continue.
					config = new Configuration();
				}
			}
			
			// Unmarshall the XML file.
			config = (Configuration) unmarshaller.unmarshal(file);
			
		} catch (JAXBException e2) {
			// Something's gone wrong during the unmarshalling process (e.g.
			// couldn't find file or invalid XML).
			System.err.println(e2.getMessage());
			
			// Create a default Configuration for now and try to continue.
			config = new Configuration();
		}
	}
	
	/**
	 * Obtain the game configuration.
	 * 
	 * Precondition: parseXML() has been called.
	 * 
	 * @return a class containing the game configuration parameters.
	 */
	public GameConfiguration getGameConfig() {
		
		// Check we're not in an illegal state.
		if (config == null) {
			throw new IllegalStateException("XML file has not been parsed.");
		}
		
		return config.getGameConfig();
	}
	
	/**
	 * Obtain the physics configuration.
	 * 
	 * Precondition: parseXML() has been called.
	 * 
	 * @return a class containing the physics configuration parameters.
	 */
	public PhysicsConfiguration getPhysicsConfig() {
		
		// Check we're not in an illegal state.
		if (config == null) {
			throw new IllegalStateException("XML file has not been parsed.");
		}
		
		return config.getPhysicsConfig();
	}
	
	/**
	 * Obtain the renderer configuration.
	 * 
	 * Precondition: parseXML() has been called.
	 * 
	 * @return a class containing the renderer configuration parameters.
	 */
	public RendererConfiguration getRendererConfig() {
		
		// Check we're not in an illegal state.
		if (config == null) {
			throw new IllegalStateException("XML file has not been parsed.");
		}
		
		// We need to make sure that the background dimensions have been set
		// correctly.
		GameConfiguration gameConfig = config.getGameConfig();
		PhysicsConfiguration physicsConfig = config.getPhysicsConfig();
		RendererConfiguration rendererConfig = config.getRendererConfig();
		
		// Get the maze dimension from the game configuration.
		PolygonShape dimensions = gameConfig.getDimensions();
		int maxX = dimensions.getMaxX();
		int minX = dimensions.getMinX();
		int maxY = dimensions.getMaxY();
		int minY = dimensions.getMinY();
		
		// Calculate the maze dimensions in the physics world.
		// (We currently have to do this dodgy +1 because the maze is 1 square
		// bigger in each direction than the dimensions we've given it.)
		int mazeWidth = maxX + 1 - minX;
		float width = mazeWidth * physicsConfig.getSquareSize();
		int mazeHeight = maxY + 1 - minY; 
		float height = mazeHeight * physicsConfig.getSquareSize();
		
		// Set the renderer configurations background dimensions.
		Vector2 bottomLeft = new Vector2(minX, minY);
		Vector2 topRight = new Vector2(minX + width, minY + height);
		rendererConfig.setBackgroundDimensions(bottomLeft, topRight);
		
		return rendererConfig;
	}
	
}
