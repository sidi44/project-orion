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


public class ConfigurationXMLParser {

	private Configuration config;
	private String filename;
	private String schemaFilename;
	
	public ConfigurationXMLParser(String filename) {
		this.filename = filename;
		this.schemaFilename = "";
	}
	
	public ConfigurationXMLParser(String filename, String schemaFilename) {
		this.filename = filename;
		this.schemaFilename = schemaFilename;
	}
	
	public void parseXML() {
		
		try {
			JAXBContext context = JAXBContext.newInstance(Configuration.class);
			URL url = ClassLoader.getSystemResource(filename);
			File file = new File(url.getFile());
			Unmarshaller unmarshaller = context.createUnmarshaller();
			
			if (!schemaFilename.isEmpty()) {
		        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		        try {
					URL schemaUrl = ClassLoader.getSystemResource(schemaFilename);
					File schemaFile = new File(schemaUrl.getFile());
					Schema schema = sf.newSchema(schemaFile);
					unmarshaller.setSchema(schema);
					//unmarshaller.setEventHandler(new MyValidationEventHandler());
				} catch (SAXException e) {
					e.printStackTrace();
				}
			}
			
			config = (Configuration) unmarshaller.unmarshal(file);
			
		} catch (JAXBException e1) {
			e1.printStackTrace();
		}
		
	}
	
	public PhysicsConfiguration getPhysicsConfig() {
		return config.getPhysicsConfig();
	}
	
	public RendererConfiguration getRendererConfig() {
		
		// We need to make sure that the background dimensions have been set
		// correctly.
		GameConfiguration gameConfig = config.getGameConfig();
		PhysicsConfiguration physicsConfig = config.getPhysicsConfig();
		RendererConfiguration rendererConfig = config.getRendererConfig();
		
		PolygonShape dimensions = gameConfig.getDimensions();
		int mazeWidth = dimensions.getMaxX() + 1 - dimensions.getMinX();
		float width = mazeWidth * physicsConfig.getSquareSize();
		int mazeHeight = dimensions.getMaxY() + 1 - dimensions.getMinY(); 
		float height = mazeHeight * physicsConfig.getSquareSize();
		Vector2 bottomLeft = new Vector2(0, 0);
		Vector2 topRight = new Vector2(width, height);
		rendererConfig.setBackgroundDimensions(bottomLeft, topRight);
		
		return rendererConfig;
	}
	
	public GameConfiguration getGameConfig() {
		return config.getGameConfig();
	}
	
}
